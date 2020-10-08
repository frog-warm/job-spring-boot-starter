package com.github.frog.warm.job;

import com.dangdang.ddframe.job.executor.handler.ExecutorServiceHandler;
import com.dangdang.ddframe.job.util.concurrent.ExecutorServiceObject;

import java.util.concurrent.ExecutorService;

/**
 * 自定义执行线程池
 * 修改单个任务创建线程池为全局统一使用单一线程池
 *
 * @author tuzy
 */
public final class JobExecutorServiceHandler implements ExecutorServiceHandler {
    private static ExecutorService executor;
    private static final Object lock = new Object();

    @Override
    public ExecutorService createExecutorService(final String jobName) {
        if (executor == null) {
            synchronized (lock) {
                if (executor == null) {
                    executor = new ExecutorServiceObject("job-executor", getThreadSize()).createExecutorService();
                }
            }
        }
        return executor;
    }

    private static int getThreadSize() {
        int size = Runtime.getRuntime().availableProcessors() * 2;
        return Math.max(size, 16);
    }
}