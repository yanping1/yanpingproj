package com.dkha.communication.httpws.factory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Spring
 * @Since 2019/11/21 11:29
 * @Description
 */

public class SerIdFactory {

    private static AtomicInteger serIdFactory = new AtomicInteger(0);

    private static int upperBound = 100000;

    public static int getSerId() {
        int serId = serIdFactory.incrementAndGet();
        if (serId > upperBound) {
            serIdFactory.set(0);
            return 0;
        }
        return serId;
    }
}
