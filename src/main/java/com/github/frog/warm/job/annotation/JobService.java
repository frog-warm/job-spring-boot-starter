package com.github.frog.warm.job.annotation;

import com.dangdang.ddframe.job.executor.handler.ExecutorServiceHandler;
import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;
import com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler;
import com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler;
import com.github.frog.warm.job.JobExecutorServiceHandler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定时任务注解
 *
 * @author tuzy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface JobService {

    /**
     * 作业描述信息
     */
    String desc();

    /**
     * 作业启动时间的cron表达式
     * 在线表达式 https://cron.qqe2.com/
     */
    String cron();

    /**
     * 作业分片总数
     */
    int shardingTotalCount() default 1;

    /**
     * 设置分片序列号和个性化参数对照表.
     *
     * <p>
     * 分片序列号和参数用等号分隔, 多个键值对用逗号分隔. 类似map.
     * 分片序列号从0开始, 不可大于或等于作业分片总数.
     * 如:
     * 0=a,1=b,2=c
     * </p>
     */
    String shardingItemParameters() default "";

    /**
     * 设置作业自定义参数.
     *
     * <p>
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     * </p>
     */
    String jobParameter() default "";

    /**
     * 设置是否开启失效转移.
     *
     * <p>
     * 只有对monitorExecution的情况下才可以开启失效转移.
     * </p>
     */
    boolean failover() default false;

    /**
     * 是否开启错过任务重新执行.
     * 详情参考Quartz misfire处理机制
     */
    boolean misfire() default false;



    /**
     * 作业异常处理器
     */
    Class<? extends JobExceptionHandler> exceptionHandler() default DefaultJobExceptionHandler.class;

    /**
     * 线程池服务处理器
     */
    Class<? extends ExecutorServiceHandler> executorServiceHandler() default JobExecutorServiceHandler.class;

    /**
     * DataflowJob配置
     * 是否流式处理数据
     * 如果流式处理数据, 则fetchData不返回空结果将持续执行作业
     * 如果非流式处理数据, 则处理数据完成后作业结束
     */
    boolean streamingProcess() default false;
}
