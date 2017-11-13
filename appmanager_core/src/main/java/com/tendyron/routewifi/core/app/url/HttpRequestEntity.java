package com.tendyron.routewifi.core.app.url;

/**
 * Created by Neo on 2017/1/4.
 * <p>
 * http请求对象实体
 */
public class HttpRequestEntity {
    private String method;
    private String uri;
    private String protocol;
    private String host;
    private String userAgent;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getFullUrl() {
        return protocol.split("/")[0].toLowerCase() + "://" + host + uri;
    }

    @Override
    public String toString() {
        return "HttpRequestEntity{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
