package com.meteor.exception;

import com.meteor.dto.SeckillExecution;
import com.meteor.entity.Seckill;

/**
 * 重复秒杀的异常
 * @Author: meteor @Date: 2018/7/8 13:41
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
