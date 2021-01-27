package com.web.wps.oauth;

import lombok.Data;

import java.io.Serializable;

@Data
public class CmacAgent implements Serializable {
    private static final long serialVersionUID = -1830859706881331312L;
    private Long id;
    private String account;
    private String partnerKey;
    private String partnerSecret;
    private String apiUrl;
    private String status;
    private String sessionId;
    private String ip;

    public CmacAgent() {
    }
}