package com.aboutdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class RouteLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RouteLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().pathWithinApplication().value();

        // 记录请求进来的原始路径
        logger.info("Request received for path: {}", requestPath);

        return chain.filter(exchange).doFinally(signalType -> {
            // 获取路由到的目标地址（这里的 getUri 获取的是最终确定的目标地址）
            String targetUri = exchange.getRequest().getURI().toString();
            logger.info("Request routed to: {}", targetUri);
        });
    }

    @Override
    public int getOrder() {
        // 设置过滤器执行顺序，这里设置一个合适的顺序值，例如可以设为一个较小的值让它较早执行
        return -1;
    }
}