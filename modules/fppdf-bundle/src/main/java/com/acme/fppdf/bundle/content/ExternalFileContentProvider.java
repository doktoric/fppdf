package com.acme.fppdf.bundle.content;

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * External content provider for content stored in file system
 *
 */
public class ExternalFileContentProvider extends URLContentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalFileContentProvider.class);

    private static final String SEPARATOR_SET_REGEX_FRAGMENT = "[/" + Pattern.quote(File.separator) + "]";
    private static final String DOT_ESCAPED_REGEX_FRAGMENT = Pattern.quote(".");
    private static final String START_MARK = "^";
    private static final String SLASH = "/";

    /*
     * (non-Javadoc)
     *
     * @see com.acme.fppdf.bundle.ExternalContentProvider#fetchContent(java.lang.String)
     */
    @Override
    public byte[] fetchContent(String location, String locationBase) {
        byte[] result = null;

        try {
            String absoluteLocation = (isRelative(location) ? locationBase + SLASH + location : location)
                    .replaceAll(SEPARATOR_SET_REGEX_FRAGMENT, SLASH);
            URL url = absoluteLocation.matches("^file:.*$") ? new URL(absoluteLocation) : new File(absoluteLocation).toURI().toURL();
            result = readURLContent(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(e.getMessage(), e);
        }

        return result;
    }

    boolean isRelative(String location) {
        boolean result = false;
        if (location.matches(START_MARK + DOT_ESCAPED_REGEX_FRAGMENT + "{1,2}" + SEPARATOR_SET_REGEX_FRAGMENT + "[^.].*$")) {
            result = true;
        } else if (!location.contains(":") && !location.matches(START_MARK + SEPARATOR_SET_REGEX_FRAGMENT + ".*$")) {
            result = true;
        }

        return result;
    }
}
