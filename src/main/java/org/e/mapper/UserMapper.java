package org.e.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.e.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @Select("select sensor_id from user_sensor where user_id=#{id}")
    public List<Integer> getSensorId(Integer id);
}
