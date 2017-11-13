package com.tendyron.routewifi.appmanager.web.controller;

import com.tendyron.routewifi.appmanager.web.dao.AgencyDao;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.AgencyDaoImpl;
import com.tendyron.routewifi.appmanager.web.model.Agency;
import com.tendyron.routewifi.appmanager.web.model.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Neo on 2017/1/24.
 */
@Controller("agency")
@RequestMapping("management/agency")
public class AgencyCtl {

    private AgencyDao agencyDao = new AgencyDaoImpl();

    @RequestMapping("list")
    @ResponseBody
    public Json list() {
        List<Agency> list = null;
        try {
            list = agencyDao.list();
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
        return new Json(true, list);
    }

    @RequestMapping("add")
    @ResponseBody
    public Json add(Agency agency) {
        try {
            return new Json(agencyDao.add(agency), "添加");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("del")
    @ResponseBody
    public Json del(@RequestParam("id[]") int[] ids) {
        try {
            return new Json(agencyDao.del(ids), "删除");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("edit")
    @ResponseBody
    public Json edit(Agency agency) {
        try {
            return new Json(agencyDao.edit(agency), "修改");
        } catch (Exception e) {
            return new Json(false, e.getMessage());
        }
    }
}
