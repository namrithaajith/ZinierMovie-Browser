package com.zinierapp.movies.popularmovies.apiUtils;

public class NetworkState {
    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED=new NetworkState(Status.SUCCESS,"Success");
        LOADING=new NetworkState(Status.RUNNING,"Running");
    }

    public Status getStatus() {
        return status;
    }
    public static NetworkState error(String msg) {
        return new NetworkState(Status.FAILED, msg);
    }

    public String getMsg() {
        return msg;
    }

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }

}