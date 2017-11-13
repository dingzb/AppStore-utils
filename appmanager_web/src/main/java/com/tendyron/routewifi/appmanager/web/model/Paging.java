package com.tendyron.routewifi.appmanager.web.model;

import java.util.List;

/**
 * Created by Neo on 2017/2/6.
 */
public class Paging<T extends Base> {
    private int total;
    private List<T> rows;

    public Paging() {
    }

    public Paging(int total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
