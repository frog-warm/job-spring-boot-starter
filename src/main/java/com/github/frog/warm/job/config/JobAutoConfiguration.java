package com.github.frog.warm.job.config;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * job自动配置
 *
 * @author tuzy
 */
@Configuration
@ConditionalOnProperty(value = "job.enable", matchIfMissing = true)
public class JobAutoConfiguration {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JobAutoConfiguration.class);

    @Bean
    @ConfigurationProperties(prefix = "job")
    public JobProperties jobProperties() {
        return new JobProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "job.zookeeper")
    public JobZookeeperProperties jobZookeeperProperties() {
        return new JobZookeeperProperties();
    }

    @Bean
    @ConditionalOnMissingBean(CoordinatorRegistryCenter.class)
    public CoordinatorRegistryCenter coordinatorRegistryCenter(JobZookeeperProperties prop) {
        ZookeeperConfiguration config = new ZookeeperConfiguration(prop.getServer(), prop.getNamespace());
        config.setBaseSleepTimeMilliseconds(prop.getBaseSleepTimeMilliseconds());
        config.setMaxSleepTimeMilliseconds(prop.getMaxSleepTimeMilliseconds());
        config.setMaxRetries(prop.getMaxRetries());
        config.setSessionTimeoutMilliseconds(prop.getSessionTimeoutMilliseconds());
        config.setConnectionTimeoutMilliseconds(prop.getConnectionTimeoutMilliseconds());
        config.setDigest(prop.getDigest());
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(config);
        regCenter.init();
        return regCenter;
    }

    @Bean
    public JobServiceBeanPostProcessor jobServiceBeanPostProcessor(JobProperties jobProperties, CoordinatorRegistryCenter coordinatorRegistryCenter, @Autowired(required = false) JobEventConfiguration jobEventConfiguration) {
        log.info("init JobServiceBeanPostProcessor");
        return new JobServiceBeanPostProcessor(jobProperties, coordinatorRegistryCenter, jobEventConfiguration);
    }
}
