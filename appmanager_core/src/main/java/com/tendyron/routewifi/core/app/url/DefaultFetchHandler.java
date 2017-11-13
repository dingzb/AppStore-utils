package com.tendyron.routewifi.core.app.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2017/2/11.
 */
public abstract class DefaultFetchHandler implements FetchHandler {

    public static final String PROTOCOL = "protocol";
    public static final String HOST = "host";
    public static final String URL="url";
    public static final String IOS_VERSION = "ios";
    public static final String MODEL = "model";

    private Pattern pattern = Pattern.compile("(?:GET|HEAD|PUT|DELETE|PSOT|OPTIONS)\\s(?<url>/[^\\s]+(?:pre-thinned.+\\.signed\\.dpkg\\.ipa\\?)[^\\s]+)\\s(?:(?<protocol>HTTP(?:S?))/(?:[^\\s]+))(?:\\r?\\n)(?:Host:\\s)(?<host>[^\\s]+)(?:(?:\\r?\\n).*)*(?:User-Agent:\\s).*(?:iOS)/(?<ios>[^\\s]+).*(?:model)/(?<model>[^\\s]+)");

    public DefaultFetchHandler(Pattern pattern) {
        this.pattern = pattern;
    }

    public DefaultFetchHandler() {
    }

    @Override
    public void handle(byte[] data) {
        Matcher matcher = pattern.matcher(new String(data));
        if (matcher.find()) {
            process(matcher);
        }
    }

    protected abstract void process(Matcher matcher);
}
