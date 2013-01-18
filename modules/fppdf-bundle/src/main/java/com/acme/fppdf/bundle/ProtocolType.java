package com.acme.fppdf.bundle;

/**
 * Protocol type prefixes types recognized by the bundle converter
 *
 */
public enum ProtocolType {
    FILE("file:"),
    HTTP("http:"),
    HTTPS("https:");

    private final String prefix;

    private ProtocolType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return super.toString() + "(\"" + prefix + "\")";
    }
}
