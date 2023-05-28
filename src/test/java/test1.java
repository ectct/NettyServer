import com.alibaba.fastjson.JSONObject;
import org.e.Vo.MonitorVo;
import org.e.entity.Monitor;
import org.e.handle.MailHandle;
import org.e.utils.JWTUtil;
import org.e.utils.ProtostuffUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;


public class test1 {
    @Test
    public void f() throws Exception {
        MailHandle mailHandle = new MailHandle();

        String receiveMailAccount = "2873531385@qq.com";
        mailHandle.sendMail(receiveMailAccount, "你好");
    }
    @Test
    public void f2() {
        MonitorVo monitorVo = new MonitorVo();
        monitorVo.setHumidity(11);
        MonitorVo monitorVo1 = new MonitorVo();
        byte[] serialize = ProtostuffUtils.serialize(monitorVo);
        MonitorVo monitorVo2 = ProtostuffUtils.deserialize(serialize, MonitorVo.class);
        System.out.println(monitorVo2);


    }
    @Test
    public void f3() {
        Monitor monitor = new Monitor();
        monitor.setId(1);
        monitor.setHumidity(2.2F);
        monitor.setTime(new Date());
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(2);
        arrayList.add(3);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monitor", monitor);
        jsonObject.put("li", arrayList);
        System.out.println(((ArrayList<Integer>) jsonObject.get("li")).get(0));
        System.out.println(((JSONObject) JSONObject.toJSON(monitor)).toJSONString());
        System.out.println(JSONObject.toJSONString(monitor));
    }
    @Test
    public void f4() {
        System.out.println(JWTUtil.getMemberIdByJwtToken(JWTUtil.getJwtToken(1)));
        System.out.println(13/10);
    }
}
