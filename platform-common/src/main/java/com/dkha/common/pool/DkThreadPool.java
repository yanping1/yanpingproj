package com.dkha.common.pool;


import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author Spring
 * @Since 2019/8/15 11:55
 * @Description 系统线程池
 */
public class DkThreadPool {

    /**
     * 不要手动设置线程池并发线程数量，应根据线程所执行的任务是IO型任务还是CPU密集计算型任务进行设置，粗略设置如下
     * 如果是CPU密集型应用，则线程池大小设置为N+1
     * 如果是IO密集型应用，则线程池大小设置为2N+1
     * 线程IO时间占整个执行时间比例越高，则可以适当放大并发数，反之适当减少并发数量
     */
    public static ScheduledExecutorService  executorService = new ScheduledThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors() + 1,
            new BasicThreadFactory.Builder().namingPattern("async-face-pool-%d").daemon(true).build());

}
