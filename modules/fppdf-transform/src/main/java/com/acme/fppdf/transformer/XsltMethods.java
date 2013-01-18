package com.acme.fppdf.transformer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * Utility methods for XSLT transforms.
 * 
 * Can be used with a namespace like xmlns:java="http://acme.com/java/com.acme.fppdf.transformer.XsltMethods"
 * 
 * @author Gergely_Nagy1
 * 
 */
public final class XsltMethods {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy h:mm a");

    private XsltMethods() {
    }

    /**
     * URL-encode
     * 
     * @param url
     *            url to encode
     * @return url encoded url
     * @throws UnsupportedEncodingException
     *             i hate checkstyle rules
     */
    public static String urlencode(String url) throws UnsupportedEncodingException {
        // convert back encoded / marks
        return URLEncoder.encode(url, "UTF-8").replace("%2F", "/");
    }

    /**
     * Format timestamp
     * 
     * @param timestamp
     *            timestamp
     * @return timestamp formatted as string
     */
    public static String formatTimestamp(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    /**
     * the paramter value is valid href or not
     * 
     * @param href
     *            reference in xml or mm file
     * @return islocalimage file or not
     */
    public static boolean isImage(String href) {

        boolean retValue = false;
        if (StringUtils.hasText(href)) {
            if (!href.toLowerCase().startsWith("http:") && isValidImageExtension(href)) {
                retValue = true;
            }
        } else {
            retValue = false;
        }
        return retValue;
    }

    /**
     * the paramter value is valid href or not
     * @param href reference in xml or mm file
     * @return is valid file or not
     */
    private static boolean isValidImageExtension(String href) {
        return href.toLowerCase().endsWith(".png") || href.toLowerCase().endsWith(".bmp")
                || href.toLowerCase().endsWith(".gif") || href.toLowerCase().endsWith(".jpg")
                || href.toLowerCase().endsWith(".jpeg") || href.toLowerCase().endsWith(".ico");
    }
}
