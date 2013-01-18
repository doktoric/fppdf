package com.acme.fppdf.bundle.content;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for content providers
 */
public final class ContentProviderManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentProviderManager.class);

    private final Map<String, ExternalContentProvider> providers = new HashMap<>();
    private ExternalContentProvider defaultProvider;

    /**
     * Return a provider instance registered for the given prefix
     *
     * @param prefix the URI prefix for which to get a provider
     * @return the registered provider instance, the default instance or null
     */
    public ExternalContentProvider getProviderForPrefix(String prefix) {
        ExternalContentProvider result = providers.get(prefix);
        if (result == null) {
            result = defaultProvider;
            LOGGER.debug(String.format("Prefix '%s' was not registered, using default provider", prefix));
        }
        if (result == null) {
            LOGGER.debug("No default provider is registered");
        }
        return result;
    }

    /**
     * Register a provider instance for prefixes not matching any registered prefixes
     *
     * @param prefix URI prefix to be used for matching
     * @param provider the instance to be registered for matching URIs
     */
    public void register(String prefix, ExternalContentProvider provider) {
        if (providers.containsKey(prefix)) {
            LOGGER.debug(String.format("Prefix '%s' was aready registered", prefix));
        }
        providers.put(prefix, provider);
        LOGGER.debug(String.format("Provider %s registered for prefix '%s'", provider, prefix));
    }

    /**
     * Register default provider instance for prefixes not matching any registered prefixes
     *
     * @param provider the instance to be registered
     */
    public void registerDefault(ExternalContentProvider provider) {
        if (defaultProvider != null) {
            LOGGER.debug("Default provider was aready registered");
        }
        defaultProvider = provider;
        LOGGER.debug(String.format("Provider %s registered as default provider", provider));
    }
}
