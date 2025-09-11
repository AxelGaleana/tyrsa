package com.tyrsa.api_erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTemporaryPassword(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Contraseña temporal");
        message.setText("Tu nueva contraseña temporal es: " + tempPassword + 
                        "\nPor favor, cámbiala después de iniciar sesión.");

        mailSender.send(message);
    }
}