package com.web.wps.logic.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FileModifyDto {

    /**
     * 文件id
     */
    @NotBlank(message = "fileId不能为空")
    private String fileId;
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
}
