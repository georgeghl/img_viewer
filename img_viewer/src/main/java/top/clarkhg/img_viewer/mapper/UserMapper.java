package top.clarkhg.img_viewer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import top.clarkhg.img_viewer.pojo.User;

@Repository
@Mapper
public interface UserMapper {
    public User selectUserByUsername(String username);
    public int updateUserPassword(User user);
    public int insertUser(User user);
}