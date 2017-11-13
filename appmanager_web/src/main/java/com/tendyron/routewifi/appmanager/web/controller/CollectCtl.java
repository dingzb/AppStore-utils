package com.tendyron.routewifi.appmanager.web.controller;

import com.tendyron.routewifi.appmanager.web.dao.AppDao;
import com.tendyron.routewifi.appmanager.web.dao.FilterDao;
import com.tendyron.routewifi.appmanager.web.dao.PhoneDao;
import com.tendyron.routewifi.appmanager.web.dao.VersionDao;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.AppDaoImpl;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.FilterDaoImpl;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.PhoneDaoImpl;
import com.tendyron.routewifi.appmanager.web.dao.sqlite.VersionDaoImpl;
import com.tendyron.routewifi.appmanager.web.model.*;
import com.tendyron.routewifi.core.app.url.DefaultFetchHandler;
import com.tendyron.routewifi.core.app.url.IosIpaUrlFetcher;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/2/11.
 */
@Controller("collect")
@RequestMapping("management/collect")
public class CollectCtl {

    private static final String SESSION_SUBVERSION_NAME = "SUBVERSION";
    private static final String SESSION_SUBVERSION_NAME_SAVED = "SUBVERSION_SAVED";
    private final Object versionsLock = new Object();

    private FilterDao filterDao = new FilterDaoImpl();
    private VersionDao versionDao = new VersionDaoImpl();
    private AppDao appDao = new AppDaoImpl();
    private PhoneDao phoneDao = new PhoneDaoImpl();

    @RequestMapping("devices")
    @ResponseBody
    public Json list() {
        return new Json(true, IosIpaUrlFetcher.deviceList());
    }

    @RequestMapping("start")
    @ResponseBody
    public Json fetch(@RequestParam("dev") int devId, @RequestParam("filter") int filterId, HttpServletRequest request) {

        IosIpaUrlFetcher fetcher;
        Filter filter = null;
        final Map<String, String> phoneMap = new HashMap<>();

        try {
            filter = filterDao.get(filterId);
            phoneMap.putAll(phoneDao.map());
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
        try {
            fetcher = IosIpaUrlFetcher.getInstance(devId);
        } catch (IllegalStateException e) {
            return new Json(false, e.getMessage());
        }

        if (fetcher.isFetching()) {
            return new Json(false, "device is in fetching");
        } else {
            final LinkedList<Version> versions = new LinkedList<>();
            final Set<String> signatures = new HashSet<String>();
            request.getSession().setAttribute(SESSION_SUBVERSION_NAME, versions);
            final String pattern = filter.getContent();
            final SessionFetchHandler handler = new SessionFetchHandler(signatures, versions, pattern, phoneMap);
            new Thread(() -> {
                fetcher.fetch(handler);
            }).start();
            return new Json(true, "start get fetch on device: " + devId);
        }

    }

    @RequestMapping("stop")
    @ResponseBody
    public Json stopFetch(@RequestParam("dev") int devId, HttpServletRequest request) {
        try {
            IosIpaUrlFetcher.getInstance(devId).stopFetch();
            request.getSession().setAttribute(SESSION_SUBVERSION_NAME_SAVED, request.getSession().getAttribute(SESSION_SUBVERSION_NAME));
            request.getSession().removeAttribute(SESSION_SUBVERSION_NAME);
            return new Json(true, "stop fetch success.");
        } catch (IllegalStateException e) {
            return new Json(false, e.getMessage());
        }
    }

    @RequestMapping("new")
    @ResponseBody
    public Json getFetch(@RequestParam("last") String lastHash, HttpServletRequest request) {

        @SuppressWarnings("unchecked")
        LinkedList<Version> versions = (LinkedList<Version>) request.getSession().getAttribute(SESSION_SUBVERSION_NAME);

        if (versions == null) {
            return new Json(false, "no getVersion info in session.");
        }

        long timeout = 30000;
        Date start = new Date();
        Version version = null;
        while (version == null || version.getSignature().equals(lastHash)) {
            if ((new Date().getTime() - start.getTime() > timeout)) {
                return new Json(true, "get fetch timeout.");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (versionsLock) {
                version = versions.peekLast();
                try {
                    if (version != null) {
                        version = (Version) version.clone();
                    }
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (versionsLock) {
            return new Json(true, versions);
        }
    }

    @RequestMapping("save")
    @ResponseBody
    public Json save(@RequestParam("appId") int appId, HttpServletRequest request) {

        String appVersion = null;
        try {
            App app = appDao.get(appId);
            appVersion = app.getVersion();
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
        @SuppressWarnings("unchecked")
        LinkedList<Version> versions = (LinkedList<Version>) request.getSession().getAttribute(SESSION_SUBVERSION_NAME_SAVED);

        try {
            return new Json(versionDao.add(appId, appVersion, versions), "save");
        } catch (SQLException e) {
            return new Json(false, e.getMessage());
        }
    }

    class SessionFetchHandler extends DefaultFetchHandler {
        private Set<String> signatures;
        private LinkedList<Version> versions;
        private Map<String, String> phoneMap;

        public SessionFetchHandler(Set<String> signatures, LinkedList<Version> versions, String patternStr, Map<String, String> phoneMap) {
            super(Pattern.compile(patternStr));
            this.signatures = signatures;
            this.versions = versions;
            this.phoneMap = phoneMap;
        }

        @Override
        protected void process(Matcher matcher) {
            Version version = new Version();
            version.setDownloadUrl(matcher.group(DefaultFetchHandler.PROTOCOL).toLowerCase()
                    + "://" + matcher.group(DefaultFetchHandler.HOST)
                    + matcher.group(DefaultFetchHandler.URL));
            version.setIosVersion(matcher.group(DefaultFetchHandler.IOS_VERSION));
            version.setModel(matcher.group(DefaultFetchHandler.MODEL));
            version.setFriendlyModel(phoneMap.get(version.getModel()));
            version.setCaptureTime(new Date());
            version.setSignature(DigestUtils.md5Hex(version.toString()));
            if (!signatures.contains(version.getSignature())) {
                signatures.add(version.getSignature());
                synchronized (versionsLock) {
                    versions.add(version);
                }
            }
        }
    }
}
