package com.acme.fppdf.bundle.xpath;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.xml.xpath.XPathFunctionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.acme.fppdf.bundle.xpath.MatchesFunction;

public class MatchesFunctionTest {
    MatchesFunction target;

    @Before
    public void setup() {
        target = new MatchesFunction();
    }

    @After
    public void tearDown() {
        target = null;
    }

    @Test(expected = XPathFunctionException.class)
    public void testNullArg() throws Exception {
        target.evaluate(null);
    }

    @Test(expected = XPathFunctionException.class)
    public void testNotEnoughArgs() throws Exception {
        target.evaluate(Arrays.asList(new String[] {"arg0"}));
    }

    @Test(expected = XPathFunctionException.class)
    public void testTooManyArgs() throws Exception {
        target.evaluate(Arrays.asList(new String[] {"arg1", "arg2", "arg3"}));
    }

    @Test
    public void testAll() throws Exception {
        assertTrue((Boolean) target.evaluate(Arrays.asList(new String[] {"target1", "^target1$"})));
        assertFalse((Boolean) target.evaluate(Arrays.asList(new String[] {"target2", "^$"})));
    }
}
