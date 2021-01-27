package com.web.wps.util.upload;

import lombok.Data;

@Data
public class ResFileDTO {

    private String fileUrl;
    private String fileName;
    private String ossFileName;
    private String fileType;
    private long fileSize;
    private String md5key;

    public ResFileDTO() {
        super();
    }

    public ResFileDTO(String fileUrl, String fileName, String ossFileName, String fileType, long fileSize, String md5key) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.ossFileName = ossFileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.md5key = md5key;
    }
}