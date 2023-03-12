package cn.itcast.order.web;

import cn.itcast.order.service.ILock;
import com.alibaba.nacos.common.utils.ThreadUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RestController
public class TestController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ILock ilock;
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/get/{key}")
    public String get(@PathVariable String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (null == o) {
            return null;
        }
        return (String) o;
    }

    @PostMapping("/set/{key}/{value}")
    public void set(@PathVariable String key, @PathVariable String value) {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS);
        System.out.println(aBoolean.booleanValue());
    }

    @PostMapping("/setLock/{key}/{time}")
    public void setLock(@PathVariable String key, @PathVariable Long time) {
        BiConsumer<String, Long> biConsumer = (k, t) -> {
            boolean b = ilock.tryLock(k, t);
            String name = Thread.currentThread().getName();
            System.out.println("线程" + name + "加锁结果:" + b);
            while (!b) {
                ThreadUtils.sleep(1000);
                System.out.println("==线程" + name + "尝试重新加锁==");
                boolean b1 = ilock.tryLock(k, t);
                System.out.println("线程" + name + "加锁结果:" + b1);
                b = b1;
            }
            if (b) {
                ThreadUtils.sleep(5000);
                ilock.unlock(key);
            }
        };
        for (int i = 0; i < 2; i++) {
            CompletableFuture.runAsync(() -> {
                biConsumer.accept(key, time);
            });
        }
//        for (int i = 0; i < 2; i++) {
//            CompletableFuture.runAsync(() -> {
//                boolean b = ilock.tryLock(key, time);
//                String name = Thread.currentThread().getName();
//                System.out.println("线程" + name + "加锁结果:" + b);
//                while (!b) {
//                    ThreadUtils.sleep(1000);
//                    System.out.println("==线程" + name + "尝试重新加锁==");
//                    boolean b1 = ilock.tryLock(key, time);
//                    System.out.println("线程" + name + "加锁结果:" + b1);
//                    b = b1;
//                }
//            });
//        }
    }

    @PostMapping("/setRedissonLock/{key}")
    public void setRedissonLock(@PathVariable String key) {
        Consumer<String> Consumer = (k) -> {
            RLock lock = redissonClient.getLock(k);
            boolean b2 = false;
            try {
                b2 = lock.tryLock(1,10,TimeUnit.SECONDS);
                if (b2) {
                    String name = Thread.currentThread().getName();
                    System.out.println("线程:" + name + "加锁成功");
                    ThreadUtils.sleep(5000);
                }else {
                    String name = Thread.currentThread().getName();
                    System.out.println("线程:" + name + "加锁失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                System.out.println("线程:" + Thread.currentThread().getName() + "释放锁");
                lock.unlock();
            }

        };
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(() -> {
                Consumer.accept(key);
            });
        }
    }
}