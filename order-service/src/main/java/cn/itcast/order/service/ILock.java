package cn.itcast.order.service;

public interface ILock {
    boolean tryLock(String key, long timeoutSec);

    void unlock(String key);
}
