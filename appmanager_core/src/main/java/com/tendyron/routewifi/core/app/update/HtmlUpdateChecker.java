package com.tendyron.routewifi.core.app.update;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Neo on 2017/2/15.
 */
public class HtmlUpdateChecker implements UpdateChecker {

    private HtmlParser parser;
    private String url;
    private String html;

    public HtmlUpdateChecker(HtmlParser parser, String url) {
        this.parser = parser;
        this.url = url;
        getHtml();
        parser.updateHtml(html);
    }

    private void getHtml(){
        HttpClient client = HttpClients.createDefault();
        try {
            HttpResponse response = client.execute(new HttpGet(url));
            html = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getVersion() {
        if (!parser.isFind()){
            throw new IllegalStateException("parser is not updated.");
        }
        return parser.getKey();
    }

    @Override
    public String getIcon() {
        if (!parser.isFind()){
            throw new IllegalStateException("is not found.");
        }
        return parser.getIcon();
    }

    @Override
    public String getName() {
        if (!parser.isFind()){
            throw new IllegalStateException("is not found.");
        }
        return parser.getName();
    }
}
