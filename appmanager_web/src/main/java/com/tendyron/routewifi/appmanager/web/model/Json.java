package com.tendyron.routewifi.appmanager.web.model;

/**
 * Created by Neo on 2017/1/9.
 */
public class Json {
    private boolean success;
    private Object data;
    private String message;

    public Json(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public Json(boolean success, String messagePre) {
        this.success = success;
        this.message = success ? "成功" : "失败" + ": " + messagePre;
    }

    public Json(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
