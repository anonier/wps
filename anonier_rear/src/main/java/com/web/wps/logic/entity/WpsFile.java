package com.web.wps.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class WpsFile implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 文件表中主键id
     */
    private String fileId;

    /**
     * 接入者KEY
     */
    private String partnerKey;

    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件版本号
     */
    private int version;
    /**
     * 文件大小
     */
    private int size;
    /**
     * 文件oss临时下载预览地址
     */
    private String downloadUrl;

    /**
     * 创建文件姓名
     */
    private String createName;
    /**
     * 修改文件人名称
     */
    private String updateName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}