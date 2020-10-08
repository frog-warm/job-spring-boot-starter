# elastic-job-lite springboot自动装配工具 
[elastic-job-lite](https://shardingsphere.apache.org/elasticjob/legacy/lite-2.x/00-overview/intro/) 自定义starter

## 添加pom依赖
```xml
<dependency>
    <groupId>com.github.frog-warm</groupId>
    <artifactId>job-spring-boot-starter</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```
## 添加配置
- 任务配置：com.github.frog.warm.job.config.JobProperties 
- 注册中心：com.github.frog.warm.job.config.JobZookeeperProperties

参考配置：
```json
[
    {
      "name": "job.disabled",
      "type": "java.lang.Boolean",
      "description": "设置作业是否启动时禁止. <p> 可用于部署作业时, 先在启动时禁止, 部署结束后统一启动. <\/p>",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": false
    },
    {
      "name": "job.job-sharding-strategy-class",
      "type": "java.lang.String",
      "description": "作业分片策略实现类全路径 默认使用平均分配策略 详情参见：作业分片策略 http:\/\/elasticjob.io\/docs\/elastic-job-lite\/02-guide\/job-sharding-strategy",
      "sourceType": "com.github.frog.warm.job.config.JobProperties"
    },
    {
      "name": "job.max-time-diff-seconds",
      "type": "java.lang.Integer",
      "description": "最大允许的本机与注册中心的时间误差秒数 如果时间误差超过配置秒数则作业启动时将抛异常 配置为-1表示不校验时间误差",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": 0
    },
    {
      "name": "job.monitor-execution",
      "type": "java.lang.Boolean",
      "description": "监控作业运行时状态 每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。 每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": true
    },
    {
      "name": "job.monitor-port",
      "type": "java.lang.Integer",
      "description": "作业监控端口 建议配置作业监控端口, 方便开发者dump作业信息。 使用方法: echo “dump” | nc 127.0.0.1 9888",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": 0
    },
    {
      "name": "job.overwrite",
      "type": "java.lang.Boolean",
      "description": "设置本地配置是否可覆盖注册中心配置. <p> 如果可覆盖, 每次启动作业都以本地配置为准. <\/p>",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": true
    },
    {
      "name": "job.reconcile-interval-minutes",
      "type": "java.lang.Integer",
      "description": "修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复 单位：分钟",
      "sourceType": "com.github.frog.warm.job.config.JobProperties",
      "defaultValue": 0
    },
    {
      "name": "job.zookeeper.base-sleep-time-milliseconds",
      "type": "java.lang.Integer",
      "description": "等待重试的间隔时间的初始值. 单位毫秒.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": 1000
    },
    {
      "name": "job.zookeeper.connection-timeout-milliseconds",
      "type": "java.lang.Integer",
      "description": "连接超时时间. 单位毫秒.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": 0
    },
    {
      "name": "job.zookeeper.digest",
      "type": "java.lang.String",
      "description": "连接Zookeeper的权限令牌. 缺省为不需要权限验证.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties"
    },
    {
      "name": "job.zookeeper.max-retries",
      "type": "java.lang.Integer",
      "description": "最大重试次数.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": 3
    },
    {
      "name": "job.zookeeper.max-sleep-time-milliseconds",
      "type": "java.lang.Integer",
      "description": "等待重试的间隔时间的最大值. 单位毫秒.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": 3000
    },
    {
      "name": "job.zookeeper.namespace",
      "type": "java.lang.String",
      "description": "命名空间.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": "elastic-job"
    },
    {
      "name": "job.zookeeper.server",
      "type": "java.lang.String",
      "description": "连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties"
    },
    {
      "name": "job.zookeeper.session-timeout-milliseconds",
      "type": "java.lang.Integer",
      "description": "会话超时时间. 单位毫秒.",
      "sourceType": "com.github.frog.warm.job.config.JobZookeeperProperties",
      "defaultValue": 0
    }
  ]
```

## 使用步骤
1. 实现com.github.frog.warm.job.api.SimpleJob或者com.github.frog.warm.job.api.DataflowJob接口
2. 添加@JobService注解

样例：
- Simple类型作业
```java
import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.frog.warm.job.annotation.JobService;
import com.github.frog.warm.job.api.SimpleJob;
import org.slf4j.Logger;

/**
 * 测试简单任务
 *
 * @author tuzy
 */
@JobService(desc = "测试简单任务", cron = "0/30 * * * * ?", shardingTotalCount = 3)
public class SimpleJobService implements SimpleJob {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleJobService.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("分片{}执行", shardingContext.getShardingItem());
    }
}

```

- Dataflow类型作业

```java

import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.frog.warm.job.annotation.JobService;
import com.github.frog.warm.job.api.DataflowJob;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试数据流分布式作业接口
 *
 * @author tuzy
 */
@JobService(desc = "测试数据流分布式作业接口", cron = "0/30 * * * * ?")
public class DataflowJobService implements DataflowJob<DataflowJobService.DataflowJobTestDTO> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DataflowJobService.class);

    /**
     * 这个方法需要根据  @link{ShardingContext.getShardingItem()}来取数据
     *
     * @param shardingContext 分片信息
     * @return 当前分片数据
     */
    @Override
    public List<DataflowJobTestDTO> fetchData(ShardingContext shardingContext) {
        List<DataflowJobTestDTO> data = new ArrayList<>();
        int count = (int) (Math.random() * 5);
        // 根据分片ID返回数据
        switch (shardingContext.getShardingItem()) {
            case 0:
                for (int i = 0; i < count; i++) {
                    data.add(new DataflowJobTestDTO(i, "分片0数据" + i));
                }
                break;
            case 1:
                for (int i = 0; i < count; i++) {
                    data.add(new DataflowJobTestDTO(i, "分片1数据" + i));
                }
                break;
        }
        sleep();
        log.info("分片{}拉取，共{}条数据", shardingContext.getShardingItem(), data.size());
        return data;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<DataflowJobTestDTO> data) {
        log.info("分片{}开始，共{}条数据", shardingContext.getShardingItem(), data.size());
        sleep();
        log.info("分片{}结束，共{}条数据", shardingContext.getShardingItem(), data.size());
    }

    private void sleep() {
        try {
            Thread.sleep((int) (Math.random() * 5 + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class DataflowJobTestDTO {
        private int id;
        private String name;

        public DataflowJobTestDTO(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public DataflowJobTestDTO() {
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String toString() {
            return "DataflowJobService.DataflowJobTestDTO(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}

```
