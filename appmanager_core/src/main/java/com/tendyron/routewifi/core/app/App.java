package com.tendyron.routewifi.core.app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Neo on 2017/1/4.
 * <p>
 * The entity of application.
 *
 * @author Neo
 */
public class App {

    private final int versionLength = 1024;

    private int id;
    private String name;
    private String version;
    private volatile LinkedList<SubVersion> subVersions = new LinkedList<>();
    private String infoUrl;

    private String agencyName;
    private String agencyId;

    private Set<String> signature = new HashSet<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public App(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public App() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public void addSubVersion(SubVersion subVersion) {

        if (signature.contains(subVersion.getSignature())) {
            return;
        } else {
            signature.add(subVersion.getSignature());
        }
        if (versionLength <= subVersions.size()) {
            subVersions.remove(0);
        }
        subVersions.add(subVersion);
    }

    public LinkedList<SubVersion> getSubVersions() {
        return subVersions;
    }

}

