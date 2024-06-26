# Docker 啟動
https://www.rabbitmq.com/docs/download
啟動指令: (要先啟動 Docker Desktop)
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management


# 安裝版啟動
https://www.rabbitmq.com/docs/install-windows

啟動Web管理介面
rabbitmq-plugins enable rabbitmq_management

啟動指令:
rabbitmq-server start

關閉指令:
rabbitmqctl stop

