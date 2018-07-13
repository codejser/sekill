package com.meteor.service.impl;

import com.meteor.BaseTest;
import com.meteor.dto.Exposer;
import com.meteor.dto.SeckillExecution;
import com.meteor.exception.RepeatKillException;
import com.meteor.exception.SeckillCloseException;
import com.meteor.service.ISeckillService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: meteor @Date: 2018/7/10 22:58
 */
public class SeckillServiceImplTest extends BaseTest {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService iSeckillService;

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1003;
        Exposer exposer = iSeckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            logger.info("exposer:{}",exposer);
            String md5 = exposer.getMd5();
            long userPhone = 54786578445L;
            try{
                SeckillExecution seckillExecution = iSeckillService.executeSeckillProcedure(seckillId,userPhone,md5);
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