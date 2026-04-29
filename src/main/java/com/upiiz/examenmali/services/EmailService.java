package com.upiiz.examenmali.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarRecuperacion(String destinatario, String password, String nombre) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, false);

            helper.setTo(destinatario);
            helper.setSubject("Recuperación de Acceso - TechRepair ERP");
            helper.setText("Hola " + nombre + ",\n\nHas solicitado recuperar tu contraseña para el sistema de reparaciones.\n" +
                    "Tu contraseña actual es: " + password + "\n\n" +
                    "Te recomendamos cambiarla una vez que logres ingresar.\n\nSaludos,\nSoporte Técnico TechRepair.");

            mailSender.send(mensaje);
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}