package com.acme.fppdf.domain;

/**
 * 
 *  Enum type for storing orientation of the document.
 * 
 */
public enum OrientationType {

    PORTRAIT("Portrait"), LANDSCAPE("Landscape");

    private final String orientation;

    OrientationType(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }
    
}
