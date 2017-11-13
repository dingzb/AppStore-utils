package com.tendyron.routewifi.appmanager.web.model;

/**
 * Created by Neo on 2017/2/14.
 */
public class Filter extends Base {
    private String content;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
