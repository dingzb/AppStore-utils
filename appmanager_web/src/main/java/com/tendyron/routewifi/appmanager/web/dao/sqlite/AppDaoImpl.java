package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.AppDao;
import com.tendyron.routewifi.appmanager.web.model.App;
import com.tendyron.routewifi.appmanager.web.model.Paging;
import com.tendyron.routewifi.appmanager.web.model.PagingQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
public class AppDaoImpl extends SqliteBaseDao<App> implements AppDao {

    public AppDaoImpl() {
        super("app");
    }

    @Override
    public boolean add(App app) throws SQLException {
        assertCheck(app);
        if (existByInfoUrl(app.getName(), app.getAgencyId())) {
            throw new IllegalStateException("Record with info url: [" + app.getInfoUrl() + "] is exist.");
        }
        String sql = "INSERT INTO app (name, version, info_url, agency_id, create_time) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, app.getName());
        ps.setString(2, app.getVersion());
        ps.setString(3, app.getInfoUrl());
        ps.setInt(4, app.getAgencyId());
        ps.setString(5, getStr(new Date()));
        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public boolean edit(App app) throws SQLException {
        if (app.getId() == null || !existById(app.getId())) {
            throw new IllegalStateException("App with id: [" + app.getId() + "] is not exist.");
        }

        assertCheck(app);
        String sql = "UPDATE app SET name = ?, version = ?, info_url = ?, agency_id = ?, update_time = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, app.getName());
        ps.setString(2, app.getVersion());
        ps.setString(3, app.getInfoUrl());
        ps.setInt(4, app.getAgencyId());
        ps.setString(5, getStr(new Date()));
        ps.setInt(6, app.getId());
        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public boolean updateStatus(App app) throws SQLException {
        assertCheck(app);
        String sql = "UPDATE app SET name = ?, version = ?, updated = ?, icon_url = ?, version_time = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, app.getName());
        ps.setString(2, app.getVersion());
        ps.setInt(3, getInt(app.isUpdated()));
        ps.setString(4, app.getIconUrl());
        ps.setString(5, getStr(app.getVersionTime()));
        ps.setInt(6, app.getId());
        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public Paging<App> paging(App filter, PagingQuery pQuery) throws SQLException {
        String sqlPage1 = "SELECT app.*, agency.name agency_name from app LEFT JOIN agency on app.agency_id = agency.id";
        String sqlPage2 = " ORDER BY " + humpToLine2(pQuery.getSort()) + " " + pQuery.getOrder() + " LIMIT ? OFFSET ? * (?-1)";
        String sqlTotal = "select count(*) from app";

        String sqlWhere = " where 1=1";

        if (filter.getName() != null) {
            sqlWhere += " app.name like ?";
        }
        if (filter.getAgencyId() != null) {
            sqlWhere += " and app.agency_id = ?";
        }


        PreparedStatement psPage = conn.prepareStatement(sqlPage1 + sqlWhere + sqlPage2);
        PreparedStatement psTotal = conn.prepareStatement(sqlTotal + sqlWhere);

        int i = 0;
        if (filter.getName() != null) {
            psPage.setString(++i, "%" + filter.getName() + "%");
            psTotal.setString(i, "%" + filter.getName() + "%");
        }
        if (filter.getAgencyId() != null) {
            psPage.setInt(++i, filter.getAgencyId());
            psTotal.setInt(i, filter.getAgencyId());
        }
        psPage.setInt(++i, pQuery.getRows());
        psPage.setInt(++i, pQuery.getRows());
        psPage.setInt(++i, pQuery.getPage());

        ResultSet rsRows = psPage.executeQuery();
        List<App> rows = assemble(rsRows, (rs, app) -> app.setAgencyName(rs.getString("agency_name")));
        ResultSet rsTotal = psTotal.executeQuery();
        int total = 0;
        while (rsTotal.next()) {
            total = rsTotal.getInt(1);
        }

        return new Paging<App>(total, rows);
    }

    private void assertCheck(App app) throws SQLException {
        if (app.getInfoUrl() == null || app.getInfoUrl().isEmpty()) {
            throw new IllegalArgumentException("null or empty info url.");
        }
        if (app.getAgencyId() == null) {
            throw new IllegalArgumentException("null agency.");
        }
    }

    @Override
    public boolean existByInfoUrl(String infoUrl, int agency_id) throws SQLException {
        String sql = "SELECT count(*) FROM app WHERE info_url = ? AND agency_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, infoUrl);
        ps.setInt(2, agency_id);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count = rs.getInt(1);
        }
        return count > 0;
    }

    @Override
    public boolean toggleUpdated(int id) throws SQLException {
        App app = get(id);
        String sql = "UPDATE app SET updated = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, getInt(!app.isUpdated()));
        ps.setInt(2, id);
        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public List<App> list() throws SQLException {
        String sql = "SELECT * FROM app ORDER BY agency_id ASC, name ASC";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        return assemble(rs);
    }

    @Override
    protected List<App> assemble(ResultSet rs, AttachOperation<App> attachOperation) throws SQLException {
        List<App> apps = new ArrayList<>();
        while (rs.next()) {
            App app = new App();
            app.setId(rs.getInt("id"));
            app.setName(rs.getString("name"));
            app.setVersion(rs.getString("version"));
            app.setAgencyId(rs.getInt("agency_id"));
            app.setInfoUrl(rs.getString("info_url"));
            app.setIconUrl(rs.getString("icon_url"));
            app.setCreateTime(getDate(rs.getString("create_time")));
            app.setUpdateTime(getDate(rs.getString("update_time")));
            app.setVersionTime(getDate(rs.getString("version_time")));
            app.setUpdated(getBoolean(rs.getInt("updated")));
            attachOperation.process(rs, app);
            apps.add(app);
        }
        return apps;
    }
}
