package com.meteor.dao;

import com.meteor.entity.SuccessSeckill;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: meteor @Date: 2018/7/7 16:47
 */
public interface SuccessSeckillDao {
    //插入购买的明细

    int insertSuccessSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    //根据ID查询明细表

    SuccessSeckill queryIdWithSeckill(long seckillId);

}
