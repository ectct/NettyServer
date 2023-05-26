package org.e.service.impl;

import org.e.entity.Monitor;
import org.e.mapper.MonitorMapper;
import org.e.service.MonitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author e
 * @since 2023-05-25
 */
@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorMapper, Monitor> implements MonitorService {

}
