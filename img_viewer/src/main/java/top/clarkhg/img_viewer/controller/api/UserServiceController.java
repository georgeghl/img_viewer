package top.clarkhg.img_viewer.controller.api;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import top.clarkhg.img_viewer.service.UserService;
import top.clarkhg.img_viewer.service.crud.CaptchaCodeCRUDService;
import top.clarkhg.img_viewer.util.TokenUtil;


@RestController
public class UserServiceController {
    private Logger logger = LoggerFactory.getLogger(UserServiceController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaCodeCRUDService captchaCodeCRUDService;

    @PostMapping(value = "/service/login")
    public JSONObject ServiceLogin(String username, String password, int stayLogin) {
        logger.debug("/service/login called.");
        logger.debug("parameters: { username: " + username + " , password: " + password + " , stayLogin: " + stayLogin
                + " }");
        int res = userService.checkUserPassword(username, password);
        logger.debug(Integer.toString(res));
        String token = TokenUtil.genToken(username, password);
        if (res == 1) {
            return JSON.parseObject("{'res':200,'status':200,'message':'ok','token':'" + token + "'}");
        }
        return JSON.parseObject("{'res':200,'status':403,'message':'wrong'}");
    }

    @PostMapping("/service/emailCode")
    public String emailCode(String email) {
        try {
            logger.debug(email);
            captchaCodeCRUDService.sendNewCaptchaCode(email);
            return "获取成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "错误！";
        }

    }

    @PostMapping("/service/register")
    public String register(String username, String password, String captchaCode, String email) {
        int res = userService.registerNewUser(username, password, captchaCode, email);
        if (res == 1) {

            return "1";
        } else if (res == 2) {
            return "2";
        } else {
            return "0";
        }
    }

}
