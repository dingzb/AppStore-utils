package com.tendyron.routewifi.core.app;

import java.util.Date;

/**
 * Created by Neo on 2017/1/8.
 */
public class SubVersion {

    private String model;

    private String friendlyModel;

    private String iosVersion;

    private String downloadUrl;

    private String signature;

    private Date captureTime;

    private int appId;

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

    @Override
    public String toString() {
        return "SubVersion{" +
                "model='" + model + '\'' +
                ", friendlyModel='" + friendlyModel + '\'' +
                ", iosVersion='" + iosVersion + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
