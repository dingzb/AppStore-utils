package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.FilterDao;
import com.tendyron.routewifi.appmanager.web.model.Filter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Neo on 2017/2/14.
 */
public class FilterDaoImpl extends SqliteBaseDao<Filter> implements FilterDao {
    public FilterDaoImpl() {
        super("filter");
    }

    @Override
    public boolean add(Filter filter) throws SQLException {
        String sql = "INSERT INTO filter (name, content, type, create_time, update_time) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, filter.getName());
        ps.setString(2, filter.getContent());
        ps.setInt(3, filter.getType());
        ps.setString(4, getStr(new Date()));
        ps.setString(5, getStr(new Date()));
        return ps.executeUpdate() > 0;
    }

    @Override
    public boolean edit(Filter filter) throws SQLException {
        String sql = "UPDATE filter SET name = ?, content = ?, type= ?, update_time = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, filter.getName());
        ps.setString(2, filter.getContent());
        ps.setInt(3, filter.getType());
        ps.setString(4, getStr(new Date()));
        ps.setInt(5, filter.getId());
        return ps.executeUpdate() > 0;
    }

    @Override
    public List<Filter> list(int type) throws SQLException {
        List<Filter> result = new ArrayList<>();
        String sql = "SELECT * FROM filter";
        switch (type) {
            case 1:
                sql += " where type = 1";
                break;
            case 2:
                sql += " where type = 2";
                break;
            case 3:
                sql += " where type = 3";
                break;
            default:
                break;
        }
        ResultSet rs = conn.createStatement().executeQuery(sql);
        return assemble(rs);
    }

    @Override
    protected List<Filter> assemble(ResultSet rs, AttachOperation<Filter> attachOperation) throws SQLException {
        List<Filter> filters = new ArrayList<>();
        while (rs.next()) {
            Filter filter = new Filter();
            filter.setId(rs.getInt("id"));
            filter.setName(rs.getString("name"));
            filter.setContent(rs.getString("content"));
            filter.setType(rs.getInt("type"));
            filter.setCreateTime(getDate(rs.getString("create_time")));
            filter.setUpdateTime(getDate(rs.getString("update_time")));
            attachOperation.process(rs, filter);
            filters.add(filter);
        }
        return filters;
    }
}
