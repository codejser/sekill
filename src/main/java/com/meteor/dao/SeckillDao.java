package com.meteor.dao;

import com.meteor.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: meteor @Date: 2018/7/7 16:37
 */
public interface SeckillDao {

    /**
     * 减少库存的方法
     */

    int reduceNum(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    //根据id查询秒杀商品

    Seckill queryById(long seckillId);

    //查询秒杀商品的列表

    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
