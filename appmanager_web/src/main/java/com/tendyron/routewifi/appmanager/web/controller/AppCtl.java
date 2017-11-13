package com.tendyron.routewifi.appmanager.web.controller;

import com.tendyron.routewifi.appmanager.web.dao.AppDao;
import com.tendyron.routewifi.appmanager.web.dao.FilterDao;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.AppDaoImpl;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.FilterDaoImpl;
import com.tendyron.routewifi.appmanager.web.model.App;
import com.tendyron.routewifi.appmanager.web.model.Filter;
import com.tendyron.routewifi.appmanager.web.model.Json;
import com.tendyron.routewifi.appmanager.web.model.PagingQuery;
import com.tendyron.routewifi.core.app.update.HtmlUpdateChecker;
import com.tendyron.routewifi.core.app.update.RegexHtmlParser;
import com.tendyron.routewifi.core.app.update.UpdateChecker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Neo on 2017/1/24.
 */
@Controller("app")
@RequestMapping("management/app")
public class AppCtl {

    private AppDao appDao = new AppDaoImpl();
    private FilterDao filterDao = new FilterDaoImpl();

    @RequestMapping("paging")
    @ResponseBody
    public Json paging(App filter, PagingQuery pQuery) {
        try {
            return new Json(true, appDao.paging(filter, pQuery));
        } catch (Exception e) {
            e.printStackTrace();
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("add")
    @ResponseBody
    public Json add(App app) {
        try {
            return new Json(appDao.add(app), "添加");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("del")
    @ResponseBody
    public Json del(@RequestParam("id[]") int[] ids) {
        try {
            return new Json(appDao.del(ids), "删除");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("edit")
    @ResponseBody
    public Json edit(App app) {
        try {
            return appDao.edit(app) ? new Json(true, "修改成功！") : new Json(false, "修改失败！");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("update")
    @ResponseBody
    public Json checkUpdate(@RequestParam("id[]") int[] ids, @RequestParam("filterId") int filterId) {
        Filter filter = null;
        try {
            filter = filterDao.get(filterId);
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }

        List<App> apps = null;
        try {
            apps = appDao.list(ids);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Json(false, e.getMessage());
        }

        for (App app : apps) {
            UpdateChecker checker = new HtmlUpdateChecker(new RegexHtmlParser(filter.getContent()), app.getInfoUrl());
            Map<String, Object> result = new HashMap<>();
            app.updateStatus(checker.getName(), checker.getVersion(), checker.getIcon());
        }

        try {
            for (App app : apps) {
                appDao.updateStatus(app);
            }
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }

        return new Json(true);
    }

    @RequestMapping("toggleUpdated")
    @ResponseBody
    public Json toggleUpdated(@RequestParam("id") int id) {
        try {
            appDao.toggleUpdated(id);
            return new Json(true, "切换更新状态");
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("list")
    @ResponseBody
    public Json list() {
        try {
            return new Json(true, appDao.list());
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }
}
