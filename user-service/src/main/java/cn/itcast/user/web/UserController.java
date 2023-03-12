package cn.itcast.user.web;

import cn.itcast.user.config.MyConfig;
import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${test.value}")
    private String value;

    @Autowired
    private MyConfig myConfig;

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id,@RequestHeader(value = "token",required = false)String token) {
        log.info("user token:{}",token);
        return userService.queryById(id);
    }

    @GetMapping("/now")
    public MyConfig now(){
        return myConfig;
    }
}
