package com.github.frog.warm.job.test;

import com.github.frog.warm.job.SimpleJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 *
 * @author tuzy
 */
public class SimpleJobJobTest extends BaseJobTest {
    SimpleJobService simpleJobService;

    @Test
    public void test() {
        testJob(simpleJobService);
    }

    @Autowired
    public void setSimpleJobService(SimpleJobService simpleJobService) {
        this.simpleJobService = simpleJobService;
    }
}
