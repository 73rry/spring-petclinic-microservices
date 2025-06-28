package org.springframework.samples.petclinic.api.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

/**
 * Ghi log cho mỗi request / response đi qua Spring Cloud Gateway   
 * Nhờ Micrometer Tracing đã có mặt trên classpath, MDC sẽ tự chứa traceId và spanId.   
 * Pattern log đã có %X{traceId} / %X{spanId} nên chỉ cần log INFO là hiển thị.  
 */
@Component
public class LogRequestGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LogRequestGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log request đầu vào
        log.info("⇢ {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        // Tiếp tục filter chain và log khi response hoàn tất
        return chain.filter(exchange)
                .doOnSuccessOrError((r, ex) -> {
                    log.info("⇠ {} {}", exchange.getResponse().getStatusCode(), exchange.getRequest().getURI());
                });
    }

    // Đặt ngay trước WRITE_RESPONSE_FILTER để chắc chắn log sau khi response sinh xong
    @Override
    public int getOrder() {
        return -1;
    }
} 