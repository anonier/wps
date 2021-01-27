package com.web.wps.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author gengchuang
 * @version v1.0
 * @PACKAGE com.web.wps.logic.entity
 * @date 2021/1/18/下午3:52
 **/
@Data
public class WpsPartner implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 接入者key
     */
    private String partnerKey;

    /**
     * 接入者名称
     */
    private String name;

    /**
     * 接入者密钥
     */
    private String partnerSecret;

    /**
     * 回调业务系统时，业务系统的url
     */
    private String apiUrl;

    /**
     * 回调时，鉴权参数
     */
    private String apiKey;

    /**
     * 回调时，鉴权参数
     */
    private String apiSecret;
    /**
     * 接入状态 0默认接入正常 1禁止接入
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
}