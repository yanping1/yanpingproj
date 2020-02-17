package com.dkha.common.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 生成32位int UUID
 * 14位的当前系统时间(格式为：yyyyMMddHHmmss) + 当前电脑的IP地址的最后两位 + 当前线程的hashCode的前9位 + 7位的随机数
 * @Author Spring
 * @Since 2019/8/14 10:53
 */

public class IntUUID {

    public static final AtomicInteger shortUUID = new AtomicInteger(0);

    public static int getShortUUID(){
        int current;
        int next;
        do {
            current = shortUUID.get();
            next = current >= 2147483647 ? 0 : current + 1;
        } while(!shortUUID.compareAndSet(current, next));
        return next;
    }

    /**
     * 获取字符串类型UUID
     * @return
     */
    public static synchronized String getStrUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        //System.out.println(IntUUID.getShortUUID());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
    }
}
