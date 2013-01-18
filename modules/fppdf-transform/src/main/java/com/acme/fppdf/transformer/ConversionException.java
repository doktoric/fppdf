package com.acme.fppdf.transformer;

/**
 * Runtime Exception thrown when conversion fails
 * 
 * @author Gergely_Nagy1
 *
 */
public class ConversionException extends RuntimeException {

    private static final long serialVersionUID = -5634830747471714974L;

    /**
     * Constructor
     * 
     * @param message exception message
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param message exception message
     * @param cause exception cause
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}