package top.clarkhg.img_viewer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import top.clarkhg.img_viewer.pojo.CaptchaCode;

@Repository
@Mapper
public interface CaptchaCodeMapper {
    public CaptchaCode selectCaptchaCodeByEmail(String email);
    public int insertCaptchaCode(CaptchaCode captchaCode);
    public int updateCaptchaCode(CaptchaCode captchaCode);
}
