# 使用官方 Java 運行環境作為基礎鏡像
FROM openjdk:17-jdk-slim

# 設定容器內部工作目錄
WORKDIR /app

# 將應用的 jar 文件從構建上下文複製到容器內
COPY target/Hello-Service-8085-0.0.1-SNAPSHOT.jar /app/hello-service.jar

# 容器啟動時執行的命令，啟動 Spring Boot 應用
CMD ["java", "-jar", "/app/hello-service.jar"]

# 暴露 8085 端口，供外部訪問
EXPOSE 8085