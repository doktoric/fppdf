package com.acme.fppdf.bundle.content;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Base for URL-based content providers
 */
public class URLContentProvider implements ExternalContentProvider {
    private static final int BUFFER_SIZE = 1024;

    /**
     * Read content at the given URL
     * 
     * @param url
     *            source URL
     * @return content as <code>byte[]</code>
     * @throws IOException
     *             if an error condition is raised during I/O the exception is passed through
     */
    protected byte[] readURLContent(URL url) throws IOException {
        byte[] result = null;
        
        try (InputStream is = url.openStream()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];

            int readCount;
            while ((readCount = is.read(buffer)) != -1) {
                baos.write(buffer, 0, readCount);
            }

            baos.close();
            result = baos.toByteArray();
        }
        
        return result;
    }

    @Override
    public byte[] fetchContent(String location, String locationBase) throws Exception {
        return readURLContent(new URL(location));
    }
}
