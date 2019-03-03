spring cloud之旅
1、创建配置管理服务器、实现分布式配置管理应用
1.1、创建配置管理服务器
分布式配置管理应该是分布式系统和微服务应用的第一步。想象一下如果你有几十个服务或应用需要配置，而且每个服务还分为开发、测试、生产等不同维度的配置，
那工作量是相当大的，而且还容易出错。如果能把各个应用的配置信息集中管理起来，使用一套机制或系统来管理，那么将极大的提高系统开发的生产效率，同时也
会提高系统开发环境和生产环境运行的一致性。本例使用github作为配置文件存放仓库，配置管理服务器读取远程配置文件，把它转换为restful接口服务。

（1）、创建配置文件存放仓库cloud-config-repo，并推送到github中（https://github.com/zhenjingcool/springboot-demo.git）
（2）、创建配置管理服务器cloud-config-server，
    application.properties文件，server.port为restful接口开放的端口，spring.cloud.config.server.git.uri为github地址，spring.cloud.config.server.git.searchPaths为配置仓库，
        server.port=8888
        spring.cloud.config.server.git.uri=https://github.com/zhenjingcool/springboot-demo.git
        spring.cloud.config.server.git.searchPaths=springcloudstudy/cloud-config-repo
        spring.application.name=cloud-config-server
    ConfigServerApplication.java，微服务启动类，用@EnableConfigServer激活该应用为配置管理服务器
    pom.xml，要加上依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    要验证配置管理服务器有没有生效，访问http://localhost:8888/simple-service/dev，其中该链接会去读取远端simple-service-dev.properties，返回json数据
（3）、创建一个服务使用该远程配置cloud-simple-services
    注意：pom中要添加依赖,不然会不会去远程仓库读取配置文件。
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
    当spring boot启动时打印出Fetching config from server at: http://127.0.0.1:8888，说明实现远端读取配置文件功能。
    bootstrap.properties配置，spring.cloud.config.uri为配置服务器链接，
        spring.cloud.config.uri=http://127.0.0.1:8888，simple-service-dev.properties决定spring.cloud.config.name和spring.cloud.config.profile的值
        spring.cloud.config.name=simple-service
        spring.cloud.config.profile=dev
        eureka.client.serviceUrl.defaultZone=http\://localhost\:8761/eureka/,http\://zlhost\:8762/eureka/
        spring.application.name=simple-service
        server.port=8081
    代码中把连接mysql信息放到远端，本地读取远端配置连接数据库，查询数据库，并把结果显示在页面上，这里就不介绍了