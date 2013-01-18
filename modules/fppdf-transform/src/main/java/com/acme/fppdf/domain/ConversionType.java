package com.acme.fppdf.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Enum for the conversion type. This decides the type of the return file.
 * @author Gergo Nagy1
 */
@Entity
public enum ConversionType {
    TEXT("text/plain"),
    HTML("text/html"),
    PDF("application/pdf"),
    MM("application/mm"),
    ZIP("application/zip");

    @Id
    @GeneratedValue
    private long id;
    
    private final String mimeType;

    ConversionType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}