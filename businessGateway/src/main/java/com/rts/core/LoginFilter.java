package com.rts.core;

import com.alibaba.fastjson.JSONObject;
import com.rts.common.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

// 登录拦截，必须放到ioc容器里面  GlobalFilter 怎么过滤 Ordered 如果你
// 有两三个过滤器，可以通过getOrder()来排序先后
@Component
@Slf4j
public class LoginFilter implements GlobalFilter, Ordered {
    @Resource
    White white;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 创建地址匹配工具
        AntPathMatcher pathMatcher = new AntPathMatcher();
        log.info("进入过滤器");
        // 获取request对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取response对象
        ServerHttpResponse response = exchange.getResponse();
        // 获取到当前的请求地址
        URI uri = request.getURI();
        log.info("获取到的请求地址是 ====> " + uri.getPath());
        System.out.println("白名单里面的请求地址是 ====> " + white.getUrls());
        // 获取到请求白名单
        List<String> white_urls = white.getUrls();
        // 循环白名单地址
        for (String white_url : white_urls) {
            // 如果当前请求地址匹配到任何一个白名单地址 都可以通过
            if (pathMatcher.match(white_url, uri.getPath())) {
                log.info("匹配到白名单！");
                return chain.filter(exchange);
            }
        }
        try {
            log.info("没有匹配到白名单！");
            // 如果没有匹配到白名单 获取请求头中的token
            // 前端_axios.defaults.headers.common.token = store.getters.GET_TOKEN 最后面点什
            // 么这里key写什么
            List<String> token = request.getHeaders().get("token");
            System.out.println("请求头中有值的个数为：" + token.size());
            String key = token.get(0);
            log.info("请求头中的的token值为: ====> " + key);
            // 拿到token之后 到redis中先验证是否存在
            if (!redisTemplate.hasKey(key)) {
                // 如果异常则token有问题 不是没传就是不对
                return error(response, ResultJson.unauth("未登录或者登录已经超时"));
            } else {
                // 延长失效时间
                redisTemplate.expire(key, 30, TimeUnit.MINUTES);

                Object loginId = redisTemplate.opsForHash().get(key, "BusinessloginId");
                // 此处增加一个loginId的参数 为了让controller能够获取 知道请求的人是谁
                // 获取原来的请求参数
                String oldQuery = uri.getQuery();
                log.info(uri.getQuery());
                // 定义动态字符串用于拼接新的参数
                StringBuilder builder = new StringBuilder();
                // 如果原来的请求参数有值 要先拼接原来的的请求参数
                if (StringUtils.isNotBlank(oldQuery)) {
                    builder.append(oldQuery).append("&");
                }
                builder.append("loginId=" + loginId);
                // uri中的query是只读的，无法进行任何改变 只能构建一个新的uri
                URI new_uri = UriComponentsBuilder.fromUri(uri).replaceQuery(builder.toString()).build().toUri();
                // request中的uri是只读的，无法进行任何改变 构建一个新的request
                ServerHttpRequest new_request = request.mutate().uri(new_uri).build();
                // exchange中的request是只读的，无法进行任何修改 构建一个新的exchange
                ServerWebExchange new_exchange = exchange.mutate().request(new_request).build();
                return chain.filter(new_exchange);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // 如果异常则token有问题 不是没传就是不对
            return error(response, ResultJson.unauth("非法请求"));
        }

    }

    private Mono<Void> error(ServerHttpResponse response, ResultJson resultJson) {
        response.getHeaders().set("Content-Type", "application/json:charset=utf-8");
        DataBuffer buffer = response.bufferFactory().wrap(JSONObject.toJSONString(resultJson).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }
    @Override
    public int getOrder() {
        return 0;
    }
}
