package com.meteor.service.impl;

import com.meteor.dao.SeckillDao;
import com.meteor.dao.SuccessSeckillDao;
import com.meteor.dto.Exposer;
import com.meteor.dto.SeckillExecution;
import com.meteor.entity.Seckill;
import com.meteor.entity.SuccessSeckill;
import com.meteor.enums.SeckillEnum;
import com.meteor.exception.RepeatKillException;
import com.meteor.exception.SeckillCloseException;
import com.meteor.exception.SeckillException;
import com.meteor.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: meteor @Date: 2018/7/8 14:09
 */
@Service
public class SeckillServiceImpl implements ISeckillService {

    //声明日志类
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //md5加密盐值
    private final String salt = "sjhgfjhdsf$%^%^&";
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessSeckillDao successSeckillDao;


    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(false,seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if(nowTime.getTime() > endTime.getTime()
                || nowTime.getTime() < startTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true,md5,seckillId);

    }

    private String getMd5(long seckillId){
        String s = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(s.getBytes());
        return md5;
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //1、验证是否秒杀重复
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }

        //执行秒杀的逻辑：减少库存  +  插入秒杀的明细记录
        Date now = new Date();
        try{
            int updateCount = seckillDao.reduceNum(seckillId,now);
            if(updateCount <= 0){
                //此时表示秒杀失败
                throw new SeckillCloseException("seckill closed");
            }else{
                //表示秒杀成功
                int insertCount = successSeckillDao.insertSuccessSeckill(seckillId,userPhone);
                if(insertCount <= 0){
                    //插入失败；原因可能是同一个用户多次秒杀；处理异常
                    throw new RepeatKillException("seckill repeat");
                }else{
                    //插入成功；返回插入成功后的明细表
                    SuccessSeckill successSeckill = successSeckillDao.queryIdWithSeckill(seckillId);
                    return new SeckillExecution(seckillId, SeckillEnum.SUCCESS,successSeckill);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException(e.getMessage());
        }

    }
}
