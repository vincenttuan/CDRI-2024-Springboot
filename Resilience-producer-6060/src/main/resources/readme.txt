課程名稱：
使用 Resilience4j 增強 Spring Boot 應用的穩定性與容錯性

課程介紹：
在現代分佈式系統中，應用的穩定性與容錯性是至關重要的。Resilience4j 是一個專為 Java 設計的輕量級容錯庫，提供了斷路器（Circuit Breaker）、重試（Retry）、隔離（Bulkhead）等多種容錯機制。本課程將帶您一步步學習如何使用 Resilience4j 在 Spring Boot 應用中實現這些技術，從而提升應用的可靠性。

課程目標：
了解 Resilience4j 的基本概念和功能。
學習如何在 Spring Boot 應用中配置和使用斷路器（Circuit Breaker）。
學習如何在 Spring Boot 應用中配置和使用重試（Retry）。
學習如何在 Spring Boot 應用中配置和使用隔離（Bulkhead），包括信號量和線程池兩種模式。
學習如何在 Spring Boot 應用中配置和使用限流（Rate Limiter）。
學習如何在 Spring Boot 應用中配置和使用時間限制（Time Limiter）。
掌握全局異常處理技術，統一處理應用中的異常情況。

details": {
    "failureRate": "100.0%",
    "failureRateThreshold": "50.0%",
    "slowCallRate": "0.0%",
    "slowCallRateThreshold": "100.0%",
    "bufferedCalls": 5,
    "slowCalls": 0,
    "slowFailedCalls": 0,
    "failedCalls": 5,
    "notPermittedCalls": 2,
    "state": "OPEN"
 }
 
這個JSON片段提供了斷路器的詳細健康指標信息，以下是各項指標的繁體中文解釋：
failureRate: "100.0%"，表示在滑動窗口期內，呼叫失敗的比率達到了100%。
failureRateThreshold: "50.0%"，這是觸發斷路器開啟的失敗率閾值。在這個例子中，如果呼叫失敗率超過50%，則斷路器會開啟。
slowCallRate: "0.0%"，表示沒有呼叫被認定為是慢調用。慢調用是指那些執行時間超過預定閾值的調用。
slowCallRateThreshold: "100.0%"，這是將調用認定為慢調用的閾值。在這個配置中，只有當調用的執行時間超過100%的預定時間時，才會被認為是慢調用，實際上這表示慢調用檢測並未啟用。
bufferedCalls: 5，這表示在滑動窗口期內，總共有5次呼叫被記錄（包括成功、失敗和慢調用）。
slowCalls: 0，表示沒有調用被認定為慢調用。
slowFailedCalls: 0，表示沒有慢調用且失敗的情況。
failedCalls: 5，表示有5次調用失敗。
notPermittedCalls: 2，這表示有2次調用在斷路器開啟（或半開）狀態下被拒絕，沒有被執行。
state: "OPEN"，斷路器的當前狀態是開啟（OPEN）。在開啟狀態下，所有的呼叫都將被自動拒絕，直到斷路器變為半開狀態，此時允許一部分調用通過以檢測系統是否已恢復正常。
當斷路器處於開啟狀態時，它會阻止可能導致進一步失敗的呼叫，這有助於防止系統過載並允許底層服務恢復正常。

