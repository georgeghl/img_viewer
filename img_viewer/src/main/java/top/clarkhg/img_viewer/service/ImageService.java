package top.clarkhg.img_viewer.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import top.clarkhg.img_viewer.mapper.MyConfigItemMapper;
import top.clarkhg.img_viewer.pojo.Image;
import top.clarkhg.img_viewer.service.crud.ImageCRUDService;
import top.clarkhg.img_viewer.util.FileUtil;
import top.clarkhg.img_viewer.util.ImageUtil;
import top.clarkhg.img_viewer.util.KrpanoUtil;
import top.clarkhg.img_viewer.util.TokenUtil;

@Service
public class ImageService {
    private Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private ImageCRUDService imageCRUDService;
    @Autowired
    private MyConfigItemMapper myConfigItemMapper;

    @Transactional
    public String deleteImage(String token, int imageId) {
        String res = "";
        Image image = imageCRUDService.getImageByImageId(imageId);
        if (image.getTypeId() == 1) {
            res = deletePanoImage(token, imageId);
        } else if (image.getTypeId() == 2) {
            res = deleteNormalImage(token, imageId);
        } else {
            res = "illegal image type error";
        }
        return res;
    }

    private String deleteNormalImage(String token, int imageId) {
        if (token == null) {
            return "删除失败: 无权限！";
        }
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            return "删除失败: 无权限！";
        }
        Image image = imageCRUDService.getImageAndOwnerByImageId(imageId);
        if (image == null) {
            return "删除失败: 该图片不存在！";
        }
        logger.debug(image.toString());
        if (image.getOwnerUsername().equals(username)) {
            String IMAGE_DELETE_TYPE = myConfigItemMapper.selectConfigValueByName("image_delete_type");
            if (IMAGE_DELETE_TYPE.equals("TRUE")) {
                int res = imageCRUDService.realDeleteImage(username, imageId);
                if (res != 0) {
                    try {
                        String uploadPath = myConfigItemMapper.selectConfigValueByName("upload_path");
                        uploadPath = uploadPath + "/image_normal/";
                        String fileFolderPath = uploadPath + imageId + "/";
                        File ffffff = new File(fileFolderPath);
                        if (FileUtil.deleteFile(ffffff)) {
                            logger.debug("文件删除成功！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "服务器内部发生错误！";
                    }
                    return "删除成功！";
                }

            } else if (IMAGE_DELETE_TYPE.equals("FALSE")) {
                int res = imageCRUDService.virtualDeleteImage(username, imageId);
                if (res != 0) {
                    return "删除成功！";
                }
            } else {
                return "服务器内部配置错误，删除失败！";
            }

        }

        String res = "ok";
        return res;
    }

    @Transactional
    public String deletePanoImage(String token, int imageId) {
        if (token == null) {
            return "删除失败: 无权限！";
        }
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            return "删除失败: 无权限！";
        }
        Image image = imageCRUDService.getImageAndOwnerByImageId(imageId);
        if (image == null) {
            return "删除失败: 该图片不存在！";
        }
        logger.debug(image.toString());
        if (image.getOwnerUsername().equals(username)) {
            String IMAGE_DELETE_TYPE = myConfigItemMapper.selectConfigValueByName("image_delete_type");
            if (IMAGE_DELETE_TYPE.equals("TRUE")) {
                int res = imageCRUDService.realDeleteImage(username, imageId);
                if (res != 0) {
                    try {
                        String uploadPath = myConfigItemMapper.selectConfigValueByName("upload_path");
                        uploadPath = uploadPath + "/image_pano/";
                        String fileFolderPath = uploadPath + imageId + "/";
                        File ffffff = new File(fileFolderPath);
                        if (FileUtil.deleteFile(ffffff)) {
                            logger.debug("文件删除成功！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "服务器内部发生错误！";
                    }
                    return "删除成功！";
                }

            } else if (IMAGE_DELETE_TYPE.equals("FALSE")) {
                int res = imageCRUDService.virtualDeleteImage(username, imageId);
                if (res != 0) {
                    return "删除成功！";
                }
            } else {
                return "服务器内部配置错误，删除失败！";
            }

        }

        String res = "ok";
        return res;
    }

    @Transactional
    public String uploadImg(MultipartFile image,
            String imageName,
            String imageDescription,
            int imageCategory,
            int imageType,
            int accessType,
            String token) throws Exception{
        String res = "";
        if (!image.isEmpty()) {
            // try {
                // 输出上传的图片及信息到日志
                logger.debug("上传的图片名称: {}", imageName);
                logger.debug("上传的图片描述: {}", imageDescription);
                logger.debug("上传的图片类别: {}", imageCategory);
                logger.debug("上传的图片类型: {}", imageType);
                logger.debug("上传的图片访问类型: {}", accessType);
                String originalFilename = image.getOriginalFilename();
                logger.debug("上传的图片原始名称: {}", originalFilename);
                String username = TokenUtil.getUsername(token);
                logger.debug(username);
                if (!(username != null)) {
                    return "上传失败: 无权限!";
                }
                Image imageUploaded = imageCRUDService.uploadNewImage(username, imageName, imageDescription, imageType,
                        accessType, imageCategory, originalFilename);
                logger.debug(
                        "Image properties: imageId={}, imageName={}, description={}, typeId={}, public_={}, categoryId={}",
                        imageUploaded.getImageId(), imageUploaded.getImageName(), imageUploaded.getDescription(),
                        imageUploaded.getTypeId(), imageUploaded.getPublic_(), imageUploaded.getCategoryId());
                if (imageType == 1) {
                    res = uploadPanoImg(image, imageName, imageDescription, imageCategory, imageType, accessType, token,
                            imageUploaded);
                } else if (imageType == 2) {
                    res = uploadNormalImg(image, imageName, imageDescription, imageCategory, imageType, accessType,
                            token, imageUploaded);

                } else {
                    res = "error!";
                }
            // } catch (Exception e) {
            //     logger.error("上传图片失败: {}", e.getMessage());
            //     return "上传失败: " + e.getMessage();
            // }
        } else {
            return "上传失败: 图片为空！";
        }
        return res;
    }

    @Transactional
    public String uploadNormalImg(MultipartFile image,
            String imageName,
            String imageDescription,
            int imageCategory,
            int imageType,
            int accessType,
            String token,
            Image imageUploaded) {
        // 指定保存图片的目录路径
        String uploadPath = myConfigItemMapper.selectConfigValueByName("upload_path"); // 替换为你想要保存图片的目录路径
        uploadPath = uploadPath + "/image_normal/";
        try {
            // 如果目录不存在，则创建目录
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 构造保存图片的文件路径
            // 使用原始文件名
            String fileFolderPath = uploadPath + imageUploaded.getImageId() + "/";
            String filePath = uploadPath + imageUploaded.getImageId() + "/best.jpg";
            File currentImageFolder = new File(fileFolderPath);
            if (!currentImageFolder.exists()) {
                currentImageFolder.mkdirs();
            }
            // 将图片保存到指定路径
            File dest = new File(filePath);
            image.transferTo(dest);
            // 获取gps
            Map<String, Double> gpsInfoDouble=ImageUtil.getGpsInfoDouble(filePath);
            Map<String, String[]> gpsInfoDms=ImageUtil.getGpsInfoDms(filePath);
            Map<String, String[]> gpsInfoRaw=ImageUtil.getGpsInfoRaw(filePath);
            Map<String, Map> gpsMap=new HashMap<>();
            gpsMap.put("gpsInfoDouble", gpsInfoDouble);
            gpsMap.put("gpsInfoDms", gpsInfoDms);
            gpsMap.put("gpsInfoRaw", gpsInfoRaw);
            JSONObject gpsJson=new JSONObject(gpsMap);
            String gps=JSON.toJSONString(gpsJson);
            logger.debug(gps);
            imageUploaded.setGps(gps);
            imageCRUDService.updateImageGps(imageUploaded);
        
            
            // 生成缩略图
            String thumbPath = uploadPath + imageUploaded.getImageId() + "/thumb.jpg";
            boolean suc = ImageUtil.imageCompress(filePath, thumbPath);
            if (suc) {
                return "上传成功！";
            } else {
                return "error!";
            }
        } catch (Exception e) {
            logger.debug(e.toString());
            return "error!";
        }
    }

    @Transactional
    public String uploadPanoImg(MultipartFile image,
            String imageName,
            String imageDescription,
            int imageCategory,
            int imageType,
            int accessType,
            String token,
            Image imageUploaded) throws Exception {
        // 是否保留原始图像文件
        String preserveOriginalFile = myConfigItemMapper.selectConfigValueByName("preserve_original_file");
        // 指定保存图片的目录路径
        String uploadPath = myConfigItemMapper.selectConfigValueByName("upload_path"); // 替换为你想要保存图片的目录路径
        uploadPath = uploadPath + "/image_pano/";

        // 如果目录不存在，则创建目录
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 构造保存图片的文件路径
        // 使用原始文件名
        String fileFolderPath = uploadPath + imageUploaded.getImageId() + "/";
        String filePath = uploadPath + imageUploaded.getImageId() + "/pano.jpg";
        File currentImageFolder = new File(fileFolderPath);
        if (!currentImageFolder.exists()) {
            currentImageFolder.mkdirs();
        }
        // 将图片保存到指定路径
        File dest = new File(filePath);
        image.transferTo(dest);

        // 获取gps
        Map<String, Double> gpsInfoDouble=ImageUtil.getGpsInfoDouble(filePath);
        if(gpsInfoDouble==null){
            gpsInfoDouble=new HashMap<String, Double>();
        }
        Map<String, String[]> gpsInfoDms=ImageUtil.getGpsInfoDms(filePath);
        if(gpsInfoDms==null){
            gpsInfoDms=new HashMap<String,String[]>();
        }
        Map<String, String[]> gpsInfoRaw=ImageUtil.getGpsInfoRaw(filePath);
        if(gpsInfoRaw==null){
            gpsInfoRaw=new HashMap<String,String[]>();
        }
        Map<String, Map> gpsMap=new HashMap<>();
        gpsMap.put("gpsInfoDouble", gpsInfoDouble);
        gpsMap.put("gpsInfoDms", gpsInfoDms);
        gpsMap.put("gpsInfoRaw", gpsInfoRaw);
        JSONObject gpsJson=new JSONObject(gpsMap);
        String gps=JSON.toJSONString(gpsJson);
        logger.debug(gps);
        imageUploaded.setGps(gps);
        imageCRUDService.updateImageGps(imageUploaded);


        // 复制全景图控制xml文件
        // 源文件路径
        Path sourceFile = Paths.get(uploadPath + "pano.xml");
        // 目标文件路径
        Path targetFile = Paths.get(fileFolderPath + "pano.xml");
        try {
            // 复制文件
            Files.copy(sourceFile, targetFile);
            logger.debug("文件复制成功！");
        } catch (IOException e) {
            logger.debug("文件复制失败：" + e.getMessage());
            return "上传失败: " + e.getMessage();
        }

        // 调用util将sphere转换为cube图
        String previewImagePath = uploadPath + imageUploaded.getImageId() + "/thumb.jpg";
        String vcubePath = uploadPath + imageUploaded.getImageId() + "/preview.jpg";
        KrpanoUtil.sphere2cube(filePath, fileFolderPath);
        try {
            // 让程序等待5秒钟
            Thread.sleep(3000); // 5000毫秒 = 5秒
        } catch (InterruptedException e) {
            logger.debug("等待被中断了！");
        }
        KrpanoUtil.makePreview(filePath, previewImagePath);
        KrpanoUtil.sphere2cubeVCUBE(filePath, vcubePath);
        try {
            // 让程序等待5秒钟
            Thread.sleep(3000); // 5000毫秒 = 5秒
        } catch (InterruptedException e) {
            logger.debug("等待被中断了！");
        }
        try {
            File input = new File(fileFolderPath + "pano_f.jpg");
            File output = new File(fileFolderPath + "pano_f.jpg");
            BufferedImage image__ = ImageIO.read(input);
            ImageIO.write(image__, "jpg", output);
            if (preserveOriginalFile.equals("TRUE")) {
                logger.debug("preserve original image file, skip deleting......");
            } else if (preserveOriginalFile.equals("FALSE")) {
                logger.debug("do not preserve original image file, now deleting......");
                String originalFilePath = uploadPath + imageUploaded.getImageId() + "/pano.jpg";
                logger.debug(originalFilePath);
                File ffffff = new File(originalFilePath);
                ffffff.delete();
            } else {
                logger.warn("Config item `preserve_original_file` is missing, skip deleting(default)......");
                ;
            }
        } catch (IOException e) {
            logger.debug("文件复制失败：" + e.getMessage());
            return "上传失败: " + e.getMessage();
        }

        return "上传成功！";

    }

    @Transactional
    public String modifyImg(int imageId,
            String imageName,
            String imageDescription,
            int imageCategory,
            int imageType,
            int accessType,
            String token) {
        try {
            // 输出上传的图片及信息到日志
            logger.debug("修改的图片名称: {}", imageName);
            logger.debug("修改的图片描述: {}", imageDescription);
            logger.debug("修改的图片类别: {}", imageCategory);
            logger.debug("修改的图片类型: {}", imageType);
            logger.debug("修改的图片访问类型: {}", accessType);
            String username = TokenUtil.getUsername(token);
            logger.debug(username);
            if (!(username != null)) {
                return "修改失败: 无权限!";
            }
            Image image = imageCRUDService.getImageAndOwnerByImageId(imageId);
            if (image.getOwnerUsername().equals(username)) {
                image.setImageName(imageName);
                image.setDescription(imageDescription);
                image.setCategoryId(imageCategory);
                image.setTypeId(imageType);
                image.setPublic_(accessType);
                imageCRUDService.modifyImage(image);
            } else {
                return "修改失败: 无权限!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
