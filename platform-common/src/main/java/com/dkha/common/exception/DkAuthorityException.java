package com.dkha.common.exception;

/**
 * @Author Spring
 * @Since 2019/9/21 11:58
 * @Description 权限相关异常--主要用于权限拦截器
 *
 */

public class DkAuthorityException extends RuntimeException {

    public DkAuthorityException(String s) {
        super(s);
    }

    public DkAuthorityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DkAuthorityException(Throwable throwable) {
        super(throwable);
    }

    public DkAuthorityException() {

    }
}
