package com.youthen.framework.util;

import java.io.File;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSender {

    static String SSL_EMAIL = "javax.net.ssl.SSLSocketFactory";

    public static void send(String to, final String subject, final String content) {
        try {
            final String smtpHost = "smtp.youthentech.com";

            final String account = "wangyc@youthentech.com";
            final String password = "Lijia123";
            to = account;
            final Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "true");

            final Session mailSession = Session.getInstance(props, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(account, password);
                }
            });

            final InternetAddress fromAddress = new InternetAddress(account);
            final InternetAddress toAddress = new InternetAddress(to);

            final MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(fromAddress);
            message.addRecipient(RecipientType.TO, toAddress);

            message.setSentDate(Calendar.getInstance().getTime());
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=utf-8");

            final Transport transport = mailSession.getTransport("smtp");
            transport.connect(smtpHost, account, password);
            transport.send(message, message.getRecipients(RecipientType.TO));

        } catch (final MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        try {
            final JavaMailSenderImpl senderMail = new JavaMailSenderImpl();

            // 设定 Mail Server
            senderMail.setHost("smtp.163.com");
            senderMail.setPort(25);

            final Properties prop = new Properties();
            prop.setProperty("mail.smtp.auth", "true");

            // SMTP验证时，需要用户名和密码
            senderMail.setUsername("lixinustc@163.com");
            senderMail.setPassword("wy163123");
            senderMail.setJavaMailProperties(prop); // 如果要密码验证,这里必须加,不然会报553错误

            // 建立简单的邮件信息
            final SimpleMailMessage mailMessage = new SimpleMailMessage();

            // 设定收件人、寄件人、主题与内文
            mailMessage.setTo("70917176@qq.com");
            mailMessage.setFrom("lixinustc@163.com");// 这里必须和用户名一样,不然会报553错误
            mailMessage.setSubject("小子");
            mailMessage.setText("小子想死吧。。。。。。!!");

            // 传送邮件
            senderMail.send(mailMessage);

            // 发送HTML格式的邮件
            // 建立邮件信息，可发送HTML格式
            final MimeMessage mimeMessage = senderMail.createMimeMessage(); // MimeMessage-->java的
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "GBK"); // MimeMessageHelper-->spring的
                                                                                                     // 不加后面两个参数会乱码

            // 设置收件人，主题，内容
            messageHelper.setSubject("Hello! ");
            messageHelper.setFrom("Goldcane@163.com");
            messageHelper.setTo("admin@126.com");

            final StringBuffer str = new StringBuffer();
            str.append("<html><head></head><body><h1>Hello! 中文! </h1></body></html>");
            messageHelper.setText(str.toString(), true); // 为true-->发送转义HTML

            // senderMail.send(mimeMessage); //这个是不带附件的

            // 发送带附件的
            final FileSystemResource file =
                    new FileSystemResource(new File(
                            "E:\\DevelopmentSoft\\spring-framework-3.0.5.RELEASE\\docs\\javadoc-api\\index.html"));
            messageHelper.addAttachment("index.html", file);

            senderMail.send(mimeMessage); // 这个是发送带附件的

            System.out.println("邮件传送OK..");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
