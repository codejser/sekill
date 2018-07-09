package com.meteor.dto;

import com.meteor.entity.SuccessSeckill;
import com.meteor.enums.SeckillEnum;

/**
 * 封装秒杀动作执行后返回的状态信息；包括状态码和对应的状态信息
 * @Author: meteor @Date: 2018/7/8 13:32
 */
public class SeckillExecution {

    //秒杀商品的Id
    private long seckillId;
    //秒杀后的状态码
    private int state;
    //状态码对应的信息
    private String stateInfo;

    //秒杀成功返回的对象
    private SuccessSeckill successSeckill;


    //成功秒杀的构造器

    public SeckillExecution(long seckillId, SeckillEnum seckillEnum, SuccessSeckill successSeckill) {
        this.seckillId = seckillId;
        this.state = seckillEnum.getState();
        this.stateInfo = seckillEnum.getStateInfo();
        this.successSeckill = successSeckill;
    }

    //秒杀失败的构造器

    public SeckillExecution(long seckillId, SeckillEnum seckillEnum) {
        this.seckillId = seckillId;
        this.state = seckillEnum.getState();
        this.stateInfo = seckillEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessSeckill getSuccessSeckill() {
        return successSeckill;
    }

    public void setSuccessSeckill(SuccessSeckill successSeckill) {
        this.successSeckill = successSeckill;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successSeckill=" + successSeckill +
                '}';
    }
}
