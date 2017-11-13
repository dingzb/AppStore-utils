package com.tendyron.routewifi.appmanager.web.dao.sqlite;

import com.tendyron.routewifi.appmanager.web.dao.BaseDao;
import com.tendyron.routewifi.appmanager.web.model.Base;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Neo on 2017/1/24.
 */
public abstract class SqliteBaseDao<T extends Base> implements BaseDao<T> {

    protected Connection conn = null;
    private String table;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected SqliteBaseDao(String table) {
        this.table = table;
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String connStr = prop.getProperty("db.url");
        try {
            conn = DriverManager.getConnection(connStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @Override
    public boolean existById(int id) throws SQLException {
        String sql = "SELECT count(*) FROM " + table + " WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count = rs.getInt(1);
        }
        return count > 0;
    }

    @Override
    public boolean del(int[] ids) throws SQLException {
        int len = ids.length;
        if (len < 1) {
            throw new IllegalArgumentException("ids can't empty.");
        }
        StringBuilder params = new StringBuilder("(");
        for (int i = 0; i < ids.length - 1; i++) {
            params.append("?, ");
        }
        params.append("?)");

        String sql = "DELETE FROM " + table + " WHERE id IN " + params;

        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < len; i++) {
            ps.setInt(i + 1, ids[i]);
        }

        int count = ps.executeUpdate();
        return count > 0;
    }

    @Override
    public T get(int id) throws SQLException {
        String sql = "select * from " + table + " where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return assemble(rs).get(0);
    }

    @Override
    public List<T> list(int[] ids) throws SQLException {
        int len = ids.length;
        if (len < 1) {
            throw new IllegalArgumentException("ids can't empty.");
        }
        StringBuilder params = new StringBuilder("(");
        for (int i = 0; i < ids.length - 1; i++) {
            params.append("?, ");
        }
        params.append("?)");
        String sql = "SELECT * FROM " + table + " WHERE id IN " + params;
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < len; i++) {
            ps.setInt(i + 1, ids[i]);
        }
        ResultSet rs = ps.executeQuery();
        return assemble(rs);
    }

    protected abstract List<T> assemble(ResultSet rs, AttachOperation<T> attachOperation) throws SQLException;

    protected List<T> assemble(ResultSet rs) throws SQLException {
        return assemble(rs, (rs1, t) -> {
        });
    }

    public interface AttachOperation<T> {
        public void process(ResultSet rs, T t) throws SQLException;
    }

    // ========== tools ==========
    String getStr(Date date) {
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    Date getDate(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    int getInt(boolean b) {
        return b ? 1 : 0;
    }

    boolean getBoolean(int i) {
        return i == 1;
    }
}
