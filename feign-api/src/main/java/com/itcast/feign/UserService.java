package com.itcast.feign;

//import com.itcast.feign.factory.UserServiceFallBackFactory;
import com.itcast.feign.factory.UserServiceFallBackFactory;
import com.itcast.pojo.MyConfig;
import com.itcast.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient( value = "userService",fallbackFactory = UserServiceFallBackFactory.class)
//@FeignClient( value = "userService")
public interface UserService {
    @GetMapping("/user/{id}")
    User queryById(@PathVariable("id") Long id);

    @GetMapping("/user/now")
    MyConfig now();
}
