package top.clarkhg.img_viewer.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;


import top.clarkhg.img_viewer.pojo.Image;
import top.clarkhg.img_viewer.service.ImageService;
import top.clarkhg.img_viewer.service.crud.ImageCRUDService;
import top.clarkhg.img_viewer.util.TokenUtil;


@RestController
public class ImageServiceController {
    private Logger logger = LoggerFactory.getLogger(ImageServiceController.class);
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageCRUDService imageCRUDService;

    @PostMapping("/service/upload")
    public String uploadImg(@RequestParam("image") MultipartFile image,
            @RequestParam("imageName") String imageName,
            @RequestParam("imageDescription") String imageDescription,
            @RequestParam("imageCategory") int imageCategory,
            @RequestParam("imageType") int imageType,
            @RequestParam("accessType") int accessType,
            @RequestParam("token") String token) throws Exception{
        // 在这里处理上传的图片和信息，保存到数据库或文件系统中
        // 对上传的图片执行业务逻辑，这里仅仅是示例，可以根据实际需求进行更改
        // 返回一个成功或失败的信息
            String res=imageService.uploadImg(image, imageName, imageDescription, imageCategory, imageType, accessType, token);
            return res;
    }

    @PostMapping("/service/modify")
    public String modifyImg(@RequestParam("imageId") int imageId,
            @RequestParam("imageName") String imageName,
            @RequestParam("imageDescription") String imageDescription,
            @RequestParam("imageCategory") int imageCategory,
            @RequestParam("imageType") int imageType,
            @RequestParam("accessType") int accessType,
            @RequestParam("token") String token) {
        // 在这里处理上传的图片和信息，保存到数据库或文件系统中
        // 对上传的图片执行业务逻辑，这里仅仅是示例，可以根据实际需求进行更改
        // 返回一个成功或失败的信息
        String res=imageService.modifyImg(imageId, imageName, imageDescription, imageCategory, imageType, accessType, token);
        return res;
    }

    @Transactional
    @PostMapping("/service/delete")
    public String deleteImg(@RequestParam("token") String token, @RequestParam("imageId") int imageId) {
        logger.debug("ServiceController.deleteImg() Called......");
        logger.debug("imageId: " + imageId + "token: " + token);
        String res = imageService.deleteImage(token, imageId);
        return res;
    }

    @PostMapping("/service/getUserImageCount")
    public String getUserImageCount(@RequestParam("token") String token) {
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "403");
            res.put("msg", "no such user");
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        } else {
            int cnt = imageCRUDService.getImageCountByUsername(username);
            Map<String, Object> res = new HashMap<>();
            res.put("code", "200");
            res.put("msg", "ok");
            res.put("data", cnt);
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        }
    }

    @PostMapping("/service/getUserImageByPage")
    public String getUserImageByPage(@RequestParam("token") String token, @RequestParam("page") int page) {
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "403");
            res.put("msg", "no such user");
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        } else {
            List<Image> images = imageCRUDService.getImagesByUsernameWithPaging(username, page);
            Map<String, Object> res = new HashMap<>();
            res.put("code", "200");
            res.put("msg", "ok");
            res.put("data", images);
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        }
    }

    @PostMapping("/service/getPublicImageCount")
    public String getPublicImageCount() {
        int cnt = imageCRUDService.getAllPublicImageCount();
        Map<String, Object> res = new HashMap<>();
        res.put("code", "200");
        res.put("msg", "ok");
        res.put("data", cnt);
        JSONObject resp = new JSONObject(res);
        return JSON.toJSONString(resp);
    }

    @PostMapping("/service/getPublicImageByPage")
    public String getPublicImageByPage(@RequestParam("page") int page) {
        List<Image> images = imageCRUDService.getAllPublicImagesWithPaging(page);
        Map<String, Object> res = new HashMap<>();
        res.put("code", "200");
        res.put("msg", "ok");
        res.put("data", images);
        JSONObject resp = new JSONObject(res);
        return JSON.toJSONString(resp);
    }

    @PostMapping("/service/getImageInfo")
    public String getImageInfo(@RequestParam("imageId") int imageId, @RequestParam("token") String token) {
        if (token == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "403");
            res.put("msg", "token is null");
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        }
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "403");
            res.put("msg", "no such user");
            JSONObject resp = new JSONObject(res);
            return JSON.toJSONString(resp);
        }
        Image image = imageCRUDService.getImageAndOwnerByImageId(imageId);
        Map<String, Object> res = new HashMap<>();
        res.put("code", "200");
        res.put("msg", "ok");
        res.put("data", image);
        JSONObject resp = new JSONObject(res);
        return JSON.toJSONString(resp);
    }
    

}
