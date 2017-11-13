package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.VersionDao;
import com.tendyron.routewifi.appmanager.web.model.App;
import com.tendyron.routewifi.appmanager.web.model.Paging;
import com.tendyron.routewifi.appmanager.web.model.PagingQuery;
import com.tendyron.routewifi.appmanager.web.model.Version;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/2/20.
 */
public class VersionDaoImpl extends SqliteBaseDao<Version> implements VersionDao {

//    String[] colors = new String[]{"#905a3d", "#87843b", "#224b8f", "#56452d", "#74905d", "#6950a1", "#c37e00", "#73b9a2", "#f173ac", "#cbc547", "#228fbd", "#72777b"};

    public VersionDaoImpl() {
        super("version");
    }

    @Override
    public boolean add(Version version) throws SQLException {
        String sql = "INSERT INTO version (model, friendly_model, ios_version, download_url, capture_time, signature, app_version) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, version.getModel());
        ps.setString(2, version.getFriendlyModel());
        ps.setString(3, version.getIosVersion());
        ps.setString(4, version.getDownloadUrl());
        ps.setString(5, getStr(version.getCaptureTime()));
        ps.setString(6, version.getSignature());
        ps.setString(7, version.getAppVersion());
        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public boolean edit(Version version) throws SQLException {
        throw new IllegalStateException("VersionDao not implement this method.");
    }

    @Override
    protected List<Version> assemble(ResultSet rs, AttachOperation<Version> attachOperation) throws SQLException {
        List<Version> versions = new ArrayList<>();
        while (rs.next()) {
            Version version = new Version();
            version.setId(rs.getInt("id"));
            version.setAppVersion(rs.getString("app_version"));
            version.setIosVersion(rs.getString("ios_version"));
            version.setModel(rs.getString("model"));
            version.setFriendlyModel(rs.getString("friendly_model"));
            version.setDownloadUrl(rs.getString("download_url"));
            version.setCaptureTime(getDate(rs.getString("capture_time")));
            version.setAppId(rs.getInt("app_id"));
            attachOperation.process(rs, version);
            versions.add(version);
        }
        return versions;
    }

    @Override
    public Paging<Version> paging(Version filter, PagingQuery pQuery) throws SQLException {
        String sqlPage1 = "SELECT version.*, app.name app_name from version " +
                "LEFT JOIN app on version.app_id = app.id ";
        String sqlPage2 = " ORDER BY " + humpToLine2(pQuery.getSort()) + " " + pQuery.getOrder() + " LIMIT ? OFFSET ? * (?-1)";
        String sqlTotal = "select count(*) from version";

        String sqlWhere = " where 1=1";

        if (filter.getAppId() != 0) {
            sqlWhere += " and version.app_id = ?";
        }
        if (filter.getAppVersion() != null && !filter.getAppVersion().isEmpty()) {
            sqlWhere += " and version.app_version = ?";
        }


        PreparedStatement psPage = conn.prepareStatement(sqlPage1 + sqlWhere + sqlPage2);
        PreparedStatement psTotal = conn.prepareStatement(sqlTotal + sqlWhere);

        int i = 0;
        if (filter.getAppId() != 0) {
            psPage.setInt(++i, filter.getAppId());
            psTotal.setInt(i, filter.getAppId());
        }
        if (filter.getAppVersion() != null && !filter.getAppVersion().isEmpty()) {
            psPage.setString(++i, filter.getAppVersion());
            psTotal.setString(i, filter.getAppVersion());
        }
        psPage.setInt(++i, pQuery.getRows());
        psPage.setInt(++i, pQuery.getRows());
        psPage.setInt(++i, pQuery.getPage());

        ResultSet rsRows = psPage.executeQuery();
        List<Version> rows = assemble(rsRows, (rs, version) -> {
            version.setAppName(rs.getString("app_name"));
            version.setFriendlyModel(rs.getString("friendly_model"));
        });
        ResultSet rsTotal = psTotal.executeQuery();
        int total = 0;
        while (rsTotal.next()) {
            total = rsTotal.getInt(1);
        }

        return new Paging<Version>(total, rows);
    }

    @Override
    public boolean add(int appId, String appVersion, List<Version> versions) throws SQLException {
        String sql = "INSERT INTO version (model, friendly_model, ios_version, download_url, capture_time, signature, app_version, app_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (Version version : versions) {
            ps.setString(1, version.getModel());
            ps.setString(2, version.getFriendlyModel());
            ps.setString(3, version.getIosVersion());
            ps.setString(4, version.getDownloadUrl());
            ps.setString(5, getStr(version.getCaptureTime()));
            ps.setString(6, version.getSignature());
            ps.setString(7, appVersion);
            ps.setInt(8, appId);
            ps.addBatch();
        }
        int[] counts = ps.executeBatch();
        int count = 0;
        for (int c : counts) {
            count += c;
        }
        return !(count < versions.size());
    }

    @Override
    public List<String> VersionsByAppId(int appId) throws SQLException {
        String sql = "SELECT DISTINCT (app_version) FROM version WHERE app_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, appId);
        ResultSet rs = ps.executeQuery();
        List<String> versions = new ArrayList<>();
        while (rs.next()) {
            versions.add(rs.getString("app_version"));
        }
        return versions;
    }

    @Override
    public Paging<Version> group(Version filter, PagingQuery pQuery, String patternStr) throws SQLException {
        Paging<Version> paging = paging(filter, pQuery);
        List<Version> rows = paging.getRows();
        for (Version v : rows) {
            Integer hash = getHash(v.getDownloadUrl(), patternStr);
            if (hash != null) {
                v.setGroup(hash);
            }
        }
        rows.sort(Comparator.comparing(Version::getGroup));
        return paging;
    }

    private Integer getHash(String url, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
//        Pattern pattern = Pattern.compile("apple-assets-us-std-000001[^\\s]*ipa(?=\\?)");
        Matcher matcher = pattern.matcher(url);
        int hash;
        if (matcher.find()) {
            return matcher.group().hashCode();
        }
        return null;
    }

    @Override
    public List<String> listUrl(Version filter, String patternStr) throws SQLException {
        String sql = "SELECT download_url FROM version WHERE app_id = ? AND app_version = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, filter.getAppId());
        ps.setString(2, filter.getAppVersion());
        ResultSet rs = ps.executeQuery();
        Map<Integer, String> urlMap = new HashMap<>();
        while (rs.next()) {
            String url = rs.getString("download_url");
            urlMap.put(getHash(url, patternStr), url);
        }
        return new ArrayList<>(urlMap.values());
    }
}
