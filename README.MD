## 项目架构
![image](https://github.com/clay4444/CallLogCollectSystem/blob/master/Show/CallLogCollectSystem.png)

## 模拟集群环境

Hadoop (HA)，----------------------->mini1,mini2,mini3,mini4

Yarn (HA)，----------------------->mini1,mini2,mini3,mini4

JournalNode，----------------------->mini2,mini3,mini4

Zookeeper，----------------------->mini1,mini2,mini3   

Hbase，----------------------->mini1,mini2,mini3,mini4	   HA只需要在mini6上用hbase-daemon.sh start master 命令即可

Flume，----------------------->mini1

Kafka，----------------------->mini2,mini3,mini4

Hive，----------------------->mini1

数据模拟程序，----------------------->mini1

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



## 首先把start-scripts 文件夹下的启动脚本都放到mini1的/usr/local/bin上

~~~
[/usr/local/bin/xkill.sh]
[/usr/local/bin/xcall.sh]
[/usr/local/bin/xzk-cluster-start.sh]
[/usr/local/bin/xkafka-cluster-start.sh]
[/usr/local/bin/xconsumer-start.sh]
[/usr/local/bin/xflume-calllog-start.sh]
[/usr/local/bin/xgendata-start.sh]
~~~



## 启动zookeeper集群(3台)  mini1,mini2,mini3

~~~
xzk-cluster-start.sh
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
xkafka-cluster-start.sh

kafka-topics.sh --zookeeper mini1:2181 --topic calllog --create --replication-factor 3 --partitions 4										#创建主题
~~~



## 启动Hbase消费者，从kafka中提取数据，存入Habse中

> 把CallLogConsumer 模块下的kafka-consumer文件夹上传到mini1节点上。

~~~
xconsumer-start.sh
~~~



## 启动flume收集程序，收集数据生成程序一直在写入的log文件

flume的配置文件

~~~
a1.sources = r1
a1.sinks = k1
a1.channels = c1

a1.sources.r1.type=exec
#-F 最后10行,如果从头开始收集 -c +0 -F:持续收集后续数据,否则进程停止。
a1.sources.r1.command=tail -F -c +0 /home/hadoop/calllog/callLog.log

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
xflume-calllog-start.sh
~~~



## 部署WebVisual，打开lcoalhost:8080/callLog/findAll  

> jsp 页面通过ajax请求每隔两秒刷新一次页面





## 启动数据生成程序   生成记录

~~~
xgendata-start.sh
~~~



##  jsp页面实时刷新最新的记录

~~~
~
~
~
~~~



-----------------------------------------------------------------------------------------------------

## 系统其他功能

1. 根据手机号码和起止时间查询
~~~
localhost:8080/callLog/toFindCallLogPage
~~~

2. 查询某个手机号最新的一条记录
~~~
准备工作：hive通过外部表的方式整合Hbase（防止数据丢失）
建表语句
-----------------------------------------
$hive>create database mydb ;
$hive>use mydb ;
$hive>create external table ext_calllogs_in_hbase(id string, caller string,callTime string,callee string,callDuration string) STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,f1:caller,f1:callTime,f1:callee,f1:callDuration") TBLPROPERTIES ("hbase.table.name" = "ns1:calllogs");

测试：$hive>select * from ext_calllogs_in_hbase;
---------------------------------------------
在mini1上启动hiveserver2  [jdbc协议]
进程名：RunJar        如果一次失败之后需要杀死重启
-----------------------------------------------------
$>hive/bin/hiveserver2 &
------------------------------------------------
部署WebVisual
地址：localhost:8080/callLog/toFindLatestCallLog
~~~

3.实现指定手机号指定年份各个月份的通话次数，并利用echarts实现可视化

~~~
准备工作：hive通过外部表的方式整合Hbase（防止数据丢失）
建表语句
-----------------------------------------
$hive>create database mydb ;
$hive>use mydb ;
$hive>create external table ext_calllogs_in_hbase(id string, caller string,callTime string,callee string,callDuration string) STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,f1:caller,f1:callTime,f1:callee,f1:callDuration") TBLPROPERTIES ("hbase.table.name" = "ns1:calllogs");

测试：$hive>select * from ext_calllogs_in_hbase;
---------------------------------------------
在mini1上启动hiveserver2  [jdbc协议]
进程名：RunJar        如果一次失败之后需要杀死重启
-----------------------------------------------------
$>hive/bin/hiveserver2 &
------------------------------------------------
部署WebVisual
地址：localhost:8080/callLog/toStatCallLog
~~~



成功截图

![image](https://github.com/clay4444/CallLogCollectSystem/blob/master/Show/success.png)



### 注释

~~~
为了开发的简便，所有使用hive的地方，都是使用jdbc直接链接的hiveserver2，这样难免造成了这些请求的速度很慢，生产环境中争取的做法是利用crontab或者Azkaban 跑定时的job，结果通过sqoop导入到MySql，查询的时候直接请求MySql已经处理完的结果， 这也是一般离线大数据处理的一般思路。
~~~

