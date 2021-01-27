package com.web.wps.oauth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ActionResult implements Serializable {
    private static final long serialVersionUID = 1110507991796070037L;
    public static String SUCCESS = "success";
    public static String WARN = "warn";
    public static String ERROR = "error";
    private Map<String, Object> contents = new HashMap();
    private String type;
    private String title;
    private int errorNo;
    private Object data;

    public ActionResult() {
        this.type = SUCCESS;
    }

    public ActionResult(String title) {
        this.type = SUCCESS;
        this.title = title;
    }

    public ActionResult(String title, String type) {
        this.type = SUCCESS;
        this.type = type;
        this.title = title;
    }

    public ActionResult addContent(String key, Object obj) {
        this.contents.put(key, obj);
        return this;
    }

    public static ActionResult create(Object data) {
        ActionResult msg = new ActionResult();
        msg.setData(data);
        return msg;
    }

    public static ActionResult success() {
        return new ActionResult();
    }

    public int getErrorNo() {
        return this.errorNo;
    }

    public void setErrorNo(int errorNo) {
        this.errorNo = errorNo;
    }

    public Map<String, Object> getContents() {
        return this.contents;
    }

    public void setContents(Map<String, Object> contents) {
        this.contents = contents;
    }

    public Object getContent(String key) {
        return this.contents.get(key);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return "ActionResult [contents=" + this.contents + ", type=" + this.type + ", title=" + this.title + ", errorNo=" + this.errorNo + ", data=" + this.data + "]";
    }
}