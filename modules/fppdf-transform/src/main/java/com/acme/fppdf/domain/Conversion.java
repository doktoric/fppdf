package com.acme.fppdf.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Conversion class which contains information about conversion.
 * 
 * @author Gergely_Nagy1
 */
@Entity
@Table(name = "conversion")
public class Conversion {

    private static final int CONTENT_LENGTH = 10_000_000;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @NotNull
    private ConversionType toType;

    @DateTimeFormat(style = "SS")
    private Date creationDate;

    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = CONTENT_LENGTH)
    private byte[] fromBytes;
    

    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = CONTENT_LENGTH)
    private byte[] foBytes;



    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = CONTENT_LENGTH)
    private byte[] toBytes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(9) default 'PORTRAIT'")
    private OrientationType orientation;

    @NotNull
    private Float sizeFactor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConversionType getToType() {
        return toType;
    }

    public void setToType(ConversionType toType) {
        this.toType = toType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getFromBytes() {
        return fromBytes;
    }

    public void setFromBytes(byte[] fromBytes) {
        this.fromBytes = fromBytes;
    }

    
    public byte[] getFoBytes() {
        return foBytes;
    }

    public void setFoBytes(byte[] foBytes) {
        this.foBytes = foBytes;
    }
    
    public byte[] getToBytes() {
        return toBytes;
    }

    public void setToBytes(byte[] toBytes) {
        this.toBytes = toBytes;
    }

    public OrientationType getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationType orientation) {
        this.orientation = orientation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Float getSizeFactor() {
        return sizeFactor;
    }

    public void setSizeFactor(Float sizeFactor) {
        this.sizeFactor = sizeFactor;
    }

    /**
     * Factory method for creating conversion from file
     * 
     * @param file file to convert
     * @param conversionType type to convert to
     * @return Conversion object
     * @throws IOException if file not found or file cannot be read
     */
    public static Conversion fromFile(File file, ConversionType conversionType) throws IOException {
        return fromFile(file, conversionType, OrientationType.PORTRAIT);
    }

    /**
     * Factory method for creating conversion from file
     * 
     * @param file file to convert
     * @param conversionType type to convert to
     * @param orientationType the orientation of the printable document
     * @return Conversion object
     * @throws IOException if file not found or file cannot be read
     */
    public static Conversion fromFile(File file, ConversionType conversionType, OrientationType orientationType) throws IOException {
        return fromInputStream(file.getName(), new FileInputStream(file), conversionType, orientationType);
    }

    /**
     * Factory method for creating conversion from InputStream
     * 
     * @param name name of the input (e.g. file name)
     * @param is InputStream to read data from
     * @param conversionType type to convert to
     * @return Conversion object
     * @throws IOException if file not found or file cannot be read
     */
    public static Conversion fromInputStream(String name, InputStream is, ConversionType conversionType) throws IOException {
        return fromInputStream(name, is, conversionType, OrientationType.PORTRAIT);
    }

    /**
     * Factory method for creating conversion from InputStream
     * 
     * @param name name of the input (e.g. file name)
     * @param is InputStream to read data from
     * @param conversionType type to convert to
     * @param orientationType the orientation of the printable document
     * @return Conversion object
     * @throws IOException if file not found or file cannot be read
     */
    public static Conversion fromInputStream(String name, InputStream is, ConversionType conversionType, OrientationType orientationType)
        throws IOException {
        Conversion conversion = new Conversion();
        conversion.setName(name);
        conversion.setFromBytes(IOUtils.toByteArray(is));
        conversion.setToType(conversionType);
        conversion.setCreationDate(new Date());
        conversion.setOrientation(orientationType);
        conversion.setSizeFactor(1.0F);
        return conversion;
    }
}
