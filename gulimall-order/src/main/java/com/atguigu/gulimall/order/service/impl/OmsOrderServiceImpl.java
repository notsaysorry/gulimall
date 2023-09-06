package com.atguigu.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.common.vo.MemberResponseVo;
import com.atguigu.gulimall.order.config.LoginUserInterceptor;
import com.atguigu.gulimall.order.feign.CartFeignService;
import com.atguigu.gulimall.order.feign.MemberFeignService;
import com.atguigu.gulimall.order.feign.WmsFeignService;
import com.atguigu.gulimall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.order.dao.OmsOrderDao;
import com.atguigu.gulimall.order.entity.OmsOrderEntity;
import com.atguigu.gulimall.order.service.OmsOrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;

import static com.atguigu.gulimall.order.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;


@Service("omsOrderService")
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderDao, OmsOrderEntity> implements OmsOrderService {

    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private WmsFeignService wmsFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderEntity> page = this.page(
                new Query<OmsOrderEntity>().getPage(params),
                new QueryWrapper<OmsOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        //构建OrderConfirmVo
        OrderConfirmVo confirmVo = new OrderConfirmVo();

        //获取当前用户登录的信息
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        //获取当前线程请求头信息(解决Feign异步调用丢失请求头问题)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //开启第一个异步任务
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1、远程查询所有的收获地址列表
            List<MemberAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
            confirmVo.setMemberAddressVos(address);
        }, threadPoolExecutor);

        //开启第二个异步任务
        CompletableFuture<Void> cartInfoFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2、远程查询购物车所有选中的购物项
            List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
            confirmVo.setItems(currentCartItems);
        }, threadPoolExecutor).thenRunAsync(() -> {
            List<OrderItemVo> items = confirmVo.getItems();
            //获取全部商品的id
            List<String> skuIds = items.stream()
                    .map((itemVo -> itemVo.getSkuId()))
                    .collect(Collectors.toList());
            //远程查询商品库存信息
            R skuHasStock = wmsFeignService.getSkuHasStock(skuIds);
            List<SkuStockVo> skuStockVos = skuHasStock.getData("data", new TypeReference<List<SkuStockVo>>() {});
            if (skuStockVos != null && skuStockVos.size() > 0) {
                //将skuStockVos集合转换为map
                Map<String, Boolean> skuHasStockMap = skuStockVos.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                confirmVo.setStocks(skuHasStockMap);
            }
        },threadPoolExecutor);
        //3、查询用户积分
        Integer integration = memberResponseVo.getIntegration();
        confirmVo.setIntegration(integration);
        CompletableFuture.allOf(addressFuture,cartInfoFuture).get();
        return confirmVo;
    }

    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        //去创建、下订单、验令牌、验价格、锁定库存...

        //获取当前用户登录的信息
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        responseVo.setCode(0);

        //1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();

        //通过lure脚本原子验证令牌和删除令牌
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId()),
                orderToken);
        if (result == 0L) {

        }
        return responseVo;
    }

}