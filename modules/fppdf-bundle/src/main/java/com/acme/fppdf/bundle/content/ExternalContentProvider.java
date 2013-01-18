package com.acme.fppdf.bundle.content;

/**
 * External content provider interface
 *
 */
public interface ExternalContentProvider {
    /**
     * Fetch content stored at the given location
     * 
     * @param location the URI to fetch the content from
     * @param locationBase base to be used for relative <code>location</code>s
     * @return the fetched content
     * @throws Exception any exception raised during operation
     */
    byte[] fetchContent(String location, String locationBase) throws Exception;
}
