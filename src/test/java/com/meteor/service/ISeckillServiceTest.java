package com.meteor.service;

import com.meteor.BaseTest;
import com.meteor.dao.SeckillDao;
import com.meteor.dto.Exposer;
import com.meteor.dto.SeckillExecution;
import com.meteor.entity.Seckill;
import com.meteor.exception.RepeatKillException;
import com.meteor.exception.SeckillCloseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: meteor @Date: 2018/7/8 15:37
 */
public class ISeckillServiceTest extends BaseTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService iSeckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = iSeckillService.getSeckillList();
        logger.info("list{}",seckillList);
    }

    @Test
    public void getById() {
        long seckillId = 1000;
        Seckill seckill = iSeckillService.getById(seckillId);
        logger.info("seckillName:{}",seckill.getName());
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1001;
        Exposer exposer = iSeckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            logger.info("exposer:{}",exposer);
            String md5 = exposer.getMd5();
            long userPhone = 412543544132L;
            try{
                SeckillExecution seckillExecution = iSeckillService.executeSeckill(seckillId,userPhone,md5);
                logger.info("secSeckillExecution:{}",seckillExecution);
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }
        }else{
            logger.info("exposer:{}",exposer);
        }

    }


}