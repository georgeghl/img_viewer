package top.clarkhg.img_viewer.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.clarkhg.img_viewer.pojo.Image;

import java.util.List;

@Mapper
@Repository
public interface ImageMapper {
    public int selectPublicImageCount();
    public List<Image> selectPublicImages();
    public List<Image> selectPublicImagesLimit(int offset, int limit);
    public Image selectImageByImageId(int imageId);
    public Image selectImageAndOwnerByImageId(int imageId);
    public List<Image> selectImagesByUsername(String username);
    public int selectImageCountByUsername(String username);
    public List<Image> selectImagesByUsernameLimit(String username, int offset, int limit);
    public int insertImage(Image imagePojo);
    public int updateImageNotPublic(int imageId);
    
    //user_id_image_id 表的操作也放在ImageMapper中
    public int insertUsernameImageId(String username,int imageId);
    public int deleteImage(int imageId);
    public int deleteUsernameImageId(String username,int imageId);
    public int updateImage(Image image);

    //
    public int updateImageGps(Image image);
}
