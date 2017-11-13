package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.appmanager.web.model.Agency;
import com.tendyron.routewifi.appmanager.web.model.Filter;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
public interface FilterDao extends BaseDao<Filter> {
    /**
     * @param type 1: collect, 2: update, other: all
     * @return
     * @throws SQLException
     */
    List<Filter> list(int type) throws SQLException;
}
