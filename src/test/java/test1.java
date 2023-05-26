import org.e.Vo.MonitorVo;
import org.e.entity.Monitor;
import org.e.handle.MailHandle;
import org.e.utils.JWTUtil;
import org.e.utils.ProtostuffUtils;
import org.junit.Test;


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
        System.out.println(JWTUtil.getJwtToken(1));
    }
}
