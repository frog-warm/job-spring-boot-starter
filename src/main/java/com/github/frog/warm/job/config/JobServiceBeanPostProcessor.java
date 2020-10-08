package com.github.frog.warm.job.config;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.github.frog.warm.job.annotation.JobService;
import com.github.frog.warm.job.api.DataflowJob;
import com.github.frog.warm.job.api.SimpleJob;
import org.slf4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Objects;

/**
 * job注册
 *
 * @author tuzy
 */
public class JobServiceBeanPostProcessor implements BeanPostProcessor {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JobServiceBeanPostProcessor.class);
    private final CoordinatorRegistryCenter regCenter;
    private final JobEventConfiguration jobEventConfiguration;
    private final JobProperties jobProperties;

    public JobServiceBeanPostProcessor(JobProperties jobProperties, CoordinatorRegistryCenter coordinatorRegistryCenter, JobEventConfiguration jobEventConfiguration) {
        this.regCenter = coordinatorRegistryCenter;
        this.jobEventConfiguration = jobEventConfiguration;
        this.jobProperties = jobProperties;
    }

    /**
     * bean初始化完成后调用
     *
     * @param bean     对象
     * @param beanName 名称
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        JobService jobService = AnnotationUtils.findAnnotation(AopUtils.getTargetClass(bean), JobService.class);
        if (jobService != null) {
            this.registerJob(beanName, bean, jobService);
        }
        return bean;
    }

    private void registerJob(String beanName, Object bean, JobService jobService) {
        String className = AopUtils.getTargetClass(bean).getCanonicalName();
        JobCoreConfiguration.Builder coreConfig = JobCoreConfiguration//
                .newBuilder(beanName, jobService.cron(), jobService.shardingTotalCount())//
                .failover(jobService.failover())//
                .misfire(jobService.misfire())//
                .jobProperties(
                        com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER
                                .getKey(), jobService.exceptionHandler().getCanonicalName())//
                .jobProperties(
                        com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER
                                .getKey(), jobService.executorServiceHandler().getCanonicalName());
        if (jobService.shardingItemParameters().length() > 0) {
            coreConfig.shardingItemParameters(jobService.shardingItemParameters());
        }
        if (jobService.jobParameter().length() > 0) {
            coreConfig.jobParameter(jobService.jobParameter());
        }
        if (jobService.desc().length() > 0) {
            coreConfig.description(jobService.desc());
        }
        LiteJobConfiguration.Builder liteJobConfig;
        if (bean instanceof SimpleJob) {
            SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(coreConfig.build(), className);
            liteJobConfig = LiteJobConfiguration.newBuilder(simpleJobConfig);
        } else if (bean instanceof DataflowJob) {
            DataflowJobConfiguration flowJobConf =
                    new DataflowJobConfiguration(coreConfig.build(), className, jobService.streamingProcess());
            liteJobConfig = LiteJobConfiguration.newBuilder(flowJobConf);
        } else {
            throw new RuntimeException(
                    "job bean must extends from com.github.frog.warm.job.api.* . Unsupported this class:" + bean.getClass());
        }
        liteJobConfig.monitorExecution(jobProperties.isMonitorExecution());
        if (jobProperties.getMaxTimeDiffSeconds() != 0) {
            liteJobConfig.maxTimeDiffSeconds(jobProperties.getMaxTimeDiffSeconds());
        }
        if (jobProperties.getMonitorPort() != 0) {
            liteJobConfig.monitorPort(jobProperties.getMonitorPort());
        }
        if (jobProperties.getReconcileIntervalMinutes() != 0) {
            liteJobConfig.reconcileIntervalMinutes(jobProperties.getReconcileIntervalMinutes());
        }
        if (jobProperties.getJobShardingStrategyClass() != null) {
            liteJobConfig.jobShardingStrategyClass(jobProperties.getJobShardingStrategyClass());
        }
        liteJobConfig.disabled(jobProperties.isDisabled());
        liteJobConfig.overwrite(jobProperties.isOverwrite());
        log.info("init {}({})", jobService.desc(), className);
        if (Objects.nonNull(jobEventConfiguration)) {
            new SpringJobScheduler((ElasticJob) bean, regCenter, liteJobConfig.build(), jobEventConfiguration).init();
        } else {
            new SpringJobScheduler((ElasticJob) bean, regCenter, liteJobConfig.build()).init();
        }
    }
}
