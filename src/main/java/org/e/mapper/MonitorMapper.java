package org.e.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.e.entity.Monitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author e
 * @since 2023-05-25
 */
@Mapper
public interface MonitorMapper extends BaseMapper<Monitor> {
    @Select("select * from monitor where sensor_id=#{sensorId} order by time desc limit 1;")
    public Monitor getData(Integer sensorId);

}
