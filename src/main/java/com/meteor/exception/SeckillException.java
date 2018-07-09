package com.meteor.exception;

/**
 * 秒杀相关的异常；可以作为其他异常的父类
 * @Author: meteor @Date: 2018/7/8 13:43
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
