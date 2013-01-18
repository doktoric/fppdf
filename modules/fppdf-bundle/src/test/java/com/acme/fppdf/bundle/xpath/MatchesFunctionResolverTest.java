package com.acme.fppdf.bundle.xpath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.junit.Test;

import com.acme.fppdf.bundle.xpath.MatchesFunctionResolver;

public class MatchesFunctionResolverTest {
    final String fppdf = "fppdf";
    final String uri = fppdf;
    final String prefix = fppdf;

    final String name = "matches";

    final int arity = 2;

    final MatchesFunctionResolver target = new MatchesFunctionResolver();

    @Test(expected = NullPointerException.class)
    public void testException() {
        target.resolveFunction(null, arity);
    }

    @Test
    public void testAll() {

        assertNotNull(target.resolveFunction(new QName(uri, name), arity));

        // NOTE: prefix is ignored since a prefix without namespace declaration seems to be passed as the namespace URI
        assertNotNull(target.resolveFunction(new QName(uri, name, prefix), arity));
        assertNotNull(target.resolveFunction(new QName(uri, name, XMLConstants.DEFAULT_NS_PREFIX), arity));
        assertNotNull(target.resolveFunction(new QName(uri, name, "prefix1"), arity));

        // different arity
        assertNull(target.resolveFunction(new QName(uri, name, prefix), arity + 1));
        assertNull(target.resolveFunction(new QName(uri, name, XMLConstants.DEFAULT_NS_PREFIX), arity - 1));
        assertNull(target.resolveFunction(new QName(uri, name, "prefix2"), -1));

        // different function name
        assertNull(target.resolveFunction(new QName(""), arity));
        assertNull(target.resolveFunction(new QName(name), arity));
        assertNull(target.resolveFunction(new QName("1"), arity));
    }
}
