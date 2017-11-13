package com.tendyron.routewifi.core.app.update;

/**
 * Created by Neo on 2017/2/15.
 */
public interface HtmlParser {
    String getKey();

    String getIcon();

    String getName();

    void updateHtml(String html);

    boolean isFind();
}
