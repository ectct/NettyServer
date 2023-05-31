package org.e.handle;

import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class MailHandle {

    private final String myEmailAccount = "lqm5823@163.com";
    private final String myEmailPassword = "wanlongyi";

    private final String myEmailSMTPHost = "smtp.163.com";

    public Session session;
    public MailHandle() throws Exception {
        Properties props = new Properties();                    //参数配置
        props.setProperty("mail.transport.protocol", "smtp");   //使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   //发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            //需要请求认证
        session = Session.getInstance(props);

    }

    public void sendMail(String receiveMailAccount, String msg) throws Exception {
        MimeMessage message = createMimeMessage(receiveMailAccount, msg);
        Transport transport = session.getTransport();
        transport.connect(myEmailAccount, myEmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        System.out.println("邮件发送成功");
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     */
    private MimeMessage createMimeMessage(String receiveMail, String msg) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myEmailAccount, "Netty服务器", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "app用户", "UTF-8"));
        message.setSubject("检测到异常", "UTF-8");
        message.setContent(msg, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
}