package com.meteor.dao;

import com.meteor.BaseTest;
import com.meteor.entity.SuccessSeckill;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: meteor @Date: 2018/7/7 23:23
 */
public class SuccessSeckillDaoTest extends BaseTest {

    @Autowired
    private SuccessSeckillDao successSeckillDao;

    @Test
    public void insertSuccessSeckill() {
        int num = successSeckillDao.insertSuccessSeckill(1000,1335687951);
        System.out.println(num);
    }

    @Test
    public void queryIdWithSeckill() {
        SuccessSeckill successSeckill = successSeckillDao.queryIdWithSeckill(1000);
        System.out.println(successSeckill.getSeckill().getName());
    }
}