package com.acme.fppdf.bundle.xpath;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

/**
 * Custom XPath <code>boolean {fppdf}matches(string target, string regexp))</code> function
 */
public class MatchesFunction implements XPathFunction {
    public static final String URI = "fppdf";
    public static final String NAME = "matches";
    public static final int ARITY = 2;
    public static final int ARG_POS_TARGET = 0;
    public static final int ARG_POS_REGEXP = 1;

    @SuppressWarnings("rawtypes")
    @Override
    public Object evaluate(List args) throws XPathFunctionException {
        if (args == null || args.size() != ARITY) {
            throw new XPathFunctionException(String.format("signature: boolean {%s}%s(string target, string regexp)", URI, NAME));
        }

        try {
            return Boolean.valueOf(((String) args.get(ARG_POS_TARGET)).matches((String) args.get(ARG_POS_REGEXP)));
        } catch (Exception e) {
            throw new XPathFunctionException(e);
        }
    }
}
