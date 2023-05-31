package org.e.service;

import com.alibaba.fastjson.JSONObject;
import org.e.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author e
 * @since 2023-05-25
 */
public interface UserService extends IService<User> {

    User getByUsername(String username);

    JSONObject getSensorById(Integer id);

    void updateSenor(Integer userId, List<Integer> subscribe);

    String getMail(Integer id);

    Integer getUserId(Integer id);
}
