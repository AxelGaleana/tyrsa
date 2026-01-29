package com.tyrsa.api_erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyrsa.api_erp.model.Part;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTemporaryPassword(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("industrializacion@troqueladostyrsa.com");
        message.setTo(toEmail);
        message.setSubject("Contraseña temporal");
        message.setText(
            "Tu nueva contraseña temporal es: " + tempPassword +
            "\n\nPor favor, cámbiala después de iniciar sesión." +
            "\n\n----------------------------------" +
            "\nSistema de Industrialización" +
            "\nTyrsa Troquelados"
        );

        mailSender.send(message);
    }

    public void sendPartChageAprobalRequest(String[] toEmails, String partNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("industrializacion@troqueladostyrsa.com");
        message.setTo(toEmails);
        message.setSubject("Actualizacion de parte requiere su aprobacion");
        message.setText(
            "El item con número de parte: " + partNumber + " ha sido actualizado y requiere de su aprobación" +
            "\n\nPor favor, ingrese al sistema para ver los cambios." +
            "\n\n----------------------------------" +
            "\nSistema de Industrialización" +
            "\nTyrsa Troquelados"
        );

        mailSender.send(message);
    }

    public void newPartAdded(String[] toEmails, Part part) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("industrializacion@troqueladostyrsa.com");
        message.setTo(toEmails);
        message.setSubject("Alta de parte requiere su aprobacion");
        message.setText(
            "El item con numero de parte: " + part.getNumeroParte() + " y descripción '" + part.getDescripcion() + "'' ha sido dado de alta." +
           "\n\nPor favor, ingrese al sistema para ver los cambios." +
            "\n\n----------------------------------" +
            "\nSistema de Industrialización" +
            "\nTyrsa Troquelados"
        );

        mailSender.send(message);
    }

    public void partUpdated(String[] toEmails, Part part) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("industrializacion@troqueladostyrsa.com");
        message.setTo(toEmails);
        message.setSubject("Actualizacion de parte requiere su aprobacion");
        message.setText(
            "El item con numero de parte: " + part.getNumeroParte() + " y descripción '" + part.getDescripcion() + "'' ha sido actualizado." +
           "\n\nPor favor, ingrese al sistema para ver los cambios." +
            "\n\n----------------------------------" +
            "\nSistema de Industrialización" +
            "\nTyrsa Troquelados"
        );

        mailSender.send(message);
    }
}