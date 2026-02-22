# 版本记录
- **版本号**：前端v25.04.10 & 后端v25.04.10
    - **发布日期**：25.04.10    发布成功
    - **主要变更**：数据库切换为阿里云MySQL

- **版本号**：前端v25.10.29 & 后端v25.10.29
    - **发布日期**：25.10.29   发布成功
    - **主要变更**：废弃  gateway  snowflakeID  网页文件 放在 static 文件夹下托管




现在生产数据库在  阿里云上    jdbc:mysql://rm-wz99kfxt13jnuimburo.mysql.rds.aliyuncs.com:3306/notes

username: dk
password: consoleA1
url: jdbc:mysql://rm-wz99kfxt13jnuimburo.mysql.rds.aliyuncs.com:3306/notes?characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true


// 运行在 阿里云 上  119.23.74.175
pm2 start "java -jar /root/jars/pro/note-service-v25.10.29.jar --server.port=8081 --spring.cloud.nacos.discovery.enabled=false --spring.cloud.service-registry.auto-registration.enabled=false" --name note-service-v25.10.29  

//      废弃，改用 nacos  运行在华为云 Flexus 云服务器  上 113.44.7.213
pm2 start "java -jar /home/ubuntu/jars/discovery-service-1.2.jar" --name discovery-service-1.2     

//      停止运行      阿里云 上  119.23.74.175
pm2 start "java -jar /root/jars/pro/gateway-service-25.04.10.jar --server.port=9090" --name gateway-service-gateway-service-25.04.10    

//      废弃  运行在  腾讯云   上   114.132.189.139
pm2 start "java -jar /home/ubuntu/jars/pro/note-service-25.04.10.jar --server.port=8081" --name note-service-25.04.10    

//      废弃  运行在  腾讯云   上   114.132.189.139
pm2 start "java -jar /home/ubuntu/jars/pro/snowflakeID-service-25.04.10.jar --server.port=8082" --name snowflakeID-service-25.04.10   




sudo lsof -i :80
