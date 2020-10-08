package com.github.frog.warm.job;

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
