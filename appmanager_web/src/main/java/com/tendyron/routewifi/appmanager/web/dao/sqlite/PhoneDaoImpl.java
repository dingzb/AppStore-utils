package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.PhoneDao;
import com.tendyron.routewifi.appmanager.web.model.Base;
import com.tendyron.routewifi.appmanager.web.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Neo on 2017/2/23.
 */
public class PhoneDaoImpl extends SqliteBaseDao<Phone> implements PhoneDao {

    public PhoneDaoImpl() {
        super("phone");
    }

    @Override
    public Map<String, String> map() throws SQLException {
        String sql = "select * from phone";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        List<Phone> phones = assemble(rs);
        Map<String, String> map = new HashMap<>();
        phones.forEach(phone -> map.put(phone.getModel(), phone.getName()));
        return phones.stream().collect(Collectors.toMap(Phone::getModel, Phone::getName));
    }

    @Override
    public boolean add(Phone phone) throws SQLException {
        return false;
    }

    @Override
    public boolean edit(Phone phone) throws SQLException {
        return false;
    }

    @Override
    protected List<Phone> assemble(ResultSet rs, AttachOperation<Phone> attachOperation) throws SQLException {
        List<Phone> phones = new ArrayList<>();
        while (rs.next()) {
            Phone phone = new Phone();
            phone.setModel(rs.getString("model"));
            phone.setName(rs.getString("name"));
            attachOperation.process(rs, phone);
            phones.add(phone);
        }
        return phones;
    }
}
