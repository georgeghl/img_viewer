package top.clarkhg.img_viewer.util;

public class CaptchaCodeUtil {
    public static String genCaptchaCode() {
        String captchaCode = "";
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * 10);
            captchaCode += rand;
        }
        return captchaCode;
    }
}
