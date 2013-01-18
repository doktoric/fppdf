package com.acme.fppdf.transformer;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.acme.fppdf.domain.Conversion;
import com.acme.fppdf.domain.ConversionType;
import com.acme.fppdf.transformer.ConversionException;
import com.acme.fppdf.transformer.Converter;

/**
 * Tests the Converter for invalid inputs
 * 
 * @author Gergely_Nagy1
 *
 */
public class InvalidConversionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    private final Converter converter = new Converter();

    @Test(expected = IllegalArgumentException.class)
    public void testConversionNull() {
        converter.convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConversionNullFromBytes() throws IOException {
        Conversion conversion = Conversion.fromFile(ConverterTest.TEST_MM_FILE_FREEPLANE, ConversionType.PDF);
        conversion.setFromBytes(null);
        converter.convert(conversion);
    }    

    @Test(expected = IllegalArgumentException.class)
    public void testConversionNullToType() throws IOException {
        Conversion conversion = Conversion.fromFile(ConverterTest.TEST_MM_FILE_FREEPLANE, ConversionType.PDF);
        conversion.setToType(null);
        converter.convert(conversion);
    }    

    /**
     * Test converter for invalid .mm file
     * @throws IOException 
     */
    @Test
    public void testInvalidMindMapInput() throws IOException {
        
        exception.expect(ConversionException.class);
        exception.expectMessage("Error while transforming mm to fo for " + ConverterTest.TEST_MM_FILE_FREEPLANE.getName());
        
        Conversion conversion = Conversion.fromFile(ConverterTest.TEST_MM_FILE_FREEPLANE, ConversionType.PDF);
        conversion.setFromBytes("notxml".getBytes());

        converter.convert(conversion);
    }

}
