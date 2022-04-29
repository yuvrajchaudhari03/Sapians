package com.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class SendTextMessageService implements SendMessageService {
    @Override
    public void sendOtpMessage(String to, String subject, String message) throws MessagingException, IOException {
        try {
            // Construct data
            String apiKey = "apikey=" + "NzE2ZTM4NzU0NDUzNmU1NzQ3NDY0NTc3NTgzMTUzNmQ=";
            message = "&message=" + message;
            String sender = "&sender=" + "Sapien";
            String numbers = "&numbers=" + to;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
        } catch (IOException e) {
            System.out.println("Error SMS "+e);
            throw e;
        }
    }
}
