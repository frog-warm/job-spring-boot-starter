package com.github.frog.warm.job.test;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.github.frog.warm.job.annotation.JobService;
import com.github.frog.warm.job.api.DataflowJob;
import com.github.frog.warm.job.api.SimpleJob;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.HashMap;
import java.util.List;

/**
 * job测试类
 *
 * @author tuzy
 */
@SpringBootTest
public class BaseJobTest {

    static {
        // 测试不自动注册到zk，改为手动执行
        System.setProperty("job.enable", "false");
    }

    protected void testJob(SimpleJob job) {
        JobService jobService = getJobService(job);
        for (int i = 0; i < jobService.shardingTotalCount(); i++) {
            job.execute(new ShardingContext(getShardingContext(jobService), i));
        }
    }

    protected <T> void testJob(DataflowJob<T> job) {
        JobService jobService = getJobService(job);
        for (int i = 0; i < jobService.shardingTotalCount(); i++) {
            ShardingContext shardingContext = new ShardingContext(getShardingContext(jobService), i);
            List<T> data = job.fetchData(shardingContext);
            job.processData(shardingContext, data);
        }
    }

    private JobService getJobService(ElasticJob job) {
        JobService jobService = AnnotationUtils.findAnnotation(AopUtils.getTargetClass(job), JobService.class);
        if (jobService == null) {
            throw new RuntimeException("未发现JobService注解");
        }
        return jobService;
    }

    private ShardingContexts getShardingContext(JobService jobService) {
        return new ShardingContexts("local-test-0", jobService.desc(), jobService.shardingTotalCount(),
                jobService.jobParameter(), new HashMap<>());
    }
}
