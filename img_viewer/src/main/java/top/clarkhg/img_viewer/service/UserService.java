package top.clarkhg.img_viewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.clarkhg.img_viewer.pojo.CaptchaCode;
import top.clarkhg.img_viewer.pojo.User;
import top.clarkhg.img_viewer.service.crud.CaptchaCodeCRUDService;
import top.clarkhg.img_viewer.service.crud.UserCRUDService;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserCRUDService userCRUDService;
    @Autowired
    private CaptchaCodeCRUDService captchaCodeCRUDService;

    @Transactional
    public int checkUserPassword(String username, String password) {
        User user = userCRUDService.getUserByUsername(username);
        if (user != null) {
            logger.debug(user.toString());
            logger.debug("password: "+password);
            if (user.getPassword().equals(password)) {
                return 1;
            }
        }
        return 0;
    }

    @Transactional
    public int registerNewUser(String username, String password, String captchaCodeSubmitted, String email){
        CaptchaCode captchaCode=captchaCodeCRUDService.getCaptchaCodeByEmail(email);
        String captchaCodeDB=captchaCode.getCaptchaCode();
        logger.debug("Captcha Code String: "+captchaCodeDB);
        logger.debug("captchaCodeSubmitted Code String: "+captchaCodeSubmitted);
        if(captchaCodeDB.equals(captchaCodeSubmitted)){
            try{
                userCRUDService.insertUser(username,password,email);
                return 1;
            }
            catch(Exception e ){
                e.printStackTrace();
                return 0;
            }
        }
        else{
            return 2;
        }
    }   
}
