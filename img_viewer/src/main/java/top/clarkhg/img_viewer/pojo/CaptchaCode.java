package top.clarkhg.img_viewer.pojo;

public class CaptchaCode implements java.io.Serializable{
    private String email;
    private String captchaCode;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCaptchaCode() {
        return captchaCode;
    }
    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }
    @Override
    public String toString() {
        return "CaptchaCode [email=" + email + ", captchaCode=" + captchaCode + "]";
    }
    
}
