package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.AgencyDao;
import com.tendyron.routewifi.appmanager.web.model.Agency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
public class AgencyDaoImpl extends SqliteBaseDao<Agency> implements AgencyDao {

    public AgencyDaoImpl() {
        super("agency");
    }

    @Override
    public List<Agency> list() throws SQLException {
        String sql = "SELECT * FROM agency";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        return assemble(rs);
    }

    @Override
    public boolean add(Agency agency) throws SQLException {
        if (agency.getName() == null || agency.getName().isEmpty()) {
            throw new IllegalArgumentException("null or empty name.");
        }
        if (existByName(agency.getName())) {
            throw new IllegalStateException("Record with name: [" + agency.getName() + "] is existByInfoUrl.");
        }

        String sql = "INSERT INTO agency (name, create_time, update_time) VALUES(?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, agency.getName());
        ps.setString(2, getStr(new Date()));
        ps.setString(3, getStr(new Date()));
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean edit(Agency agency) throws SQLException {
        String sql = "UPDATE agency SET name = ?, update_time = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, agency.getName());
        ps.setString(2, getStr(new Date()));
        ps.setInt(3, agency.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean existByName(String name) throws SQLException {
        String sql = "SELECT count(*) FROM agency WHERE name = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count = rs.getInt(1);
        }
        return count > 0;
    }

    @Override
    protected List<Agency> assemble(ResultSet rs, AttachOperation<Agency> attachOperation) throws SQLException {
        List<Agency> agencies = new ArrayList<>();
        while (rs.next()) {
            Agency agency = new Agency();
            agency.setId(rs.getInt("id"));
            agency.setName(rs.getString("name"));
            agency.setUpdateTime(getDate(rs.getString("update_time")));
            agency.setCreateTime(getDate(rs.getString("create_time")));
            attachOperation.process(rs, agency);
            agencies.add(agency);
        }
        return agencies;
    }
}
