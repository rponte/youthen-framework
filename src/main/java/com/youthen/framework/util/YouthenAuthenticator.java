package com.youthen.framework.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class YouthenAuthenticator extends Authenticator {

    String userName = "";
    String password = "";

    public YouthenAuthenticator() {

    }

    public YouthenAuthenticator(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.userName, this.password);
    }
}
