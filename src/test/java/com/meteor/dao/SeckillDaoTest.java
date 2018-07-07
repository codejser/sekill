package com.meteor.dao;

import com.meteor.BaseTest;
import com.meteor.entity.Seckill;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Author: meteor @Date: 2018/7/7 22:24
 */
public class SeckillDaoTest extends BaseTest{
    @Autowired
    private SeckillDao seckillDao;


    @Test
    public void testreduceNum(){
        Date date = new Date();
        int effectNum = seckillDao.reduceNum(1000,date);
        System.out.println(effectNum);
    }


    @Test
    public void testqueryById(){
        long seckillId = 1000;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void testqueryAll(){
        List<Seckill> seckillList = seckillDao.queryAll(0,4);
        System.out.println(seckillList.get(3).getName());
    }
}
