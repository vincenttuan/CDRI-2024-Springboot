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

@Configuration
public class Resilience4jConfig {

//    @Bean
//    public RetryRegistry retryRegistry() {
//        RetryRegistry registry = RetryRegistry.ofDefaults();
//        Retry retry = registry.retry("employeeRetry");
//        retry.getEventPublisher().onRetry(event -> System.out.println("retry"));
//        return registry;
//    }
    
    
	// maxAttempts(3): 表示在初始嘗試一次失敗後, 重試將進行二次, 所以總共是三次
	@Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(3) // 包含初始嘗試失敗在內的總次數
            .waitDuration(java.time.Duration.ofMillis(500))
            .build();
        
        RetryRegistry registry = RetryRegistry.of(config);
        registry.retry("employeeRetry").getEventPublisher().onRetry(event -> {
            System.out.println("retry");
        });
        return registry;
    }
	
	// 這樣配置之後，每次調用 getEmployee 方法時，最多允許 5 個並發調用。
	// 如果超過這個數量，額外的調用將等待最多 2 秒。
	// 如果超過這個等待時間，調用將失敗並觸發回退方法。
	@Bean
    public BulkheadRegistry bulkheadRegistry() {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(5)
            .maxWaitDuration(java.time.Duration.ofSeconds(2))
            .build();
        
        BulkheadRegistry registry = BulkheadRegistry.of(config);

        registry.bulkhead("employeeBulkhead").getEventPublisher()
            .onCallRejected(event -> System.out.println("Bulkhead call rejected"))
            .onCallPermitted(event -> System.out.println("Bulkhead call permitted"))
            .onCallFinished(event -> System.out.println("Bulkhead call finished"));

        return registry;
    }
    
	// 說明: 當使用 ThreadPoolBulkhead 時, 需要設置 ThreadPoolBulkheadConfig
	// ThreadPoolBulkheadConfig: 表示線程池的配置信息
	// maxThreadPoolSize(5): 表示線程池最大大小
	// coreThreadPoolSize(5): 表示核心線程池大小
	// queueCapacity(10): 表示等待佇列容量
	// ThreadPoolBulkheadRegistry: 表示線程池的註冊表
	// ThreadPoolBulkheadRegistry.of(config): 表示創建一個線程池的註冊表
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
	
	// 限流（Rate Limiter）
	// 限流機制可以防止系統被過多的請求淹沒，從而保護後端服務。Resilience4j 提供了限流的支持，可以設置每秒允許的最大請求數量。
	@Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .limitForPeriod(10)
            .timeoutDuration(Duration.ofMillis(500))
            .build();
        
        return RateLimiterRegistry.of(config);
    }
	
	// 時間限制（Time Limiter）
	// 這個功能允許你設置方法執行的最大時間，超過這個時間將會拋出 TimeoutException。這對於確保系統不會因為某些請求長時間未響應而被拖垮非常有用。
	@Bean
    public TimeLimiterRegistry timeLimiterRegistry() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(2))
            .build();
        
        return TimeLimiterRegistry.of(config);
    }
	
}
