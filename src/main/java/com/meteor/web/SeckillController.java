package com.meteor.web;

import com.meteor.dto.Exposer;
import com.meteor.dto.SeckillExecution;
import com.meteor.dto.SeckillResult;
import com.meteor.entity.Seckill;
import com.meteor.enums.SeckillEnum;
import com.meteor.exception.RepeatKillException;
import com.meteor.exception.SeckillCloseException;
import com.meteor.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author: meteor @Date: 2018/7/9 13:36
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ISeckillService iSeckillService;

    /**
     * 获取秒杀商品的列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){

        List<Seckill> seckillList = iSeckillService.getSeckillList();
        model.addAttribute("list",seckillList);
        return "list";
    }


    /**
     * 返回秒杀商品的详情信息
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = iSeckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     * 返回秒杀接口的地址JSON
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = iSeckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀的操作
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue("killphone") Long phone){
        SeckillResult<SeckillExecution> result;
        if(phone == null){
            result =  new SeckillResult<SeckillExecution>(false,"未注册");
        }
        try{
            SeckillExecution seckillExecution = iSeckillService.executeSeckill(seckillId,phone,md5);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatKillException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillCloseException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillEnum.END);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }
        return result;

    }

    /**
     * 获取系统当前的时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getTime(){
        Date date = new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }

}
