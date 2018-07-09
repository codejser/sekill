package com.meteor.enums;

/**
 * @Author: meteor @Date: 2018/7/8 14:41
 */
public enum SeckillEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"秒杀重复"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;
    private String stateInfo;

    SeckillEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillEnum stateOf(int index){
        for(SeckillEnum state :values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
