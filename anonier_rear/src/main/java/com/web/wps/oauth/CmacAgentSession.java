package com.web.wps.oauth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CmacAgentSession {
    private static final Logger log = LoggerFactory.getLogger(CmacAgentSession.class);
    private static final String COOKIE_KEY = "wps_sessionId";
    @Resource
    private CmacAgentCache cmacAgentCache;

    public CmacAgentSession() {
    }

    public void save(HttpServletRequest request, CmacAgent cmacAgent, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        this.cmacAgentCache.put(sessionId, cmacAgent);
    }

    public void save(HttpServletRequest request, CmacAgent cmacAgent, HttpServletResponse response, int expiryTime) {
        String sessionId = request.getSession().getId();
        this.cmacAgentCache.put(sessionId, cmacAgent, expiryTime);
    }

    public CmacAgent get(HttpServletRequest request) {
        String sessionId = request.getHeader("wps_sessionId");
        if (StringUtils.isBlank(sessionId)) {
            return null;
        } else {
            CmacAgent agent = (CmacAgent)this.cmacAgentCache.get(sessionId);
            if (null != agent) {
                this.cmacAgentCache.put(sessionId, agent);
            }

            return agent;
        }
    }

    public void remove(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        if (log.isDebugEnabled()) {
            log.debug("remove session the sessionId: [" + sessionId + "]");
        }

        this.cmacAgentCache.remove(sessionId);
    }
}