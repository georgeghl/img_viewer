package top.clarkhg.img_viewer.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.clarkhg.img_viewer.controller.UserServiceController;
import top.clarkhg.img_viewer.mapper.ImageMapper;
import top.clarkhg.img_viewer.pojo.Image;


import java.util.List;

@Service
public class ImageCRUDService {
    
    @Autowired
    private ImageMapper imageMapper;

    private Logger logger = LoggerFactory.getLogger(UserServiceController.class);
    public int getAllPublicImageCount() {
        return imageMapper.selectPublicImageCount();
    }

    @Transactional
    public List<Image> getAllPublicImages() {
        return imageMapper.selectPublicImages();
    }

    @Transactional
    public List<Image> getAllPublicImagesWithPaging(int page) {
        return imageMapper.selectPublicImagesLimit((page-1)*25, 25);
    }

    @Transactional
    public List<Image> getImagesByUsername(String username) {
        return imageMapper.selectImagesByUsername(username);
    }

    @Transactional
    public int getImageCountByUsername(String username) {
        return imageMapper.selectImageCountByUsername(username);
    }

    @Transactional
    public List<Image> getImagesByUsernameWithPaging(String username, int page) {
        return imageMapper.selectImagesByUsernameLimit(username,(page-1)*25, 25);
    }

    @Transactional
    public Image getImageByImageId(int imageId){
        return imageMapper.selectImageByImageId(imageId);
    }

    @Transactional
    public Image getImageAndOwnerByImageId(int imageId){
        return imageMapper.selectImageAndOwnerByImageId(imageId);
    }

    @Transactional
    public Image uploadNewImage(String username, String name, String description, int typeId, int public_,
            int categoryId, String originalFilename) {
        Image imagePojo = new Image();
        imagePojo.setImageName(name);
        imagePojo.setDescription(description);
        imagePojo.setTypeId(typeId);
        imagePojo.setPublic_(public_);
        imagePojo.setCategoryId(categoryId);
        imagePojo.setOriginalFilename(originalFilename);
        if (imageMapper.insertImage(imagePojo) == 1) {
            logger.debug(""+imagePojo.getImageId());
            if (imageMapper.insertUsernameImageId(username, imagePojo.getImageId()) == 1) {
                return imagePojo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public String modifyImage(Image image) {
        if(image==null){
            return "错误！";
        }
        int res=imageMapper.updateImage(image);
        if(res!=0){
            return "更新成功！";
        }
        else{
            return "更新失败！";
        }
    }

    @Transactional
    public int updateImageGps(Image image){
        if(image==null){
            return 0;
        }
        int res=imageMapper.updateImageGps(image);
        return res;
        }

    
    

    @Transactional
    public int virtualDeleteImage(String username, int imageId){
        imageMapper.updateImageNotPublic(imageId);
        return imageMapper.deleteUsernameImageId(username, imageId);
    }

    @Transactional
    public int realDeleteImage(String username, int imageId){
        int res=0;
        try{
            int res_1=imageMapper.deleteUsernameImageId(username, imageId);
            int res_2=imageMapper.deleteImage(imageId);
            if( res_1!=0 & res_2!=0){
                res=1;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
