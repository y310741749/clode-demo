package cn.itcast.order.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ILockImpl implements ILock {
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    private static RedisScript<Long> redisScript;

    private String threadName;

    static {
        redisScript = new DefaultRedisScript<>();
        ((DefaultRedisScript<Long>) redisScript).setLocation(new ClassPathResource("unLock.lua"));
        ((DefaultRedisScript<Long>) redisScript).setResultType(Long.class);
    }

    @Override
    public boolean tryLock(String key, long timeoutSec) {
        threadName = Thread.currentThread().getName();
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, Thread.currentThread().getName(), timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(aBoolean);
    }

    @Override
    public void unlock(String key) {
        List<String> list = Lists.newArrayList();
        list.add(key);
        Long execute = redisTemplate.execute(redisScript, list, threadName);
        System.out.println("线程" + threadName + "释放锁结果:" + execute);
    }

}
