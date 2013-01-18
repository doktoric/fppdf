package com.acme.fppdf.bundle.content;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.acme.fppdf.bundle.content.ExternalFileContentProvider;

public class ExternalFileContentProviderTest {
    private static final String DOT = ".";
    private static final String SLASH = "/";
    
    ExternalFileContentProvider target;

    @Before
    public void setUp() {
        target = new ExternalFileContentProvider();
    }

    @After
    public void tearDown() {
        target = null;
    }

    @Test
    public void testFetchContent() throws Exception {
        final byte[] expected = "TEST".getBytes();
        
        File tempFile = File.createTempFile("tmp.", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream os = new FileOutputStream(tempFile)) {
            os.write(expected);
        }
        
        assertArrayEquals(expected, target.fetchContent(tempFile.getAbsolutePath(), null));
        assertArrayEquals(expected, target.fetchContent(tempFile.getAbsolutePath(), DOT));
        
        assertArrayEquals(expected, target.fetchContent(tempFile.toURI().toURL().toString(), null));
        assertArrayEquals(expected, target.fetchContent(tempFile.toURI().toURL().toString(), DOT));

        assertArrayEquals(expected, target.fetchContent(tempFile.getName(), tempFile.getParent()));
        assertNull(target.fetchContent(tempFile.getName(), SLASH));
    }

    @Test(expected = NullPointerException.class)
    public void testIsRelativeWithNull() {
        target.isRelative(null);
    }

    @Test
    public void testIsRelative() {
        assertTrue(target.isRelative(DOT));
        assertTrue(target.isRelative("./something1"));
        assertTrue(target.isRelative(String.format("..%ssomething1", File.separator)));
        
        assertTrue(target.isRelative(".."));
        assertTrue(target.isRelative("../something2"));
        assertTrue(target.isRelative(String.format("..%ssomething2", File.separator)));

        assertTrue(target.isRelative("..."));
        assertTrue(target.isRelative(".../something3"));
        assertTrue(target.isRelative(String.format("...%ssomething3", File.separator)));
        
        assertTrue(target.isRelative(".something4"));
        assertTrue(target.isRelative("..something4"));
        assertTrue(target.isRelative(String.format("...%ssomething4", File.separator)));
        
        assertTrue(target.isRelative("something5"));
        assertTrue(target.isRelative("something5/something5"));
        assertTrue(target.isRelative(String.format("something5%ssomething5", File.separator)));
    }
    
    @Test
    public void testIsNotRelative() {
        assertFalse(target.isRelative(SLASH));
        assertFalse(target.isRelative(File.separator));
        
        assertFalse(target.isRelative("C:/"));
        assertFalse(target.isRelative("C:/something"));
        
        assertFalse(target.isRelative("file:/something"));
        assertFalse(target.isRelative("file://something"));
        assertFalse(target.isRelative("file:///something"));
        assertFalse(target.isRelative("file:/C:/something"));
        assertFalse(target.isRelative("file://C:/something"));
        assertFalse(target.isRelative("file:///C:/something"));
    }
}
