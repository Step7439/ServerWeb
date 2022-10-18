package ru.netology;

public class Request {
    protected final String method;
    protected final String path;


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
    public String getQueryParam(String name){
      return name;
    }
//    public String getQueryParams() {
//        return String a;
//    }

}

