package edu.fudan.se.asof.engine;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class ResultHolder<T> {
    private T result;

    public T get() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ignore) {
            }
        }
        Log.debug(result.toString());
        return result;
    }

    public void set(T result) {
        Log.debug(result.toString());
        synchronized (this) {
            this.result = result;
            this.notify();
        }
    }
}