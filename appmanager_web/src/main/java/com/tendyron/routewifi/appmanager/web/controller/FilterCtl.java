package com.tendyron.routewifi.appmanager.web.controller;

import com.tendyron.routewifi.appmanager.web.dao.FilterDao;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.FilterDaoImpl;
import com.tendyron.routewifi.appmanager.web.model.Filter;
import com.tendyron.routewifi.appmanager.web.model.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Neo on 2017/2/14.
 */

@Controller("filter")
@RequestMapping("management/filter")
public class FilterCtl {

    private FilterDao filterDao = new FilterDaoImpl();

    @RequestMapping("list")
    @ResponseBody
    public Json list(@RequestParam(name = "type", required = false, defaultValue = "0") int type) {
        try {
            return new Json(true, filterDao.list(type));
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("add")
    @ResponseBody
    public Json add(Filter filter) {
        try {
            return new Json(true, filterDao.add(filter));
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("edit")
    @ResponseBody
    public Json edit(Filter filter) {
        try {
            return new Json(true, filterDao.edit(filter));
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("del")
    @ResponseBody
    public Json add(@RequestParam("id[]") int[] ids) {
        try {
            return new Json(true, filterDao.del(ids));
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }
}
