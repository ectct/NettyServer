package org.e.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.e.entity.*;
import org.e.mapper.*;
import org.e.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author e
 * @since 2023-05-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MonitorMapper monitorMapper;
    @Autowired
    OximeterMapper oximeterMapper;
    @Autowired
    OxyconMapper oxyconMapper;
    @Autowired
    VentilatorMapper ventilatorMapper;
    @Autowired
    UserSensorMapper userSensorMapper;
    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public JSONObject getSensorById(Integer id) {
        List<Integer> sensorId = userMapper.getSensorId(id);
        JSONObject res = new JSONObject();
        ArrayList<Monitor> monitors = new ArrayList<>();
        ArrayList<Oximeter> oximeters = new ArrayList<>();
        ArrayList<Oxycon> oxycons = new ArrayList<>();
        ArrayList<Ventilator> ventilators = new ArrayList<>();
        for (Integer i : sensorId) {
            switch (i/10) {
                case 1:
                    monitors.add(monitorMapper.getData(i));
                    break;
                case 2:
                    oximeters.add(oximeterMapper.getData(i));
                    break;
                case 3:
                    oxycons.add(oxyconMapper.getData(i));
                    break;
                case 4:
                    ventilators.add(ventilatorMapper.getData(i));
                    break;
            }
        }
        res.put("monitor", monitors);
        res.put("oximeter", oximeters);
        res.put("oxycon", oxycons);
        res.put("ventilator", ventilators);
        return res;
    }

    @Override
    public void updateSenor(Integer userId, List<Integer> subscribe) {
        LambdaQueryWrapper<UserSensor> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(UserSensor::getUserId, userId);
        userSensorMapper.delete(wrapper);
        UserSensor userSensor = new UserSensor();
        userSensor.setUserId(userId);
        for (Integer id : subscribe) {
            userSensor.setSensorId(id);
            userSensorMapper.insert(userSensor);
        }
    }
}
