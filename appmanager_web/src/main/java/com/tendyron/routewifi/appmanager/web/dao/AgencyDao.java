package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.appmanager.web.model.Agency;
import com.tendyron.routewifi.appmanager.web.model.App;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
public interface AgencyDao extends BaseDao<Agency> {
    List<Agency> list() throws SQLException;

    boolean existByName(String name) throws SQLException;
}
