package com.tendyron.routewifi.appmanager.web.controller;

/**
 * Created by Neo on 2017/2/20.
 */

import com.tendyron.routewifi.appmanager.web.dao.FilterDao;
import com.tendyron.routewifi.appmanager.web.dao.VersionDao;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.FilterDaoImpl;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.VersionDaoImpl;
import com.tendyron.routewifi.appmanager.web.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

@Controller("version")
@RequestMapping("management/version")
public class VersionCtl {
    private VersionDao versionDao = new VersionDaoImpl();
    private FilterDao filterDao = new FilterDaoImpl();


    @RequestMapping("del")
    @ResponseBody
    public Json del(@RequestParam("id[]") int[] ids) {
        try {
            return new Json(versionDao.del(ids), "删除");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("paging")
    @ResponseBody
    public Json paging(Version filter, PagingQuery query, @RequestParam(value = "patternId", defaultValue = "0", required = false) int filterId,
                       @RequestParam(value = "isGroup", required = false, defaultValue = "false") boolean group) {
        try {
            Paging paging = null;
            if (group) {
                if (filterId == 0) {
                    return new Json(false, "patternId is no specified.");
                }
                Filter paFilter = filterDao.get(filterId);
                paging = versionDao.group(filter, query, paFilter.getContent());
            } else {
                paging = versionDao.paging(filter, query);
            }
            return new Json(true, paging);
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("versions")
    @ResponseBody
    public Json versions(@RequestParam("appId") int appId) {
        try {
            return new Json(true, versionDao.VersionsByAppId(appId));
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("download")
    public void downloadRecord(HttpServletResponse response, Version filter,
                               @RequestParam("filterId") int filterId) {

        List<String> urls = null;
        try {
            Filter paFilter = filterDao.get(filterId);
            if (paFilter == null){
                return;
            }
            urls = versionDao.listUrl(filter, paFilter.getContent());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (urls == null) {
            return;
        }
        response.addHeader("Content-Disposition", "attachment;fileName=monitor_record.txt");


        OutputStream os = null;
        try {
            os = response.getOutputStream();
            for (String url : urls) {
                System.out.println(url);
                os.write(url.getBytes());
                os.write("\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
