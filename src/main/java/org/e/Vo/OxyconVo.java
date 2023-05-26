package org.e.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//肺功能仪
public class OxyconVo implements Serializable {
  int nid;
  float PEF; // U/min
  float FVC; // L

}
