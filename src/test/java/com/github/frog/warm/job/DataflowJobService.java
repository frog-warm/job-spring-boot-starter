package com.github.frog.warm.job;

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
