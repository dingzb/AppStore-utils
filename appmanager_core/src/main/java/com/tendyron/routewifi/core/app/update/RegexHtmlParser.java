package com.tendyron.routewifi.core.app.update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/2/15.
 */
public class RegexHtmlParser implements HtmlParser {

    private Pattern pattern;
    private Matcher matcher;
    private boolean found = false;

    /**
     * @param patternStr must contain groups with name [key],[icon] and [name].
     */
    public RegexHtmlParser(String patternStr) {
        this.pattern = Pattern.compile(patternStr);
    }

    @Override
    public String getKey() {
        return group("key");
    }

    @Override
    public String getIcon() {
        return group("icon");
    }

    @Override
    public String getName() {
        return group("name");
    }

    @Override
    public void updateHtml(String html) {
        matcher = pattern.matcher(html);
        found = matcher.find();
    }

    @Override
    public boolean isFind() {
        return found;
    }

    private String group(String name) {
        try {
            return matcher.group(name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "";
        }
    }
}
