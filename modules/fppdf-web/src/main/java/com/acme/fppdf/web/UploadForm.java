package com.acme.fppdf.web;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.acme.fppdf.domain.ConversionType;
import com.acme.fppdf.domain.OrientationType;

/**
 * DTO backing the upload form.
 * 
 * @author Gergely_Nagy1
 * 
 */
public class UploadForm {

    @NotNull
    private ConversionType toType;

    @ValidMultipartFile(required = true)
    private CommonsMultipartFile file;

    @NotNull
    private OrientationType orientationType = OrientationType.PORTRAIT;

    @NotNull
    private Float sizeFactor;

    public ConversionType getToType() {
        return toType;
    }

    public void setToType(ConversionType toType) {
        this.toType = toType;
    }

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;

    }

    public Float getSizeFactor() {
        return sizeFactor;
    }

    public void setSizeFactor(Float sizeFactor) {
        this.sizeFactor = sizeFactor;
    }

    public OrientationType getOrientationType() {
        return orientationType;
    }

    public void setOrientationType(OrientationType orientationType) {
        this.orientationType = orientationType;
    }

}
