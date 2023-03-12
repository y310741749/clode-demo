package cn.itcast.order.web;

import cn.itcast.order.service.OrderService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.nacos.common.utils.ThreadUtils;
import com.itcast.feign.UserService;
import com.itcast.pojo.MyConfig;
import com.itcast.pojo.Order;
import com.itcast.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    @SentinelResource("hot")
    public Order queryOrderByUserId(@RequestParam Long orderId, @RequestHeader(name = "Cookie",required = false) String token) {
        log.info("order token:{}", token);
        // 根据id查询订单并返回
        Order order = orderService.queryOrderById(orderId);
        User user = userService.queryById(order.getUserId());
        order.setUser(user);
        return order;
    }

    @GetMapping("/now")
    public MyConfig now() {
        return userService.now();
    }

    @GetMapping("/query")
    public String query(){
        orderService.queryGoods();
        return "query查询";
    }

    @GetMapping("/save")
    public String save(){
        orderService.queryGoods();
        return "save新增";
    }

    @PostMapping("/update")
    public String update(){
        return "update更新";
    }
}
