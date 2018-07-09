//存放交互逻辑
//模块化


//JSON对象

var seckill = {
    //封装秒杀相关的URL
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/' + seckillId + "/exposer";
        },
        execution : function (seckillId,md5) {
            return '/seckill/' + seckillId + "/"+ md5 +"/execution";
        },
    },
    //验证手机号是否正确的方法
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    //封装秒杀逻辑的方法
    handleSeckill: function (seckillId,node) {
        //获取秒杀地址，控制显示的逻辑，执行秒杀的操作
        //显示开始秒杀的按钮
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        //执行Post请求;url为/seckill/{seckillId}/exposer
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //如果请求成功，获取暴露的接口
            if(result && result['success']){
                var exposer = result['data'];
                //再次判断Exposer是否开启了秒杀
                if(exposer['exposed']){
                    //开启秒杀
                    //获取执行秒杀的地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log('killUrl:'+killUrl);
                    //为秒杀按钮绑定点击事件
                    $("#killBtn").one('click',function () {
                        //点击之后禁用按钮
                        $(this).addClass('disabled');
                        //执行秒杀请求
                        $.post(killUrl,{},function (result) {
                           if(result && result['success']) {
                                //获取result中的数据
                               var killResult = result['data'];
                               var state = killResult['state'];
                               var stateInfo = killResult['stateInfo'];
                               //显示秒杀的结果
                               node.html('<span class="label label-success">' + stateInfo + '</span>');
                           }
                        });
                    });
                    node.show();
                }else{
                    //未开启秒杀，重新执行倒计时
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                //如果请求失败，就控制台输出请求结果
                console.log('result:'+result);
            }


        });
    },
    //封装倒计时方法
    countdown: function (seckillId, nowTime, startTime, endTime) {
        //获取倒计时显示的结点
        var seckillBox = $('#seckill-box');

        //判断当前时间是否在秒杀活动期间
        if (nowTime > endTime) {
            //如果当前时间大于结束时间，则秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //如果当前时间小于开始时间，则显示倒计时的区域：绑定倒计时的事件
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //输出的时间格式
                var format = event.strftime(' 秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //执行秒杀的逻辑
                //封装了秒杀逻辑的方法
                seckill.handleSeckill(seckillId,seckillBox);
            })
        } else {
            //秒杀结束
            seckill.handleSeckill(seckillId,seckillBox);
        }


    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机和登陆验证；   计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killphone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            //验证手机号是否存在或者手机号是否正确
            if (!seckill.validatePhone(killPhone)) {
                //此时cookie中没有手机号，需要绑定手机号
                //获取弹出层
                var killPhoneModal = $('#killPhoneModal');
                //设置弹出层的属性
                killPhoneModal.modal({
                    show: true,        //显示弹出层
                    backdrop: 'static',        //禁止位置关闭
                    keyboard: false            //关闭键盘事件
                });
                //为提交按钮绑定点击事件
                $("#killPhoneBtn").click(function () {
                    //获取已填入的手机号
                    var killphone = $("#killPhoneKey").val();
                    //判断手机号是否合法
                    if (seckill.validatePhone(killphone)) {
                        //如果手机号合法：就将手机号写入cookie;
                        $.cookie('killphone', killphone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        //如果手机号不合法：就在弹出层显示错误提示
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });

            }
            //此时页面的cookie中已经存在手机号：证明其已经登录了秒杀系统
            //此时执行倒计时的逻辑
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    //获取系统的当前时间
                    var nowTime = result['data'];
                    //调用封装的显示秒杀倒计时的方法
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            });


        }
    }
}
