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

    public void sendPartChageAprobalRequest(String[] toEmails, String partNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmails);
        message.setSubject("Actualizacion de parte requiere su aprobacion");
        message.setText("El numero de parte: " + partNumber + " ha sido actualizada y requiere de su aprobación" +
                        "\nPor favor, ingrese al sistema para ver los cambios.");

        mailSender.send(message);
    }
}