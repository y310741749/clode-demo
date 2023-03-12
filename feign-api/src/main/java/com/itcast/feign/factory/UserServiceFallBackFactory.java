package com.itcast.feign.factory;

import com.itcast.feign.UserService;
import com.itcast.pojo.MyConfig;
import com.itcast.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceFallBackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable cause) {
        log.error("出现异常");
        return new UserService() {
            @Override
            public User queryById(Long id) {
                System.out.println("queryById异常");
                return new User();
            }

            @Override
            public MyConfig now() {
                System.out.println("now异常");
                return new MyConfig();
            }
        };
    }
}
