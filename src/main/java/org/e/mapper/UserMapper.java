package org.e.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.e.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author e
 * @since 2023-05-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
