package com.web.wps.logic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
//@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {

    public FileDTO() {
        super();
    }

    private String id;
    private String name;
    /**
     * 文件版本
     */
    private int version;
    /**
     * 文件大小
     */
    private int size;
    /**
     * 创建者
     */
    private String createName;
    /**
     * 修改者
     */
    private String updateName;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 修改时间
     */
    private long updateTime;
    /**
     * 下载地址
     */
    private String download_url;
    /**
     * 用户的账户
     */
    private String account;
}