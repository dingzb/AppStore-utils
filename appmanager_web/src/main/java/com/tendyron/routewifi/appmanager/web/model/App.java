package com.tendyron.routewifi.appmanager.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Neo on 2017/2/6.
 */
public class App extends Base {

    private String version = "";
    private Integer agencyId;
    private String agencyName;
    private String infoUrl;
    private String iconUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date versionTime;
    private boolean updated;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Date getVersionTime() {
        return versionTime;
    }

    public void setVersionTime(Date versionTime) {
        this.versionTime = versionTime;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public void updateStatus(String name, String version, String iconUrl) {
        if (this.version != null && version != null && !this.version.equals(version)) {
            updated = false;
            this.version = version;
        }
        if (name != null && !name.isEmpty()) {
            setName(name);
        }
        if (iconUrl != null && !iconUrl.isEmpty()) {
            this.iconUrl = iconUrl;
        }
        this.versionTime = new Date();
    }
}
