package com.service;

import javax.mail.MessagingException;
import java.io.IOException;

public interface SendMessageService {
    public void sendOtpMessage(String to, String subject, String message) throws MessagingException, IOException;
}
