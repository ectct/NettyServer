package org.e.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//血氧计
public class OximeterVo implements Serializable {
  int nid;
  float SaO2; // 血氧饱和度 %
  float PR;  // 脉率 BPM
}
