package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.appmanager.web.model.Paging;
import com.tendyron.routewifi.appmanager.web.model.PagingQuery;
import com.tendyron.routewifi.appmanager.web.model.Version;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Neo on 2017/2/20.
 */
public interface VersionDao extends BaseDao<Version> {
    Paging<Version> paging(Version filter, PagingQuery pQuery) throws SQLException;

    boolean add(int appId, String appVersion, List<Version> versions) throws SQLException;

    List<String> VersionsByAppId(int appId) throws SQLException;

    Paging<Version> group(Version filter, PagingQuery pQuery, String patterStr) throws SQLException;

    public List<String> listUrl(Version filter, String patternStr)  throws SQLException;

}
