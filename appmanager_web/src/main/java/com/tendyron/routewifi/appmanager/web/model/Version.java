package com.tendyron.routewifi.appmanager.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Neo on 2017/1/8.
 */
public class Version extends Base implements Cloneable {

    private String model;

    private String friendlyModel;

    private String iosVersion;

    private String appVersion;

    private String downloadUrl;

    private String signature; //该记录的hash值，用于记录的判重

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date captureTime;

    private int appId;

    private String appName;

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    private Integer group;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFriendlyModel() {
        return friendlyModel;
    }

    public void setFriendlyModel(String friendlyModel) {
        this.friendlyModel = friendlyModel;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Date captureTime) {
        this.captureTime = captureTime;
    }

    public String getVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "Version{" +
                "model='" + model + '\'' +
                ", friendlyModel='" + friendlyModel + '\'' +
                ", iosVersion='" + iosVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", signature='" + signature + '\'' +
                ", captureTime=" + captureTime +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
