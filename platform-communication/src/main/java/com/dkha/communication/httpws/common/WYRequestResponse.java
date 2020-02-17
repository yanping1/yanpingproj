package com.dkha.communication.httpws.common;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CountDownLatch;

/**
 * @version V1.0
 * @Description: 请求和数据结果
 * @Title:
 * @Package com.dkha.communication.httpws.common
 * @author: huangyugang
 * @date: 2019/11/29 18:29
 * @Copyright: 成都电科慧安
 */
@Data
public class WYRequestResponse {
    private String serid;
    private HttpServletResponse response;
    private CountDownLatch countDownLatch=new CountDownLatch(1);

}
