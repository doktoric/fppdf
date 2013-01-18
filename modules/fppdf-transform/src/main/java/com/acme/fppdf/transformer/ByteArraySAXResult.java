package com.acme.fppdf.transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.ContentHandler;

/**
 * Extends a SAXResult so the wrapped byte array can be easily retrieved.
 * 
 * @author Gergely_Nagy1
 *
 */
public class ByteArraySAXResult extends SAXResult {

    private ByteArrayOutputStream os;
    
    /**
     * Constructs a new ByteArraySAXResult
     * 
     * @param handler the SAX handler to use
     * @param os the same ByteArrayOutputStream passed to the SAX handler
     */
    public ByteArraySAXResult(ContentHandler handler, ByteArrayOutputStream os) {
        super(handler);
        this.os = os;
    }
    
    /** 
     * Return copy of wrapped byte array
     * 
     * @return new copy of the wrapped byte array
     */
    public byte[] toByteArray() {
        return os.toByteArray();
    }
    
    /** 
     * Convert this result to a source
     * 
     * @return this result converted to a source using the same byte array
     */
    public StreamSource toStreamSource() {
        return new StreamSource(new ByteArrayInputStream(toByteArray()));
    }
}
