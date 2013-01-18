package com.acme.fppdf.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.acme.fppdf.domain.Conversion;
import com.acme.fppdf.domain.ConversionType;
import com.acme.fppdf.domain.OrientationType;
import com.acme.fppdf.transformer.Converter;

public class ConverterTest {

    public static final File TEST_MM_INPUT_DIR = new File("src/test/resources/mm");
    public static final File TEST_MM_TARGET_DIR = new File("target/test/pdf");
    public static final File TEST_MM_FILE_FREEPLANE = new File(TEST_MM_INPUT_DIR, "freeplaneApplications.mm");
    
    
    private final Converter converter = new Converter();
    
    @Test
    public void testConverter() throws IOException {
        convert(TEST_MM_FILE_FREEPLANE);
    }
    
    @Test
    public void testConvertWithOrientation() throws Exception {
        convert(new File(TEST_MM_INPUT_DIR, "orientation.mm"), OrientationType.LANDSCAPE);
    }
    
    @Test
    public void testConvertDocumentationWithOrientation() throws Exception {
        convert(new File(TEST_MM_INPUT_DIR, "documentation-orientation.mm"), OrientationType.LANDSCAPE);
    }

    @Test
    public void testIcons() throws IOException {
        convert(new File(TEST_MM_INPUT_DIR, "icons.mm"));
    }
    
    @Test
    public void testRichContent() throws IOException {
        convert(new File(TEST_MM_INPUT_DIR, "richcontent.mm"));
    }
    
    
    @Test
    public void testDocumentation() throws IOException {
        convert(new File(TEST_MM_INPUT_DIR, "documentation.mm"));
    }

    protected Conversion convert(File mmFile, OrientationType orientation) throws IOException {
        Assert.assertTrue(mmFile + " doesn't exist", mmFile.exists());
        
        Conversion conversion = Conversion.fromFile(mmFile, ConversionType.PDF, orientation);
        converter.convert(conversion);
        
        byte[] pdf = conversion.getToBytes();
        Assert.assertTrue("No pdf was generated from " + mmFile.getName(), pdf != null && pdf.length > 0);
        
        save(conversion, new File(TEST_MM_TARGET_DIR, mmFile.getName() + ".pdf"));
        return conversion;
    }
    
    protected Conversion convert(File mmFile) throws IOException {
        return convert(mmFile, OrientationType.PORTRAIT);
    }
    
    protected void save(Conversion conversion, File file) throws IOException {
        file.getParentFile().mkdirs();
        IOUtils.write(conversion.getToBytes(), new FileOutputStream(file));
    }
}
