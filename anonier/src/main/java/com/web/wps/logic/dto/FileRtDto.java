package com.web.wps.logic.dto;

import com.web.wps.util.Token;
import lombok.Data;

import java.util.Date;

@Data
public class FileRtDto {
    private String id;
    private String fileId;
    private String name;
    private int version;
    private int size;
    private String partnerKey;
    private String createName;
    private String updateName;
    private Date createTime;
    private Date updateTime;

    private Token token;
}