package org.e.service;

import org.e.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
