package edu.fudan.se.asof.network;

/**
 * Created by Dawnwords on 2014/4/9.
 */
public interface NetworkListener<T> {
    void onSuccess(T data);

    void onFailure(String reason);
}
