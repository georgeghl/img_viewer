package top.clarkhg.img_viewer.service.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.clarkhg.img_viewer.mapper.UserMapper;
import top.clarkhg.img_viewer.pojo.User;

@Service
public class UserCRUDService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public User getUserByUsername(String username){
        return userMapper.selectUserByUsername(username);
    }

    
    @Transactional
    public int updateUserPasswordByUsername(String username, String password){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        return userMapper.updateUserPassword(user);
    }

    @Transactional
    public int insertUser(String username, String password,String email){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return userMapper.insertUser(user);
    }
}
