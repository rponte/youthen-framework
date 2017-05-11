package com.youthen.framework.util.logging;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import com.youthen.framework.common.StringUtils;
import com.youthen.framework.common.fields.FieldSupportedMessage;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpSMTPAppender extends SMTPAppender {

    private static final String MESSEGE_KEY_OPTION_NOT_ACTIVE = "XFW72013";
    private static final String MESSEGE_KEY_ADDRESS_PARSE_FAILURE = "XFW72003";
    private static final String MESSAGE_KEY_MESSAGE_OBJECT_NOT_CONFIGURED = "XFW72004";
    private static final String MESSAGE_KEY_TRIGGER_EVENT_NOT_SET = "XFW72005";
    private static final String MESSAGE_KEY_OPTION_IS_INSUFFICIENT = "XFW72006";
    private static final String MESSAGE_KEY_SEND_MAIL_ERROR = "XFW72007";
    private static final String MESSAGE_KEY_CRLF_INJECTION_ERROR = "XFW72008";

    private static final String LT = "<";
    private static final String GT = ">";
    private static final String COMMMA = ",";

    private MimeMessage mimeMessage;

    /**
     */
    public SisqpSMTPAppender() {
        super();
        this.mimeMessage = null;
    }

    /**
     * {@inheritDoc}
     * 
     * <pre>
     * </pre>
     */
    @Override
    public void activateOptions() {

        try {
            final Session session = this.createSession();

            this.mimeMessage = new MimeMessage(session);

            this.addressMessage();

            if (StringUtils.isNotEmpty(this.getSubject()) && !this.hasCRLF(this.getSubject())) {
                this.mimeMessage.setSubject(this.getSubject(), this.getCharset());
            }

        } catch (final MessagingException e) {
            LogLog.error(new FieldSupportedMessage(MESSEGE_KEY_OPTION_NOT_ACTIVE).format("").getMesg(), e);
        }
    }

    /**
     * 
     */
    @Override
    protected Session createSession() {

        Properties props = null;

        try {
            props = new Properties(System.getProperties());
        } catch (final SecurityException ex) {
            props = new Properties();
        }

        if (StringUtils.isNotEmpty(this.getSMTPHost())) {
            props.put("mail.smtp.host", this.getSMTPHost());
        }

        if (this.getSMTPPort() > 0) {
            props.put("mail.smtp.port", Integer.valueOf(this.getSMTPPort()));
        }

        Authenticator auth = null;
        if (StringUtils.isNotEmpty(this.getSMTPPassword()) && StringUtils.isNotEmpty(this.getSMTPUsername())) {
            props.put("mail.smtp.auth", "true");
            auth = new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SisqpSMTPAppender.this.getSMTPUsername(), SisqpSMTPAppender.this
                            .getSMTPPassword());
                }
            };
        }

        final Session session = Session.getInstance(props, auth);

        if (this.getSMTPDebug()) {
            session.setDebug(this.getSMTPDebug());
        }
        return session;

    }

    /**
     * @see org.apache.log4j.net.SMTPAppender#addressMessage(javax.mail.Message)
     */
    @Override
    protected void addressMessage(final Message aMsg) throws MessagingException {
        LogLog.error(new FieldSupportedMessage("This method is deprecated.").format("").getMesg());
    }

    /**
     * 
     */
    void addressMessage() throws MessagingException {
        this.setMessageSendInfo(this.getFrom());

        this.setMessageReceiveInfo(this.getTo(), Message.RecipientType.TO);

        this.setMessageReceiveInfo(this.getCc(), Message.RecipientType.CC);

        this.setMessageReceiveInfo(this.getBcc(), Message.RecipientType.BCC);
    }

    /**
     * 
     */
    void setMessageSendInfo(final String aAddressStr) throws MessagingException {
        InternetAddress result = null;
        if (StringUtils.isNotEmpty(aAddressStr)) {
            result = this.toInternetAddress(aAddressStr);
            this.mimeMessage.setFrom(result);
        } else {
            LogLog.error(new FieldSupportedMessage(MESSAGE_KEY_OPTION_IS_INSUFFICIENT).format("From", this.name)
                    .getMesg());
        }
    }

    /**
     * 
     */
    void setMessageReceiveInfo(final String aAddressStr, final RecipientType aRecipientType) throws MessagingException {
        final InternetAddress[] addresses = this.parseMailAddress(aAddressStr);
        if (addresses != null && addresses.length > 0) {
            this.mimeMessage.setRecipients(aRecipientType, addresses);
        }
    }

    /**
     * 
     */
    InternetAddress[] parseMailAddress(final String aAddressStr) {
        InternetAddress[] result = null;

        if (StringUtils.isNotEmpty(aAddressStr)) {
            final String[] addresses = aAddressStr.split(COMMMA);
            result = new InternetAddress[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                result[i] = this.toInternetAddress(addresses[i]);
            }
        }
        return result;
    }

    /**
     * 
     */
    InternetAddress toInternetAddress(final String aAddressStr) {

        InternetAddress result = null;
        if (StringUtils.isNotEmpty(aAddressStr) && !this.hasCRLF(aAddressStr)) {
            final int namePosition = aAddressStr.indexOf(LT);
            try {
                if (namePosition >= 0) {
                    result = new InternetAddress(
                                    this.suplessLTGT(aAddressStr.substring(namePosition)),
                                    aAddressStr.substring(0, namePosition).trim(),
                                    this.getCharset());
                } else {
                    result = new InternetAddress(aAddressStr);
                }
            } catch (final AddressException e) {
                this.errorHandler.error(
                        new FieldSupportedMessage(MESSEGE_KEY_ADDRESS_PARSE_FAILURE).format(aAddressStr).getMesg(), e,
                        ErrorCode.ADDRESS_PARSE_FAILURE);
            } catch (final UnsupportedEncodingException e) {
                this.errorHandler.error(
                        new FieldSupportedMessage(MESSEGE_KEY_ADDRESS_PARSE_FAILURE).format(aAddressStr).getMesg(), e,
                        ErrorCode.ADDRESS_PARSE_FAILURE);
            }
        }

        return result;
    }

    /**
     * 
     */
    String suplessLTGT(final String aValue) {
        String result = aValue;
        result = result.replaceAll(LT, "");
        result = result.replaceAll(GT, "");
        return result;
    }

    /**
     * 
     */
    boolean hasCRLF(final String aValue) {
        if (StringUtils.isNotEmpty(aValue)) {
            final String[] checkStrs = {System.getProperty("line.separator"), "\r", "\n", "%0d", "%0a"};

            for (final String checkStr : checkStrs) {
                if (aValue.indexOf(checkStr.toUpperCase()) >= 0 || aValue.indexOf(checkStr.toLowerCase()) >= 0) {
                    LogLog.error(new FieldSupportedMessage(MESSAGE_KEY_CRLF_INJECTION_ERROR).format(aValue).getMesg());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * <pre>
     * </pre>
     */
    @Override
    protected boolean checkEntryConditions() {
        boolean result = false;

        if (this.mimeMessage == null) {
            this.errorHandler.error(new FieldSupportedMessage(MESSAGE_KEY_MESSAGE_OBJECT_NOT_CONFIGURED).getMesg());
        } else if (this.evaluator == null) {
            this.errorHandler.error(new FieldSupportedMessage(MESSAGE_KEY_TRIGGER_EVENT_NOT_SET).format(this.name)
                    .getMesg());
        } else if (this.layout == null) {
            this.errorHandler.error(new FieldSupportedMessage(MESSAGE_KEY_OPTION_IS_INSUFFICIENT).format("Layout",
                    this.name)
                    .getMesg());
        } else {
            result = true;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * <pre>
     * </pre>
     */
    @Override
    protected void sendBuffer() {

        try {
            final MimeBodyPart part = new MimeBodyPart();
            final StringBuffer sbuf = new StringBuffer();

            String t = this.layout.getHeader();
            if (t != null) {
                sbuf.append(t);
            }

            final int len = this.cb.length();
            for (int i = 0; i < len; i++) {
                final LoggingEvent event = this.cb.get();
                sbuf.append(this.layout.format(event));
                if (this.layout.ignoresThrowable()) {
                    final String[] s = event.getThrowableStrRep();
                    if (s != null) {
                        for (int j = 0; j < s.length; j++) {
                            sbuf.append(s[j]);
                            sbuf.append(Layout.LINE_SEP);
                        }
                    }
                }
            }

            t = this.layout.getFooter();
            if (t != null) {
                sbuf.append(t);
            }

            part.setContent(sbuf.toString(), this.getPpfwSMTPPatternLayout().getContentType());
            final Multipart mp = new MimeMultipart();
            mp.addBodyPart(part);
            this.mimeMessage.setContent(mp);

            this.mimeMessage.setSentDate(new Date());

            Transport.send(this.mimeMessage);
        } catch (final Exception e) {
            LogLog.error(new FieldSupportedMessage(MESSAGE_KEY_SEND_MAIL_ERROR).getMesg(), e);
        }
    }

    /**
     * @return Charset
     */
    public String getCharset() {
        return this.getPpfwSMTPPatternLayout().getCharset();
    }

    /**
     * 
     */
    public SisqpSMTPPatternLayout getPpfwSMTPPatternLayout() {
        return (SisqpSMTPPatternLayout) this.layout;
    }

}
