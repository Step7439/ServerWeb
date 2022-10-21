package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {
    protected final String method;
    protected String path;

    protected List<NameValuePair> list;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryParam(String name) {
        var builder = new StringBuilder();
        for (NameValuePair pair : this.list) {
            if (pair.getName().equals(name)) {
                builder.append(pair + " ");
            }
        }
        return builder.toString();
    }

    public String getQueryParams() {
        return this.list.toString();
    }
    public static Request createRequest(String method, String url) {
        Request req = new Request(method, url);
        req.parsePath(url);
        req.parseQuery(url);
        return req;
    }

    private void parseQuery(String url) {
        if (url.indexOf('?') == -1) {
        return;
        }
        var splitPath = url.split("\\?")[1];
        var parsedQuery = splitPath.split("#")[0];
        this.list = URLEncodedUtils.parse(parsedQuery, StandardCharsets.UTF_8);
    }

    private void parsePath(String url) {
        var splittedPath = url.split("\\?")[0];
        this.path = splittedPath;
    }

}

