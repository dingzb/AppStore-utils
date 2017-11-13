package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.core.app.App;
import com.tendyron.routewifi.core.app.SubVersion;

import java.util.List;

/**
 * Created by Neo on 2017/1/9.
 */
public interface MonitorDao {
    void saveApp(App app);

    void saveSubVersion(int appId, SubVersion sub);

    void saveSubVersion(int appId, List<SubVersion> subs);

    App get(int id);

    App get(String name, String version);

    List<SubVersion> getSubversions(String[] signatures);

    boolean appExist(App app);

}
