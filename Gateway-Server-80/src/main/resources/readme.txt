配置:
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

spring-cloud-starter-gateway:
這是 Spring Cloud Gateway 的主要啟動器，在 Spring 5 之後它是基於 WebFlux 和非阻塞的 API。
它提供了基於異步、事件驅動的架構的 API Gateway 功能，適用於反應式微服務架構。
此啟動器適合於需要高吞吐量和低延遲的應用場景。
---------------------------------------------------------------------------------

+------------+     +--------------+     +---------------+
|            |     |              |     |               |
|  Gateway   +---->+    Eureka    +---->+    Orders     |
|            |     |    Server    |     |    Service    |
+------------+     +--------------+     +---------------+
                           ^         ^
                           |         |_________
                   +-------+------+     +------+--------+
                   |              |     |               |
                   |  Customers   |     |   Products    |
                   |   Service    |     |    Service    |
                   +--------------+     +---------------+

---------------------------------------------------------------------------------
沒有 Gateway
client 端要自行記住每一個為服務的 ip 與 port, 例如:
http://10.1.1.2:9091/products/1
http://10.5.5.2:9092/customers/1
http://10.8.7.5:9093/orders/1

有 Gateway
client 端不用記住每一個為服務的 ip 與 port
只要知道 gateway 的位置(localhost)即可, 例如:
http://localhost/products/1
http://localhost/customers/1
http://localhost/orders/1








