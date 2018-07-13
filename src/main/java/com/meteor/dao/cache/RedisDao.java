package com.meteor.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.meteor.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 缓存接口，负责缓存相关数据的键值对
 * @Author: meteor @Date: 2018/7/10 20:35
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //声明缓存池
    private  JedisPool jedisPool;
    //对应的构造方法：方便去注入到Spring的容器
    public RedisDao(String ip ,int port){
        jedisPool = new JedisPool(ip,port);
    }


    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    /**
     * 秒杀优化的方法是将从数据库中查找对应Id的秒杀商品缓存到Redis中
     * 缓存的键值对是SeckillId-->Seckill
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(Long seckillId){
        try{
            //获取Jedis缓存对象资源
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:" + seckillId;
                //根据键获取在Redis中对应的二进制数组
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        //序列化的过程:把缓存中没有的对象存储起来
        try{
            //获取Jedis缓存对象资源
            Jedis jedis = jedisPool.getResource();
            try{
               //先获取缓存中的键值：即秒杀商品的ID
                String key = "seckill:" + seckill.getSeckillId();
                //生成二进制数组
                byte[] bytes =  ProtostuffIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            } finally {
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
