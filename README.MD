## 项目架构
![image](https://github.com/clay4444/CallLogCollectSystem/Show/CallLogCollectSystem.png)

## 模拟集群环境

Hadoop (HA) , 	mini1,mini2,mini3,mini4

Yarn (HA) ,		mini1,mini2,mini3,mini4

JournalNode		mini2,mini3,mini4

Zookeeper,  		mini1,mini2,mini3
Hbase,  			mini1,mini2,mini3,mini4	 HA只需要在mini6上用hbase-daemon.sh start master 命令即可

Flume,  			mini1
Kafka,  			mini2,mini3,mini4

Hive,  			~

数据模拟程序		mini1

------------------------------------------------------

mini1 (2G)：

~~~
DFSZKFailoverController		(ZKFC     Hadoop HA)
NameNode				
HMaster					   (Hbase)
QuorumPeerMain			    (Zookeeper)
ResourceManager				(Yarn)
~~~

mini2 (2G)：

~~~
QuorumPeerMain			     (Zookeeper)
JournalNode					(Hadoop HA)
HRegionServer				(Hbase)
NodeManager					(Yarn)
Kafka
DataNode					(Hadoop)
~~~

mini3 (2G)：

~~~
JournalNode					(Hadoop HA)
NodeManager					(Yarn)
QuorumPeerMain			     (Zookeeper)
DataNode					(Hadoop)
HRegionServer				(Hbase)
Kafka
~~~

mini4 (2G)：

~~~
Kafka
NodeManager					(Yarn)
DataNode					(Hadoop)
HRegionServer				(Hbase)
JournalNode					(Hadoop HA)
~~~

mini6 (2G)：

~~~
DFSZKFailoverController		(ZKFC     Hadoop HA)
ResourceManager				(Yarn)
NameNode
~~~



## 启动zookeeper集群(3台)  mini1,mini2,mini3

~~~
zkServer.sh start 
~~~



## 预先启动Hadoop(HA), Yarn(HA)

~~~
start-all.sh
~~~



## 部署数据生成程序(mini1)

把CallLogGen 模块下的calllog文件夹传送到节点mini1下 ( CallLog.jar,calllog.log,  calllog.sh )



## 注册Hbase协处理器

maven  编译CallLogCoproessor模块, 生成jar包

把jar包上传到Hbase安装目录的lib目录下

配置Hbase-site.xml,  添加如下内容

~~~
<!-- 协处理器 -->
<property>
        <name>hbase.coprocessor.region.classes</name>
        <value>org.clay.calllog.coprossor.CallLogRegionObserver</value>
</property>
~~~



## 启动Hbase

~~~
start-hbase.sh
~~~



## 创建Hbase表

~~~
create 'ns1:calllogs' , 'f1','f2'
~~~



## 启动kafka,(mini2,mini3,mini4)创建名为calllog的topic

~~~
cd /soft/kafka/config

kafka-server-start.sh -daemon server.properties			#启动kafka (mini2,mini3,mini4)

kafka-topics.sh --zookeeper mini1:2181 --topic calllog --create --replication-factor 3 --partitions 4										#创建主题
~~~



## 启动Hbase消费者，从kafka中提取数据，存入Habse中

> 把CallLogConsumer 模块下的kafka-consumer文件夹上传到mini1节点上。

> 给run-kafkaconsumer.sh 添加执行权限，然后执行。

> HbaseConsumer会自动从kafka提取数据，放入Hbase数据库中。



## 启动flume收集程序，收集数据生成程序一直在写入的log文件

flume的配置文件

~~~
a1.sources = r1
a1.sinks = k1
a1.channels = c1

a1.sources.r1.type=exec
#-F 最后10行,如果从头开始收集 -c +0 -F:持续收集后续数据,否则进程停止。
a1.sources.r1.command=tail -F -c +0 /home/hadoop/calllog/calllog.log

a1.channels.c1.type=memory

a1.sinks.k1.type = org.apache.flume.sink.kafka.KafkaSink
a1.sinks.k1.kafka.topic = calllog
a1.sinks.k1.kafka.bootstrap.servers = mini2:9092 mini3:9092 mini4:9092
a1.sinks.k1.kafka.flumeBatchSize = 20
a1.sinks.k1.kafka.producer.acks = 1

a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
~~~

启动flume进行收集

~~~
flume-ng agent -f /home/hadoop/soft/flume-1.7.0/conf/calllog.conf -n a1 &
~~~



## 启动数据生成程序   生成记录

~~~
>mini1:      cd calllog/
>mini1:     ./calllog.sh      [执行之前需要预先添加执行权限]
~~~



##  查看Hbase中的数据

~~~
scan 'ns1:calllogs'
~~~





## 部署WebVisual  程序查看可视化

~~~
localhost:8080/callLog/toFindCallLogPage
~~~

根据手机号码和起止时间查询