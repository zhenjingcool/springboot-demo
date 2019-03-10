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
    注意：pom中要添加依赖,不然不会去远程仓库读取配置文件。
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


2、服务注册与发现
服务注册与发现对于微服务系统来说非常重要。有了服务发现与注册，你就不需要整天改服务调用的配置文件了，你只需要使用服务的标识符，就可以访问到服务。
所有的服务端及访问服务的客户端都需要连接到注册管理器（eureka服务器）。服务在启动时会自动注册自己到eureka服务器，每一个服务都有一个名字，这个名字会被
注册到eureka服务器。使用服务的一方只需要使用该名字加上方法名就可以调用到服务。
2.1、eureka注册服务器
（1）、创建eureka注册服务器cloud-eureka-server
    application.properties,eureka.client.serviceUrl.defaultZone为设置eureka服务器所在的地址，查询服务和注册服务都需要依赖这个地址。
        server.port=8761
        eureka.instance.hostname=localhost
        eureka.client.registerWithEureka=false
        eureka.client.fetchRegistry=false
        eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
        spring.application.name=cloud-eureka-server
    pom文件，增加starter
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
    client端配置，以cloud-simple-services为例，需要注册到服务端，客户端只需pom中增加，且启动类增加@EnableDiscoveryClient注解。
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
    启动服务端，再启动客户端，当客户端在启动日志中打印出DiscoveryClient_CLOUD-SIMPLE-SERVICES/PC201606262118:cloud-simple-services:8081 - registration status: 204时，说明服务已注册到注册服务器，
    同时注册服务器后台日志会打印出Registered instance CLOUD-SIMPLE-SERVICES/PC201606262118:cloud-simple-services:8081 with status UP (replication=false)
    
    
3、简单应用cloud-simple-ui，使用了配置管理服务器、注册服务器、断路器、负载均衡器


4、thrift、zookeeper建立微服务(https://www.cnblogs.com/skyblog/p/5535418.html)
4.1、Thrift是一个跨语言的服务部署框架，通过IDL（Interface Definition Language，接口定义语言）来定义RPC接口，然后通过thrift编译器
生成不同语言的代码，并由生成的代码负责RPC协议层和传输层的实现。
4.2、定义接口(.thrift文件)，并生成接口代码
    （1）、下载thrift-0.9.2.tar.gz，解压到C:/thrift，运行thrift.exe --gen java D:\XXX\UserService.thrift,生成接口代码，拷贝到
        cloud-thrift-interface工程下面。
4.3、搭建zookeeper集群。下载zookeeper-3.4.13.tar.gz，解压，conf目录下创建zookeeper配置文件zoo.cfg，设置心跳间隔tickTime，监听的客户端端
口clientPort等信息，启动zookeeper。
4.4、服务端service，cloud-thrift-server
    （1）、UserServiceImpl实现接口，并实现接口中的方法，服务提供
    （2）、ThriftConfig，把实现类注册进Thrift服务器
    （3）、ZooKeeperConfig，将服务实例注册进zookeeper，zookeeper集群节点通过zookeeper.server.list设置，这里只有一个节点
4.5、客户端service，cloud-thrift-client，客户端需要及时监听服务列表的变化，并作出负载均衡