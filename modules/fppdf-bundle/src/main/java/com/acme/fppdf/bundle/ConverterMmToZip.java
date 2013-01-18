package com.acme.fppdf.bundle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.acme.fppdf.bundle.content.ContentProviderManager;
import com.acme.fppdf.bundle.xpath.MatchesFunctionResolver;

/**
 * Converts mindmap files to ZIP archives that contain external resources referenced from the original mindmap file
 *
 */
public class ConverterMmToZip {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterMmToZip.class);

    private static final String RESOURCE_PATH_PREFIX = "resources/";
    private static final String RESOURCE_MAPPING_ENTRY_NAME = "resource_mapping";
    private static final String DEFAULT_ENCODING_NAME = "UTF-8";

    private static final String[] EXCLUDED_PREFIXES = {
        "#ID_",
        "#_Freeplane_Link_",
        "freeplaneresource:",
        "mailto:",
        "menuitem:",
    };
    private static final String[] RECOGNIZED_SUFFIXES = {
        ".bmp",
        ".gif",
        ".ico",
        ".jpg",
        ".png",
    };
    private static final String INCLUDED_PREFIXES_CONDITION;
    static {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        b.append("(");
        for (String s: EXCLUDED_PREFIXES) {
            if (first) {
                first = false;
            } else {
                b.append(" and ");
            }
            b.append(String.format("not(starts-with(.,'%s'))", s));
        }
        b.append(")");
        INCLUDED_PREFIXES_CONDITION = b.toString();
    }
    private static final String RECOGNIZED_SUFFIXES_CONDITION;
    static {
        StringBuilder b = new StringBuilder();
        b.append("fppdf:matches(string(.),'(?i)^.*((");
        boolean first = true;
        for (String s: RECOGNIZED_SUFFIXES) {
            if (first) {
                first = false;
            } else {
                b.append(")|(");
            }
            b.append(Pattern.quote(s));
        }
        b.append("))\\s*$')");
        RECOGNIZED_SUFFIXES_CONDITION = b.toString();
    }
    private static final String[] XPATH_EXPRESSIONS = {
        String.format(
                "//hook[@NAME='ExternalObject']/@URI[%s and %s]",
                INCLUDED_PREFIXES_CONDITION,
                RECOGNIZED_SUFFIXES_CONDITION),
        String.format(
                "//node/@LINK[%s and %s]",
                INCLUDED_PREFIXES_CONDITION,
                RECOGNIZED_SUFFIXES_CONDITION),
        String.format(
                "//*[fppdf:matches(name(),'(?i)^html$')]//*[fppdf:matches(name(),'(?i)^img$')]/@*[fppdf:matches(name(),'(?i)^src$') and %s and %s]",
                INCLUDED_PREFIXES_CONDITION,
                RECOGNIZED_SUFFIXES_CONDITION),
    };
    private static final String DOT = ".";
    private static final String COLON = ":";

    private InputStream src;
    private ZipOutputStream dst;
    private String mapName;
    private String baseDirName;
    private ContentProviderManager contentProviders;

    private Map<String, String> mappedResources = new TreeMap<>();
    private long sequence;
    private String encodingName;

    /**
     * Converts a mindmap input stream into a version that contains remapped references to locally stored copies of external resources.
     *
     * @param src
     *            Input stream of mindmap file contents
     * @param dst
     *            Zip output stream of the mindmap file with remapped resource references to locally stored instances
     * @param mapFileName
     *            The name of the new mindmap file that will be used in the output ZIP archive
     * @param contentProviders
     *            The content providers that will fetch the exeternal content based on the URI prefix
     */
    public ConverterMmToZip(InputStream src, ZipOutputStream dst, String mapFileName, ContentProviderManager contentProviders) {
        this.src = src;
        this.dst = dst;

        File mapFile = new File(mapFileName);
        this.mapName = mapFile.getName();
        this.baseDirName = new File(mapFileName).getParent();
        if (baseDirName == null) {
            baseDirName = DOT;
        }

        this.contentProviders = contentProviders;
    }

    /**
     * Performs the conversion
     *
     * @param baseDirName
     *            Base directory to be used as anchor for relative references
     * @throws Exception
     *             any kind of exception encountered during conversion is passed through
     */
    public void convert(String baseDirName) throws Exception {
        Document mindmapDocument = XmlUtil.xmlStreamToDom(src);

        encodingName = mindmapDocument.getXmlEncoding();
        if (encodingName == null) {
            encodingName = DEFAULT_ENCODING_NAME;
        }

        Set<String> processedResources = new HashSet<>();
        for (String xpathExpression : XPATH_EXPRESSIONS) {
            NodeList nodes = XmlUtil.getMatchingNodes(mindmapDocument, xpathExpression);
            int nodeCount = nodes.getLength();
            for (int i = 0; i < nodeCount; ++i) {
                UriMapping uriMapping = remapResourceUri((Attr) nodes.item(i));
                String mappedUri = uriMapping.getMappedUri();
                if (!processedResources.contains(mappedUri)) {
                    String originalUri = uriMapping.getOriginalUri();
                    byte[] resourceContent = fetchExternalContent(originalUri, baseDirName);
                    if (resourceContent == null) {
                        LOGGER.warn(String.format("Empty content: '%s'", originalUri));
                        resourceContent = new byte[0];
                    }
                    writeEntry(mappedUri, resourceContent);
                    processedResources.add(mappedUri);
                }
            }
        }

        writeEntry("/" + mapName, XmlUtil.domToByteArray(mindmapDocument));
        writeResourceMappingEntry();
    }

    private UriMapping remapResourceUri(Attr attribute) {
        UriMapping result = new UriMapping();

        String originalUri = attribute.getValue();
        result.setOriginalUri(originalUri);

        String mappedUri = mappedResources.get(originalUri);
        if (mappedUri == null) {
            mappedUri = RESOURCE_PATH_PREFIX + (sequence++);
            int extensionPos = originalUri.lastIndexOf(DOT);
            if (extensionPos != -1) {
                mappedUri += originalUri.substring(extensionPos);
            }
            mappedResources.put(originalUri, mappedUri);
        }

        attribute.setValue(mappedUri);
        result.setMappedUri(mappedUri);
        LOGGER.info(String.format("'%s' ---> '%s'", originalUri, mappedUri));

        return result;
    }

    private byte[] fetchExternalContent(String externalContentName, String baseDirName) throws Exception {
        return contentProviders
                .getProviderForPrefix(externalContentName.split(COLON, 2)[0].trim() + COLON)
                .fetchContent(externalContentName, baseDirName);
    }

    private void writeResourceMappingEntry() throws IOException {
        StringBuilder mappingEntryBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : mappedResources.entrySet()) {
            mappingEntryBuilder
                    .append(entry.getKey())
                    .append(":\t\t")
                    .append(entry.getValue())
                    .append("\n");
        }
        writeEntry(RESOURCE_MAPPING_ENTRY_NAME, mappingEntryBuilder.toString().getBytes(encodingName));
    }

    private void writeEntry(String name, byte[] content) throws IOException {
        dst.putNextEntry(new ZipEntry(name));
        dst.write(content);
        dst.closeEntry();
    }

    private static class UriMapping {
        private String originalUri;
        private String mappedUri;

        /**
         * @return the originalUri
         */
        public String getOriginalUri() {
            return originalUri;
        }

        /**
         * @param originalUri
         *            the originalUri to set
         */
        public void setOriginalUri(String originalUri) {
            this.originalUri = originalUri;
        }

        /**
         * @return the mappedUri
         */
        public String getMappedUri() {
            return mappedUri;
        }

        /**
         * @param mappedUri
         *            the mappedUri to set
         */
        public void setMappedUri(String mappedUri) {
            this.mappedUri = mappedUri;
        }
    }

    private static final class XmlUtil {
        private XmlUtil() {
        }

        private static byte[] domToByteArray(Document document) throws Exception {
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

            Transformer identityTransformer = TransformerFactory.newInstance().newTransformer();
            identityTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            identityTransformer.transform(new DOMSource(document), new StreamResult(resultStream));

            return resultStream.toByteArray();
        }

        private static Document xmlStreamToDom(InputStream src) throws Exception {
            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(src);
        }

        private static NodeList getMatchingNodes(Document document, String expressionSource) throws Exception {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setXPathFunctionResolver(new MatchesFunctionResolver());
            return (NodeList) xpath
                    .compile(expressionSource)
                    .evaluate(document, XPathConstants.NODESET);
        }
    }
}
