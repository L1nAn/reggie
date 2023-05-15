package com.mzw.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mzw.common.R;
import com.mzw.entity.User;

import com.mzw.service.UserService;
import com.mzw.utils.SMSUtils;
import com.mzw.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/26
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {//参数是phone,但是我们用User来进行接收，因为USer里面就有phone这个属性
        //获取手机号
        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)) {
            //生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用阿里云提供的短信服务API发送短信,但是我们自己的项目是没有发送短信的功能的，所以下面的代码注销掉
//            SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);

            return R.success("短信发送成功");
        }
        //发送失败
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登陆功能
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    //map就是键值对，phone对应的是code
    public R<User> login(@RequestBody Map map, HttpSession session) {//这也可以在dto那里新建一个类来代替map，一样的效果
//返回的是用户信息，因为登录之后，浏览器需要我们的用户信息。浏览器需要展示我们的信息。
        log.info(map.toString());//看是否正确封装参数map

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码对比
        if(codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登陆成功。
            //判断当前手机号对应的用户是否为新用户，如果是就自动完成注册
            LambdaQueryWrapper<User> qu = new LambdaQueryWrapper<>();
            qu.eq(User::getPhone, phone);
            User user = userService.getOne(qu);//one代表唯一
            if(user == null) {
                 user = new User();
                 user.setPhone(phone);
                 user.setStatus(1);//设置状态，不设置也可以，因为有默认值
                 userService.save(user);//保存用户，也就是添加新用户
            }
            session.setAttribute("user", user.getId());//这句很重要，没有这句话，经过过滤器校验的时候就会被拦截住
            return R.success(user);
        }

        return R.error("登陆失败");
    }
}
