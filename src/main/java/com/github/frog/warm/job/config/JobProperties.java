package com.github.frog.warm.job.config;

/**
 * 任务执行配置
 *
 * @author tuzy
 */
public class JobProperties {
    /**
     * 监控作业运行时状态
     * 每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。
     * 每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。
     */
    private boolean monitorExecution = true;

    /**
     * 最大允许的本机与注册中心的时间误差秒数
     * 如果时间误差超过配置秒数则作业启动时将抛异常
     * 配置为-1表示不校验时间误差
     */
    private int maxTimeDiffSeconds;

    /**
     * 作业监控端口
     * 建议配置作业监控端口, 方便开发者dump作业信息。
     * 使用方法: echo “dump” | nc 127.0.0.1 9888
     */
    private int monitorPort;

    /**
     * 作业分片策略实现类全路径
     * 默认使用平均分配策略
     * 详情参见：作业分片策略
     * http://elasticjob.io/docs/elastic-job-lite/02-guide/job-sharding-strategy
     */
    private String jobShardingStrategyClass;

    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复
     * 单位：分钟
     */
    private int reconcileIntervalMinutes;

    /**
     * 设置作业是否启动时禁止.
     *
     * <p>
     * 可用于部署作业时, 先在启动时禁止, 部署结束后统一启动.
     * </p>
     */
    private boolean disabled;

    /**
     * 设置本地配置是否可覆盖注册中心配置.
     *
     * <p>
     * 如果可覆盖, 每次启动作业都以本地配置为准.
     * </p>
     */
    private boolean overwrite = true;

    public JobProperties() {
    }

    public boolean isMonitorExecution() {
        return this.monitorExecution;
    }

    public int getMaxTimeDiffSeconds() {
        return this.maxTimeDiffSeconds;
    }

    public int getMonitorPort() {
        return this.monitorPort;
    }

    public String getJobShardingStrategyClass() {
        return this.jobShardingStrategyClass;
    }

    public int getReconcileIntervalMinutes() {
        return this.reconcileIntervalMinutes;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean isOverwrite() {
        return this.overwrite;
    }

    public void setMonitorExecution(boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
        this.maxTimeDiffSeconds = maxTimeDiffSeconds;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }

    public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
        this.jobShardingStrategyClass = jobShardingStrategyClass;
    }

    public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
        this.reconcileIntervalMinutes = reconcileIntervalMinutes;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String toString() {
        return "JobProperties(monitorExecution=" + this.monitorExecution + ", maxTimeDiffSeconds=" + this.maxTimeDiffSeconds + ", monitorPort=" + this.monitorPort + ", jobShardingStrategyClass=" + this.jobShardingStrategyClass + ", reconcileIntervalMinutes=" + this.reconcileIntervalMinutes + ", disabled=" + this.disabled + ", overwrite=" + this.overwrite + ")";
    }
}
