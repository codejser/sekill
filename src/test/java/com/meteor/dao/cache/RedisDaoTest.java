package com.meteor.dao.cache;

import com.meteor.BaseTest;
import com.meteor.dao.SeckillDao;
import com.meteor.entity.Seckill;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: meteor @Date: 2018/7/10 21:20
 */
public class RedisDaoTest extends BaseTest {

    private long id = 1001;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        //存储和获取放在同一个方法里测试
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null){
            seckill = seckillDao.queryById(id);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                Seckill seckill1 = redisDao.getSeckill(seckill.getSeckillId());
                System.out.println(seckill);
            }
        }
    }


}