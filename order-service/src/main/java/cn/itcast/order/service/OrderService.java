package cn.itcast.order.service;

import cn.itcast.order.mapper.OrderMapper;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.itcast.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 4.返回
        return order;
    }

    @SentinelResource("goods")
    public void queryGoods(){
        System.out.println("查询商品");
    }
}
