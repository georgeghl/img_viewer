package top.clarkhg.img_viewer.service.crud;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import top.clarkhg.img_viewer.controller.UserServiceController;
import top.clarkhg.img_viewer.mapper.CaptchaCodeMapper;
import top.clarkhg.img_viewer.pojo.CaptchaCode;
import top.clarkhg.img_viewer.util.CaptchaCodeUtil;

@Service
public class CaptchaCodeCRUDService {
    private Logger logger = LoggerFactory.getLogger(UserServiceController.class);
    @Autowired
    private CaptchaCodeMapper captchaCodeMapper;
    @Autowired
    private MailSender mailSender;

    @Transactional
    public CaptchaCode getCaptchaCodeByEmail(String email) {
        return captchaCodeMapper.selectCaptchaCodeByEmail(email);
    }

    @Transactional
    public int sendNewCaptchaCode(String email) throws MessagingException, UnsupportedEncodingException {
        CaptchaCode captchaCode = captchaCodeMapper.selectCaptchaCodeByEmail(email);
        String captchaCodeString = CaptchaCodeUtil.genCaptchaCode();
        if (captchaCode == null) {
            captchaCode = new CaptchaCode();
            captchaCode.setEmail(email);
            captchaCode.setCaptchaCode(captchaCodeString);
            logger.debug(captchaCode.toString());
            captchaCodeMapper.insertCaptchaCode(captchaCode);
        } else {
            captchaCode.setCaptchaCode(captchaCodeString);
            logger.debug(captchaCode.toString());
            captchaCodeMapper.updateCaptchaCode(captchaCode);
        }
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 1.发件人昵称
            String name = MimeUtility.encodeText("ImageViewer");
            // 2.发件人邮箱
            simpleMailMessage
                    .setFrom(String.valueOf(new InternetAddress(name + "<" + "flippedclass0418@163.com" + ">")));
            // 3.收件人
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("ImageViewer Captcha Code");
            // 4.设置邮件内容
            simpleMailMessage
                    .setText("您的验证码是: [  " + captchaCodeString + "   ], 打死也不要告诉其他人哦！\nYour captcha code is: [  "
                            + captchaCodeString + "   ], do not tell others!");
            // 5.发送邮件
            mailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
