package com.tendyron.routewifi.appmanager.web.dao;

import com.tendyron.routewifi.appmanager.web.model.Phone;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Neo on 2017/2/23.
 */
public interface PhoneDao extends BaseDao<Phone> {
    Map<String, String> map() throws SQLException;
}
