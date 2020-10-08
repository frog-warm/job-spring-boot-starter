package com.github.frog.warm.job.test;

import com.github.frog.warm.job.DataflowJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试
 *
 * @author tuzy
 */
public class DataflowJobJobTest extends BaseJobTest {
    DataflowJobService dataflowJobService;

    @Test
    public void test() {
        testJob(dataflowJobService);
    }

    @Autowired
    public void setDataflowJobService(DataflowJobService dataflowJobService) {
        this.dataflowJobService = dataflowJobService;
    }
}
