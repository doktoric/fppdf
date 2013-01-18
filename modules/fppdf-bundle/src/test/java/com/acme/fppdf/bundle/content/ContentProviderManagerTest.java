package com.acme.fppdf.bundle.content;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.acme.fppdf.bundle.content.ContentProviderManager;
import com.acme.fppdf.bundle.content.ExternalContentProvider;

public class ContentProviderManagerTest {
    private static final String PREFIX_1 = "1:";
    private static final String PREFIX_2 = "2:";
    private static final String PREFIX_DEFAULT = "default:";
    
    ContentProviderManager target;
    ExternalContentProvider provider1;
    ExternalContentProvider provider2;
    ExternalContentProvider provider3;

    @Before
    public void setUp() {
        target = new ContentProviderManager();
        provider1 = new ExternalContentProvider() {
            @Override
            public byte[] fetchContent(String location, String locationBase) throws Exception {
                return null;
            }
        };
        provider2 = new ExternalContentProvider() {
            @Override
            public byte[] fetchContent(String location, String locationBase) throws Exception {
                return null;
            }
        };
        provider3 = new ExternalContentProvider() {
            @Override
            public byte[] fetchContent(String location, String locationBase) throws Exception {
                return null;
            }
        };
    }

    @After
    public void tearDown() {
        target = null;
        provider1 = null;
        provider2 = null;
        provider3 = null;
    }

    @Test
    public void testAll() {
        assertNull(target.getProviderForPrefix(null));
        assertNull(target.getProviderForPrefix(PREFIX_1));
        assertNull(target.getProviderForPrefix(PREFIX_2));
        assertNull(target.getProviderForPrefix(PREFIX_DEFAULT));
        
        target.register(PREFIX_1, provider1);
        assertNull(target.getProviderForPrefix(null));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_1));
        assertNull(target.getProviderForPrefix(PREFIX_2));
        assertNull(target.getProviderForPrefix(PREFIX_DEFAULT));
        
        target.register(PREFIX_2, provider2);
        assertNull(target.getProviderForPrefix(null));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_1));
        assertEquals(provider2, target.getProviderForPrefix(PREFIX_2));
        assertNull(target.getProviderForPrefix(PREFIX_DEFAULT));

        target.registerDefault(provider3);
        assertEquals(provider3, target.getProviderForPrefix(null));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_1));
        assertEquals(provider2, target.getProviderForPrefix(PREFIX_2));
        assertEquals(provider3, target.getProviderForPrefix(PREFIX_DEFAULT));

        target.register(PREFIX_2, provider1);
        assertEquals(provider3, target.getProviderForPrefix(null));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_1));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_2));
        assertEquals(provider3, target.getProviderForPrefix(PREFIX_DEFAULT));
        
        target.registerDefault(provider1);
        assertEquals(provider1, target.getProviderForPrefix(null));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_1));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_2));
        assertEquals(provider1, target.getProviderForPrefix(PREFIX_DEFAULT));
    }
}
