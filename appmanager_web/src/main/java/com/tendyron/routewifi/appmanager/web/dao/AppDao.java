package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.appmanager.web.model.App;
import com.tendyron.routewifi.appmanager.web.model.Paging;
import com.tendyron.routewifi.appmanager.web.model.PagingQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
public interface AppDao extends BaseDao<App> {
    Paging<App> paging(App filter, PagingQuery pagingQuery) throws SQLException;

    boolean updateStatus(App app) throws SQLException;

    boolean existByInfoUrl(String infoUrl, int agency_id) throws SQLException;

    boolean toggleUpdated(int id) throws SQLException;

    List<App> list() throws SQLException;
}
