package top.clarkhg.img_viewer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import top.clarkhg.img_viewer.pojo.MyConfigItem;

@Mapper
@Repository
public interface MyConfigItemMapper {
    public int insertConfigItem(MyConfigItem myConfigItem);
    public MyConfigItem selectConfigItem(String itemName);
    public String selectConfigValueByName(String name);
    public int updateConfigItem(MyConfigItem myConfigItem);
}
