package com.web.wps.logic.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FileNewDto {

    /**
     * 文件id
     */
    @NotBlank(message = "fileid不能为空")
    private String fileId;
    /**
     * 文件名称
     */
    @NotBlank(message = "fileName不能为空")
    private String fileName;
    /**
     * 文件大小
     */
    @NotNull(message = "size不能为空")
    private int size;
    /**
     * 下载地址
     */
    @NotBlank(message = "downloadUrl不能为空")
    private String downloadUrl;
    /**
     * 用户的账户
     */
    @NotBlank(message = "account不能为空")
    private String account;
    /**
     * 用户用户名
     */
    @NotBlank(message = "userName不能为空")
    private String userName;
    /**
     * 接入者的唯一标识
     */
    @NotBlank(message = "partnerKey不能为空")
    private String partnerKey;
}
