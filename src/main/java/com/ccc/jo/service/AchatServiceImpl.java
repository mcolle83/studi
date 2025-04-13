package com.ccc.jo.service;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.repository.AchatRepository;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AchatServiceImpl implements AchatService {

    private final AchatRepository achatRepository;
    @Value("${spring.mail.username}")
    private String mailusername;
    @Value("${spring.mail.password}")
    private String mailpassword;

    @Autowired
    public AchatServiceImpl(AchatRepository achatRepository) {
        this.achatRepository = achatRepository;
    }

    @Override
    public List<Achat> getAllAchats() {
        return achatRepository.findAll();
    }

    @Override
    public Achat getAchatById(Long id) {
        return achatRepository.findById(id).orElse(null);
    }

    @Override
    public Long createAchat(Utilisateur utilisateur, BigDecimal prixtotal, LocalDateTime date) {
        Achat newAchat = new Achat();
        newAchat.setUtilisateur(utilisateur);
        newAchat.setPrixtotal(prixtotal);
        newAchat.setDate(date);
		newAchat.setCle(UUID.randomUUID().toString());
        return achatRepository.save(newAchat).getId();
    }

    @Override
    public void updateAchat(Achat updatedAchat) {
    }

    @Override
    public void deleteAchat(Long id) {
        achatRepository.deleteById(id);
    }

    @Override
    public List<Achat> getAllAchatsByUtilisateur(Utilisateur utilisateur) {
        return achatRepository.findAllByUtilisateur(utilisateur);
    }

    @Override
    public void sendEmailAchat(Utilisateur utilisateur, Achat achat) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.debug", "true");
        jakarta.mail.Authenticator auth = new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailusername, mailpassword);
            }
        };
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
            try {
            msg.setFrom(new InternetAddress(mailusername));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(utilisateur.getEmail()));
            msg.setSubject("Achat effectue");
            msg.setText("Bienvenue " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " ! Vous avez fait un achat le " + achat.getDate() + " . Pour recuperer le QRCode de chaque epreuve que vous avez achete, merci d'acceder a 'mes achats effectues' .");
            Transport.send(msg);
            } catch (MessagingException e) {
            System.out.println("Une erreur est arrivee lors de l envoi d email");
            }
    }
}