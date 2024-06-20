package com.example.demo.config;

import java.time.Duration;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

/**
 * Resilience4j 配置類，用於配置各種容錯機制如重試、限流、隔離和時間限制等。
 */
@Configuration
public class ResilienceConfig {
	
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
				.maxAttempts(3) // 表示在初始嘗試一次失敗後，重試將進行兩次，所以總共是三次。
				.waitDuration(Duration.ofMillis(500))
				.build();
		RetryRegistry registry = RetryRegistry.of(config);
		// 觀察
		registry.retry("employeeRetry").getEventPublisher().onRetry(event -> {
			System.out.println("retry " + new Date().getTime());
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
				.maxWaitDuration(Duration.ofSeconds(5))
				.build();
		
		BulkheadRegistry registry = BulkheadRegistry.of(config);
		
		registry.bulkhead("employeeBulkhead").getEventPublisher()
			.onCallRejected(event -> System.out.println("Bulkhead call Rejected"))
			.onCallPermitted(event -> System.out.println("Bulkhead call Permitted"))
			.onCallFinished(event -> System.out.println("Bulkhead call Finished"));
		
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
}


