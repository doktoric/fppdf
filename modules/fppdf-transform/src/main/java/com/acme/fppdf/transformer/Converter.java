package com.acme.fppdf.transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.acme.fppdf.domain.Conversion;
import com.acme.fppdf.domain.ConversionType;

/**
 * Pdf based implementation to converter interface.
 *
 * @author Richard_Doktorics
 * @author Gergely_Nagy1
 */
@Component
public class Converter {

    private static final String XML = ".xml";
    private static final String MM = ".mm";
    private static final String XSL_BASE_URL = "/xslts/";
    private static final String XSL_MM_TO_PDF_FOP = XSL_BASE_URL + "transform.xsl";
    private Logger logger = LoggerFactory.getLogger(Converter.class);
    private final TransformerFactory transformerFactory;

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;

    /**
     * Constructor: sets up XSL and FO transformer factories
     */
    public Converter() {
        transformerFactory = TransformerFactory.newInstance();

        try {
            fopFactory = FopFactory.newInstance();
            fopFactory.setBaseURL(getClass().getResource(XSL_BASE_URL)
                    .toString());
            fopFactory.setUserConfig(new DefaultConfigurationBuilder()
                    .build(getClass().getResourceAsStream("/fopconfig.xml")));
            foUserAgent = fopFactory.newFOUserAgent();

        } catch (SAXException | IOException | ConfigurationException e) {
            throw new ConversionException("Error while configuring FO", e);
        }
    }

    /**
     * Perform conversion. Takes conversion.fromBytes and conversion.toType, and sets conversion.toBytes with the conversion result.
     *
     * @param conversion conversion to perform
     */
    public void convert(Conversion conversion) {

        Validate.notNull(conversion, "conversion parameter was null");
        Validate.notNull(conversion.getFromBytes(),
                "conversion.fromBytes was null");
        Validate.notNull(conversion.getToType(), "conversion.toType was null");

        if (ConversionType.PDF.equals(conversion.getToType())) {

            ByteArrayStreamResult foResult = createFo(conversion,
                    XSL_MM_TO_PDF_FOP);
            
            // TODO : delete begin

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                baos.write(foResult.toByteArray());
            } catch (Exception e) {
                logger.error(e.toString());
            }
            // logger.error("\n========================================\n" + baos.toString() + "\n-------------------------------------------\n");

            // TODO : delete end

            doFoConversion(conversion, foResult.toStreamSource());

        } else {
            Validate.isTrue(false,
                    "Currently only PDF output is supported, found "
                            + conversion.getToType());
        }
    }

    /**
     * get a xml file to array in the zip
     * @param conversion the paramter objest
     * @return xml byte array
     */
    private byte[] convertArrayToZip(Conversion conversion) {
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(conversion.getFromBytes()));

        ZipEntry entry = null;
        ByteArrayOutputStream out = null;

        try {
            while ((entry = zipStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (isMMorXML(entryName)) {
                    out = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipStream.read(buf)) != -1) {
                        out.write(buf, 0, bytesRead);
                    }
                    out.close();
                    break;
                }

            }
            zipStream.close();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return out.toByteArray();
    }

    /**
     * parameter file is mm or xml
     * @param entryName the name of the file
     * @return the boolean value that says this file is xml or mm
     */
    private boolean isMMorXML(String entryName) {
        return entryName.toLowerCase().endsWith(MM) || entryName.toLowerCase().endsWith(XML);
    }

    /**
     * Creates an XSL FO stylesheet from the specified conversion and XSL stylestheet
     *
     * @param conversion the conversion to take parameters from
     * @param styleSheetPath path to the XSL to generate the XSL FO
     * @return a StreamResult containing the XSL FO stylesheet as byte array
     */
    private ByteArrayStreamResult createFo(Conversion conversion,
            String styleSheetPath) {

        try {
            StreamSource mmSource = null;
            if (isMMorXML(conversion.getName())) {
                mmSource = createStreamSource(conversion.getFromBytes());
            } else {
                mmSource = createStreamSource(convertArrayToZip(conversion));
            }
            StreamSource mmToFoSource = createStreamSource(styleSheetPath);
            ByteArrayStreamResult foResult = new ByteArrayStreamResult();

            Transformer transformer = transformerFactory           .newTransformer(mmToFoSource);
            transformer.setParameter("fileName", conversion.getName());
            transformer.setParameter("orientation", conversion.getOrientation().name());
            transformer.setParameter("sizeFactor", conversion.getSizeFactor());
            transformer.transform(mmSource, foResult);
            conversion.setFoBytes(foResult.toByteArray());
            return foResult;

        } catch (IOException e) {
            throw new ConversionException(
                    "Error while loading mm to fo stylesheets for "
                            + conversion.getName(), e);

        } catch (TransformerException e) {
            throw new ConversionException(
                    "Error while transforming mm to fo for "
                            + conversion.getName(), e);
        }
    }


    /**
     * Perform the specified conversion using XSL FO
     *
     * @param conversion the conversion to perform, the result is set on conversion.toBytes
     * @param foSource the XSL FO stylesheet as a StreamSource
     */
    private void doFoConversion(Conversion conversion, StreamSource foSource) {

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent,
                    outStream);
            ByteArraySAXResult fopResult = new ByteArraySAXResult(
                    fop.getDefaultHandler(), outStream);
            fop.getUserAgent().setURIResolver(new CustomURIResolver(conversion));

            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(foSource, fopResult);
            conversion.setToBytes(fopResult.toByteArray());
        } catch (FOPException e) {
            logger.error(e.getMessage());
            throw new ConversionException(
                    "Error while creating Fop processor for "
                            + conversion.getName(), e);

        } catch (TransformerException e) {
            logger.error(e.getMessage());
            throw new ConversionException(
                    "Error while transforming fo to pdf for "
                            + conversion.getName(), e);
        }
    }

    /**
     * Open resource at the specified path as a StreamSource
     * 
     * @param path the resource path
     * @return the StreamSource wrapping the resource at the specified path
     * @throws ConversionException if the resource is not found
     */
    private StreamSource createStreamSource(byte[] bytes) {
        return new StreamSource(new ByteArrayInputStream(bytes));
    }

    /**
     * Wrap a byte array in a StreamSource
     * 
     * @param bytes byte array to wrap
     * @return the StreamSource wrapping the byte array
     */
    private StreamSource createStreamSource(String path) throws IOException {

        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new ConversionException("Resource not found: " + path);
        }
        return new StreamSource(is, getClass().getResource(path).toString());
    }
}
