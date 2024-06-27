<pre>
 * 申請: Gmail 應用程式密碼
 * 1. 登錄你的 Google 帳戶。
 * 2. 前往 https://myaccount.google.com/security
 * 3. 點選"兩步驟驗證"
 * 4. 點選"應用程式密碼" <-- 網頁最下方
 * ps: 若看不到"應用程式密碼", 請點選下方網址
   <a href="https://myaccount.google.com/apppasswords?pli=1&rapt=AEjHL4N4imft3Utjxqp0tmG-NW8zedKzQoKwtNqkPkUFd7BpnC6Se5HtNGnKDfzP3x5UpSPgyfjuEHAMNY27rVhsV0gOr33vN9s7IkYMr-EurprkV-L0mvE">
 * https://myaccount.google.com/apppasswords?pli=1&rapt=AEjHL4N4imft3Utjxqp0tmG-NW8zedKzQoKwtNqkPkUFd7BpnC6Se5HtNGnKDfzP3x5UpSPgyfjuEHAMNY27rVhsV0gOr33vN9s7IkYMr-EurprkV-L0mvE
   </a>  
 * 5. 這將生成一個應用程序專用密碼，複製此密碼。
</pre>

# RabbitMQ
<pre>   
一個完整的測試流程，以確保 RabbitMQ 容器在重啟後仍能保留消息。

步驟 1: 啟動 RabbitMQ 容器
首先，我們需要啟動一個配置了持久化存儲的 RabbitMQ 容器。確保你已經在主機上準備好了映射目錄 c:/rabbitmq_data。
<font style="background-color: black; color: white; padding: 10px;">
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -v c:/rabbitmq_data:/var/lib/rabbitmq rabbitmq:3.13-management
</font>
步驟 2: 下載和安裝 rabbitmqadmin
進入容器，安裝所需的工具，並下載 rabbitmqadmin 工具：

docker exec -it rabbitmq bash
apt-get update && apt-get install -y curl
curl -o rabbitmqadmin http://localhost:15672/cli/rabbitmqadmin
chmod +x rabbitmqadmin

步驟 3: 創建持久化隊列
使用 rabbitmqadmin 在 RabbitMQ 中聲明一個持久化隊列：

./rabbitmqadmin declare queue name=my_persistent_queue durable=true

步驟 4: 發送持久化消息
向剛剛建立的持久化隊列發送消息，並確保消息被設置為持久化：

./rabbitmqadmin publish exchange=amq.default routing_key=my_persistent_queue payload="Hello, World!" properties='{"delivery_mode":2}'

步驟 5: 驗證消息
在不影響消息重新排入隊列的情況下檢查消息：

./rabbitmqadmin get queue=my_persistent_queue ackmode=ack_requeue_true

步驟 6: 重啟容器
重啟 RabbitMQ 容器以測試消息持久性：

docker restart rabbitmq

步驟 7: 再次驗證消息
容器重啟後，再次使用 rabbitmqadmin 檢查隊列中的消息，以確保它們在重啟後仍然存在：
   
</pre>
