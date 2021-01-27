package com.web.wps.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class WpsLog implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String wpsFileId;
    private String name;
    private String userId;
    private String status;

    public WpsLog() {
        super();
    }

    public WpsLog(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }
}