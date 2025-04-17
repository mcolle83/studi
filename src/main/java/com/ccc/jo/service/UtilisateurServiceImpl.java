package com.ccc.jo.service;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.repository.UtilisateurRepository;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

import java.util.*;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    /*private BCryptPasswordEncoder passwordEncoder;*/
    @Value("${spring.mail.username}")
    private String mailusername;
    @Value("${spring.mail.password}")
    private String mailpassword;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public Utilisateur getUtilisateurByEmailAndMotdepasse(String email, String motdepasse) {
        return utilisateurRepository.findByEmailAndMotdepasse(email, motdepasse);
    }

    @Override
    public Utilisateur getUtilisateurByTokenconfirm(String tokenconfirm) {
        return utilisateurRepository.findByTokenconfirm(tokenconfirm);
    }

    @Override
    public void createUtilisateur(Utilisateur utilisateur) {
        Utilisateur newUtilisateur = new Utilisateur();
        newUtilisateur.setNom(utilisateur.getNom());
		newUtilisateur.setPrenom(utilisateur.getPrenom());
        newUtilisateur.setEmail(utilisateur.getEmail());
        newUtilisateur.setMotdepasse(utilisateur.getMotdepasse());
        /*newUtilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));*/
		newUtilisateur.setCle(UUID.randomUUID().toString());
		newUtilisateur.setRole("Utilisateur");
        newUtilisateur.setActive(false);
        utilisateur.setTokenconfirm(UUID.randomUUID().toString());
        newUtilisateur.setTokenconfirm(utilisateur.getTokenconfirm());
        utilisateurRepository.save(newUtilisateur);
    }

    @Override
    public Utilisateur loginUtilisateur(Utilisateur utilisateur) {
        Utilisateur loggedutilisateur = getUtilisateurByEmailAndMotdepasse(utilisateur.getEmail(), utilisateur.getMotdepasse());
        return loggedutilisateur;
    }

    @Override
    public Utilisateur updateUtilisateur(Long id, String nom, String prenom, String email) {
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(id).orElse(null);
        updatedUtilisateur.setNom(nom);
        updatedUtilisateur.setPrenom(prenom);
        updatedUtilisateur.setEmail(email);
        utilisateurRepository.save(updatedUtilisateur);
        return updatedUtilisateur;
    }

    @Override
    public Utilisateur updateMotdepasse(Long id, String motdepasse) {
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(id).orElse(null);
        updatedUtilisateur.setMotdepasse(motdepasse);
        utilisateurRepository.save(updatedUtilisateur);
        return updatedUtilisateur;
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public void sendEmailConfirmation(Utilisateur utilisateur) {
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
        String link = "https://studi-e7e2bdae765d.herokuapp.com/active=" + utilisateur.getTokenconfirm();
            try {
            msg.setFrom(new InternetAddress(mailusername));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(utilisateur.getEmail()));
            msg.setSubject("Activation du compte");
            msg.setText("Bienvenue " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " ! Pour activer votre compte sur le site de réversation des JO 2024, cliquez sur ce lien : " + link);
            Transport.send(msg);
            } catch (MessagingException e) {
            System.out.println("Une erreur est arrivée lors de l'envoi d'email");
            }
    }

    @Override
    public void activeUtilisateur(String tokenconfirm) {
        Utilisateur activeUtilisateur = utilisateurRepository.findByTokenconfirm(tokenconfirm);
        activeUtilisateur.setActive(true);
        utilisateurRepository.save(activeUtilisateur);
    }
}