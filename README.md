/info  打印出git版本信息

配置参考
spring:
  cloud:
    stream:
      #指定用kafka stream来作为默认消息中间件
      default-binder: kafka
      kafka:
        #来自KafkaBinderConfigurationProperties
        binder:
          brokers: name87:9094
          zkNodes: name85:2181,name86:2181,name87:2181/kafka0101
          #如果需要传递自定义header信息，需要在此处声明，不然自定义消息头是不会出现在最终消息当中的
#          headers: myType
          configuration:
            auto:
              offset:
                #可以设置原生kafka属性，比如设置新的消费组从最新的offset开始消费
                reset: latest

  cloud:
    stream:
      bindings:
        #springCloudBus topic是默认值可以不配,对应的input output分别是springCloudBusInput,springCloudBusOutput,需要对bus stream定制的话可以配置这两个channel
        springCloudBusInput:
          destination: springCloudBus
          #可以指定消费组，避免每次生成随机group
          group: cloud-bus-testgroup:${spring.application.index}

springcloud bus默认订阅发布的topic是springCloudBus,input channel是springCloudBusInput用来订阅处理消息,output channel是springCloudBusOutput用来发布消息到总线，比如/bus/refresh刷新配置事件

https://blog.csdn.net/xiao_jun_0820/article/details/78115746


Hence, your configuration needs to be:
spring:
   cloud:
     stream:
       bindings:
         springCloudBusOutput:
           destination: testExchange
Here the destination name testExchange is the exchange name not the queue name. To avoid anonymous name in the queue, you can set a group name for inbound channel binding.

spring:
   cloud:
     stream:
       bindings:
         springCloudBusInput:
           destination: testExchange
           group: testQueue

This will make the queue name testExchange.testQueue
https://stackoverflow.com/questions/40119055/spring-cloud-bus-rename-rabbitmq-queues
http://blueskykong.com/2018/02/13/spring-cloud-bus-event/