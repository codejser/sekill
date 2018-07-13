package com.meteor.service;

import com.meteor.dto.Exposer;
import com.meteor.dto.SeckillExecution;
import com.meteor.entity.Seckill;
import com.meteor.exception.RepeatKillException;
import com.meteor.exception.SeckillCloseException;
import com.meteor.exception.SeckillException;

import java.util.List;

/**
 * 秒杀的Service层业务接口
 * 站在“使用者”的角度去设计接口
 *三个方面：方法定义的粒度；参数，返回的类型
 * @Author: meteor @Date: 2018/7/8 13:12
 */

public interface ISeckillService {


    /**
     * 查询所有的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();


    /**
     * 根据Id查询秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    /**
     * 暴露秒杀地址的方法
     * 参数是秒杀商品的Id
     * 返回的类型是封装的Dto类：对应一系列的属性和构造器
     * 秒杀开启时输出秒杀地址
     * 否则输出系统当前的时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * 执行秒杀动作的方法
     * 参数包括商品的id,秒杀者对应的电话号码以及暴漏秒杀地址后传来的MD5(用来验证)
     * 返回的类型是封装了成功或者失败信息的Dto类：主要包括状态码和状态信息；然后用构造器去封装
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;


    /**
     * 执行秒杀动作的方法(存储过程)
     * 参数包括商品的id,秒杀者对应的电话号码以及暴漏秒杀地址后传来的MD5(用来验证)
     * 返回的类型是封装了成功或者失败信息的Dto类：主要包括状态码和状态信息；然后用构造器去封装
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;
}
