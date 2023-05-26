package org.e.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//监测器
public class MonitorVo implements Serializable {
  int nid;
  float PM25;   // PM2.5
  float temperature; // 温度 ℃
  float humidity; //湿度 %rh
}
