package org.e.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//呼吸机
public class VentilatorVo implements Serializable {
  int nid;
  float respiratory_rate; // 呼吸频率 次/min
  float Tidal_volume;  //  潮气量 mL/kg
  float oxygen;      //  氧浓度 %
}
