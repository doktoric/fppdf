package com.acme.fppdf.bundle;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.acme.fppdf.bundle.ProtocolType;

public class ProtocolTypeTest {

    @Test
    public void test() {
        assertEquals("FILE(\"file:\")", ProtocolType.FILE.toString());
        assertEquals("HTTP(\"http:\")", ProtocolType.HTTP.toString());
        assertEquals("HTTPS(\"https:\")", ProtocolType.HTTPS.toString());

        Set<ProtocolType> values = new HashSet<>();
        values.add(ProtocolType.FILE);
        values.add(ProtocolType.HTTP);
        values.add(ProtocolType.HTTPS);
        assertEquals(values, new HashSet<ProtocolType>(Arrays.asList(ProtocolType.values())));
    }

}
