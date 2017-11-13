package com.tendyron.routewifi.appmanager.web.model;

/**
 * Created by Neo on 2017/2/6.
 */
public class PagingQuery {
    private Integer page = 1;
    private Integer rows = Integer.MAX_VALUE;
    private String order = "asc";
    private String sort = "id";

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
            this.order = order.toUpperCase();
        } else {
            throw new IllegalArgumentException("Only 'asc' or 'desc' is allowed.");
        }
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
