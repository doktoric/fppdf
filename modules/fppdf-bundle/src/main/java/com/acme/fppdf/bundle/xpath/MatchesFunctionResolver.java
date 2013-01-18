package com.acme.fppdf.bundle.xpath;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;

/**
 * Function resolver for the custom <code>boolean {fppdf}matches(string target, string regexp)</code> function
 */
public class MatchesFunctionResolver implements XPathFunctionResolver {
    /* (non-Javadoc)
     * @see javax.xml.xpath.XPathFunctionResolver#resolveFunction(javax.xml.namespace.QName, int)
     */
    @Override
    public XPathFunction resolveFunction(QName functionName, int arity) {
        XPathFunction result = null;
        if (MatchesFunction.URI.equals(functionName.getNamespaceURI())
                && MatchesFunction.NAME.equals(functionName.getLocalPart())
                && (arity == MatchesFunction.ARITY)) {
            result = new MatchesFunction();
        }
        return result;
    }
}
