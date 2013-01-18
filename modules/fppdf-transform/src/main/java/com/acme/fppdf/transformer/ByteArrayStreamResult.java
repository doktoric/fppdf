package com.acme.fppdf.transformer;

import java.io.ByteArrayInputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Extends a StreamResult so the wrapped byte array can be easily retrieved.
 * 
 * @author Gergely_Nagy1
 *
 */
public class ByteArrayStreamResult extends StreamResult {

    private ByteArrayOutputStream os;

    /** Constructs a new ByteArrayStreamResult */
    public ByteArrayStreamResult() {
        os = new ByteArrayOutputStream();
        setOutputStream(os);
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
