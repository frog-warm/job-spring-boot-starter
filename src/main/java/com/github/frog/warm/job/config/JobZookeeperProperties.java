package com.github.frog.warm.job.config;

/**
 * zk配置
 *
 * @author tuzy
 */
public class JobZookeeperProperties {
    /**
     * 连接Zookeeper服务器的列表.
     * 包括IP地址和端口号.
     * 多个地址用逗号分隔.
     * 如: host1:2181,host2:2181
     */
    private String server;

    /**
     * 命名空间.
     */
    private String namespace = "elastic-job";

    /**
     * 等待重试的间隔时间的初始值.
     * 单位毫秒.
     */
    private int baseSleepTimeMilliseconds = 1000;

    /**
     * 等待重试的间隔时间的最大值.
     * 单位毫秒.
     */
    private int maxSleepTimeMilliseconds = 3000;

    /**
     * 最大重试次数.
     */
    private int maxRetries = 3;

    /**
     * 会话超时时间.
     * 单位毫秒.
     */
    private int sessionTimeoutMilliseconds;

    /**
     * 连接超时时间.
     * 单位毫秒.
     */
    private int connectionTimeoutMilliseconds;

    /**
     * 连接Zookeeper的权限令牌.
     * 缺省为不需要权限验证.
     */
    private String digest;

    public JobZookeeperProperties() {
    }

    public String getServer() {
        return this.server;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int getBaseSleepTimeMilliseconds() {
        return this.baseSleepTimeMilliseconds;
    }

    public int getMaxSleepTimeMilliseconds() {
        return this.maxSleepTimeMilliseconds;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public int getSessionTimeoutMilliseconds() {
        return this.sessionTimeoutMilliseconds;
    }

    public int getConnectionTimeoutMilliseconds() {
        return this.connectionTimeoutMilliseconds;
    }

    public String getDigest() {
        return this.digest;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }


    public String toString() {
        return "JobZookeeperProperties(server=" + this.server + ", namespace=" + this.namespace + ", baseSleepTimeMilliseconds=" + this.baseSleepTimeMilliseconds + ", maxSleepTimeMilliseconds=" + this.maxSleepTimeMilliseconds + ", maxRetries=" + this.maxRetries + ", sessionTimeoutMilliseconds=" + this.sessionTimeoutMilliseconds + ", connectionTimeoutMilliseconds=" + this.connectionTimeoutMilliseconds + ", digest=" + this.digest + ")";
    }
}
