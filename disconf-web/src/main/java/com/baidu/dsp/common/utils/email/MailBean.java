package com.baidu.dsp.common.utils.email;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.utils.email.EmailProperties;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 邮件发送公共类
 * 
 * @author modi
 * @version 1.0.0
 */
@Service
public class MailBean implements InitializingBean {

    private static Logger LOG = AopLogFactory.getLogger(MailBean.class);

    @Autowired
    private EmailProperties emailProperties;

    // mail sender
    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    /**
     * 发送html邮件
     * 
     * @throws MessagingException
     * @throws AddressException
     */
    public void sendHtmlMail(String from, String[] to, String title, String text) throws AddressException,
            MessagingException {

        long start = System.currentTimeMillis();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "GBK");

        InternetAddress[] toArray = new InternetAddress[to.length];
        for (int i = 0; i < to.length; i++) {
            toArray[i] = new InternetAddress(to[i]);
        }

        messageHelper.setFrom(new InternetAddress(from));
        messageHelper.setTo(toArray);
        messageHelper.setSubject(title);
        messageHelper.setText(text, true);
        mimeMessage = messageHelper.getMimeMessage();
        mailSender.send(mimeMessage);
        long end = System.currentTimeMillis();
        LOG.info("send mail start:" + start + " end :" + end);
    }

    /**
     * 设置邮箱host，用户名和密码
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender.setHost(emailProperties.getHost());

        if (!StringUtils.isEmpty(emailProperties.getUser())) {
            mailSender.setUsername(emailProperties.getUser());
        }

        if (!StringUtils.isEmpty(emailProperties.getPassword())) {
            mailSender.setPassword(emailProperties.getPassword());
        }

        if (!StringUtils.isEmpty(emailProperties.getPort())) {
            mailSender.setPort(emailProperties.getPortInteger());
        }
    }
}