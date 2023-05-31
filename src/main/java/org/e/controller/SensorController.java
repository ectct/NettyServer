package org.e.controller;

import org.e.Vo.MonitorVo;
import org.e.Vo.OximeterVo;
import org.e.Vo.OxyconVo;
import org.e.Vo.VentilatorVo;
import org.e.entity.Monitor;
import org.e.entity.Oximeter;
import org.e.entity.Oxycon;
import org.e.entity.Ventilator;
import org.e.handle.MailHandle;
import org.e.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class SensorController {
    @Autowired
    MonitorService monitorService;
    @Autowired
    OximeterService oximeterService;
    @Autowired
    OxyconService oxyconService;
    @Autowired
    VentilatorService ventilatorService;
    @Autowired
    MailHandle mail;
    @Autowired
    UserService userService;
    public void monitorProcess(MonitorVo monitorVo) throws Exception {
        int nid = monitorVo.getNid();
        if (!(monitorVo.getPM25()<75)) {
            alert("PM2.5异常:"+monitorVo.getPM25(), nid);
        }

        if (!(monitorVo.getTemperature()>10 && monitorVo.getTemperature()<30)) {
            alert("温度异常:"+monitorVo.getTemperature(), nid);
        }

        if (!(monitorVo.getHumidity()>30 && monitorVo.getHumidity()<80)) {
            alert("湿度异常:"+monitorVo.getHumidity(), nid);
        }
        Monitor monitor = new Monitor();
        monitor.setTime(new Date());
        monitor.setSensorId(monitorVo.getNid());
        BeanUtils.copyProperties(monitorVo, monitor);
        monitorService.save(monitor);
    }
    public void oximeterProcess(OximeterVo oximeterVo) throws Exception {
        int nid = oximeterVo.getNid();
        if (!(oximeterVo.getPR()>50 && oximeterVo.getPR()<120)) {
            alert(nid);
        }
        if (!(oximeterVo.getSaO2()>80 && oximeterVo.getSaO2()<100)) {
            alert(nid);
        }
        Oximeter oximeter = new Oximeter();
        oximeter.setTime(new Date());
        oximeter.setSensorId(oximeterVo.getNid());
        BeanUtils.copyProperties(oximeterVo, oximeter);
        oximeterService.save(oximeter);
    }
    public void oxyconProcess(OxyconVo oxyconVo) throws Exception {
        int nid = oxyconVo.getNid();
        if (!(oxyconVo.getFVC()>1.5 && oxyconVo.getFVC()<5)) {
            alert(nid);
        }
        if (!(oxyconVo.getPEF()>300 && oxyconVo.getPEF()<600)) {
            alert(nid);
        }
        Oxycon oxycon = new Oxycon();
        oxycon.setTime(new Date());
        oxycon.setSensorId(oxyconVo.getNid());
        BeanUtils.copyProperties(oxyconVo, oxycon);
        oxyconService.save(oxycon);
    }

    public void ventilatorProcess(VentilatorVo ventilatorVo) throws Exception {
        int nid = ventilatorVo.getNid();
        if (!(ventilatorVo.getOxygen()>15 && ventilatorVo.getOxygen()<50)) {
            alert(nid);
        }

        if (!(ventilatorVo.getTidal_volume()>5 && ventilatorVo.getTidal_volume()<12)) {
            alert(nid);
        }

        if (!(ventilatorVo.getRespiratory_rate()>7 && ventilatorVo.getRespiratory_rate()<26)) {
            alert(nid);
        }
        Ventilator ventilator = new Ventilator();
        ventilator.setTime(new Date());
        ventilator.setSensorId(ventilatorVo.getNid());
        ventilator.setTidalVolume(ventilatorVo.getTidal_volume());
        ventilator.setRespiratoryRate(ventilatorVo.getRespiratory_rate());
        BeanUtils.copyProperties(ventilatorVo, ventilator);
        ventilatorService.save(ventilator);
    }
    private void alert(String msg, Integer id) throws Exception {
        String email = null;
        try {
            Integer userId = userService.getUserId(id);
            email = userService.getMail(userId);
        }
        catch (Exception e) {

            e.printStackTrace();
            return;
        }
        if (email != null) {
            mail.sendMail(email, msg);
        }
    }
    private void alert(Integer id) throws Exception {
        alert("异常",id);
    }

}
