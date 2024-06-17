package com.example.demo;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Resilience4j 配置類，用於配置各種容錯機制如重試、限流、隔離和時間限制等。
 */
@Configuration
public class Resilience4jConfig {

    /**
     * 配置重試機制 (Retry)
     * 目的是確保服務在遇到臨時故障時能夠重試，從而提高服務的穩定性。
     * 運作原理是設置最大嘗試次數和重試間隔時間，在指定次數內重試請求。
     * 
     * maxAttempts(3): 表示在初始嘗試一次失敗後，重試將進行兩次，所以總共是三次。
     * waitDuration: 重試之間的等待時間為 500 毫秒。
     * 
     * @return RetryRegistry
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(3) // 包含初始嘗試失敗在內的總次數
            .waitDuration(Duration.ofMillis(500))
            .build();
        
        RetryRegistry registry = RetryRegistry.of(config);
        registry.retry("employeeRetry").getEventPublisher().onRetry(event -> {
            System.out.println("retry");
        });
        return registry;
    }

    /**
     * 配置信號量隔離機制 (Bulkhead)
     * 目的是限制同時執行的請求數量，防止過多的並發請求導致系統過載。
     * 運作原理是設置最大並發請求數量和最大等待時間，超過限制的請求將被拒絕或等待。
     * 
     * maxConcurrentCalls(5): 每次調用 getEmployee 方法時，最多允許 5 個並發調用。
     * maxWaitDuration: 如果超過這個數量，額外的調用將等待最多 2 秒。
     * 
     * @return BulkheadRegistry
     */
    @Bean
    public BulkheadRegistry bulkheadRegistry() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(5)
            .maxWaitDuration(Duration.ofSeconds(2))
            .build();
        
        BulkheadRegistry registry = BulkheadRegistry.of(config);

        registry.bulkhead("employeeBulkhead").getEventPublisher()
            .onCallRejected(event -> System.out.println("Bulkhead call rejected"))
            .onCallPermitted(event -> System.out.println("Bulkhead call permitted"))
            .onCallFinished(event -> System.out.println("Bulkhead call finished"));

        return registry;
    }
    
    /**
     * 配置線程池隔離機制 (ThreadPool Bulkhead)
     * 目的是通過線程池來限制並發請求數量，防止單個服務的問題影響整個系統。
     * 運作原理是設置線程池大小和佇列容量，超過限制的請求將被拒絕或排隊等待。
     * 
     * maxThreadPoolSize(5): 線程池最大大小。
     * coreThreadPoolSize(5): 核心線程池大小。
     * queueCapacity(10): 等待佇列容量。
     * 
     * @return ThreadPoolBulkheadRegistry
     */
    @Bean
    public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry() {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
            .maxThreadPoolSize(5) // 線程池最大大小
            .coreThreadPoolSize(5) // 核心線程池大小
            .queueCapacity(10) // 等待佇列容量
            .build();
        
        ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);

        registry.bulkhead("employeeBulkhead2").getEventPublisher()
            .onCallRejected(event -> System.out.println("Bulkhead ThreadPool call rejected"))
            .onCallPermitted(event -> System.out.println("Bulkhead ThreadPool call permitted"))
            .onCallFinished(event -> System.out.println("Bulkhead ThreadPool call finished"));

        return registry;
    }

    /**
     * 配置限流機制 (Rate Limiter)
     * 目的是限制每秒允許的請求數量，防止系統被過多的請求淹沒。
     * 運作原理是設置每秒允許的最大請求數量和超時時間，超過限制的請求將被拒絕。
     * 
     * limitRefreshPeriod: 設置限流的刷新週期為 1 秒。
     * limitForPeriod: 設置每個週期內允許的最大請求數量為 10。
     * timeoutDuration: 設置請求超時時間為 500 毫秒。
     * 
     * @return RateLimiterRegistry
     */
    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .limitForPeriod(10)
            .timeoutDuration(Duration.ofMillis(500))
            .build();
        
        return RateLimiterRegistry.of(config);
    }

    /**
     * 配置時間限制機制 (Time Limiter)
     * 目的是限制方法執行的最大時間，防止長時間未響應的請求拖垮系統。
     * 運作原理是設置方法執行的最大時間，超過這個時間將拋出 TimeoutException。
     * 
     * timeoutDuration: 設置方法執行的最大時間為 2 秒。
     * 
     * @return TimeLimiterRegistry
     */
    @Bean
    public TimeLimiterRegistry timeLimiterRegistry() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(2))
            .build();
        
        return TimeLimiterRegistry.of(config);
    }
}
