package com.web.wps.oauth;

import org.springframework.stereotype.Service;

@Service
public class CmacAgentCache extends AbstractCacheSupport<CmacAgent> {
    private static final String CACHE_ID = CmacAgentCache.class.getSimpleName();

    public CmacAgentCache() {
    }

    public String getCacheId() {
        return CACHE_ID;
    }
}