package org.e.controller;

import org.e.Vo.MonitorVo;
import org.e.Vo.OximeterVo;
import org.e.Vo.OxyconVo;
import org.e.Vo.VentilatorVo;
import org.e.entity.Monitor;
import org.e.entity.Oximeter;
import org.e.entity.Oxycon;
import org.e.entity.Ventilator;
import org.e.service.MonitorService;
import org.e.service.OximeterService;
import org.e.service.OxyconService;
import org.e.service.VentilatorService;
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
    public void monitorProcess(MonitorVo monitorVo) {
        if (!(monitorVo.getPM25()<75)) {

        }

        if (!(monitorVo.getTemperature()>10 && monitorVo.getTemperature()<30)) {

        }

        if (!(monitorVo.getHumidity()>30 && monitorVo.getHumidity()<80)) {

        }
        Monitor monitor = new Monitor();
        monitor.setTime(new Date());
        monitor.setSensorId(monitorVo.getNid());
        BeanUtils.copyProperties(monitorVo, monitor);
        System.out.println(monitorVo.toString());
        monitorService.save(monitor);
    }
    public void oximeterProcess(OximeterVo oximeterVo) {
        if (!(oximeterVo.getPR()>50 && oximeterVo.getPR()<120)) {

        }
        if (!(oximeterVo.getSaO2()>80 && oximeterVo.getSaO2()<100)) {

        }
        Oximeter oximeter = new Oximeter();
        oximeter.setTime(new Date());
        oximeter.setSensorId(oximeterVo.getNid());
        BeanUtils.copyProperties(oximeterVo, oximeter);
        oximeterService.save(oximeter);
    }
    public void oxyconProcess(OxyconVo oxyconVo) {
        if (!(oxyconVo.getFVC()>1.5 && oxyconVo.getFVC()<5)) {

        }
        if (!(oxyconVo.getPEF()>300 && oxyconVo.getPEF()<600)) {

        }
        Oxycon oxycon = new Oxycon();
        oxycon.setTime(new Date());
        oxycon.setSensorId(oxyconVo.getNid());
        BeanUtils.copyProperties(oxyconVo, oxycon);
        oxyconService.save(oxycon);
    }

    public void ventilatorProcess(VentilatorVo ventilatorVo) {
        if (!(ventilatorVo.getOxygen()>15 && ventilatorVo.getOxygen()<50)) {

        }

        if (!(ventilatorVo.getTidal_volume()>5 && ventilatorVo.getTidal_volume()<12)) {

        }

        if (!(ventilatorVo.getRespiratory_rate()>7 && ventilatorVo.getRespiratory_rate()<26)) {

        }
        Ventilator ventilator = new Ventilator();
        ventilator.setTime(new Date());
        ventilator.setSensorId(ventilatorVo.getNid());
        ventilator.setTidalVolume(ventilatorVo.getTidal_volume());
        ventilator.setRespiratoryRate(ventilatorVo.getRespiratory_rate());
        BeanUtils.copyProperties(ventilatorVo, ventilator);
        ventilatorService.save(ventilator);
    }
}
