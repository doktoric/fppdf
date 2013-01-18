package com.acme.fppdf.transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOURIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.fppdf.domain.Conversion;

/**
 * Custom Uri resolver to zip file
 * 
 * @author Richard_Doktorics
 * 
 */
public class CustomURIResolver extends FOURIResolver {

    private static final String ZIP = ".zip";

    private Logger logger = LoggerFactory
            .getLogger(CustomURIResolver.class);
    /**
     * Conversion object
     */
    private Conversion conversion;

    /**
     * if we have conversion object
     * 
     * @param conversion
     *            parameter object
     */
    public CustomURIResolver(Conversion conversion) {
        this();
        this.conversion = conversion;
    }

    /**
     * empty constructor
     */
    public CustomURIResolver() {
        super();
        this.conversion = null;
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        Source source = null;
        
        if (isZip(conversion.getName())) {
            source = getFileWithName(href);
        }
        
        if (source == null) {
            source = super.resolve(href, base);
        }
        return source;
    }

    private Source getFileWithName(String href) {
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(conversion.getFromBytes()));
        ZipEntry entry = null;
        StreamSource source = null;
        try {
            while ((entry = zipStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.equals(href)) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int bytesRead = 0;

                    while ((bytesRead = zipStream.read(buf)) != -1) {
                        out.write(buf, 0, bytesRead);

                        out.close();
                        source = new StreamSource(new ByteArrayInputStream(out.toByteArray()));
                       
                    }
                    zipStream.closeEntry();
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }

        return source;
    }
    
    /**
     * parameter file is zip or not
     * @param name the conversion object name
     * @return true or false that depends on the file is zip or not
     */
    private boolean isZip(String name) {
        return (name.toLowerCase().endsWith(ZIP)) ? true : false;
    }
    

}
