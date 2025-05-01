package com.ccc.jo.service;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.repository.UtilisateurRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.*;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

import javax.imageio.ImageIO;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
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
    public Utilisateur getUtilisateurByTokenconfirm(String tokenconfirm) {
        return utilisateurRepository.findByTokenconfirm(tokenconfirm);
    }

    @Override
    public void createUtilisateur(Utilisateur utilisateur) {
        Utilisateur newUtilisateur = new Utilisateur();
        newUtilisateur.setNom(utilisateur.getNom());
		newUtilisateur.setPrenom(utilisateur.getPrenom());
        newUtilisateur.setEmail(utilisateur.getEmail());
        newUtilisateur.setMotdepasse(BCrypt.hashpw(utilisateur.getMotdepasse(), BCrypt.gensalt(12)));
		newUtilisateur.setCle(UUID.randomUUID().toString());
		newUtilisateur.setRole("Utilisateur");
        newUtilisateur.setActive(false);
        utilisateur.setTokenconfirm(UUID.randomUUID().toString());
        newUtilisateur.setTokenconfirm(utilisateur.getTokenconfirm());
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        utilisateur.setGauthsecret(key.getKey());
        newUtilisateur.setGauthsecret(utilisateur.getGauthsecret());
        utilisateurRepository.save(newUtilisateur);
    }

    @Override
    public Boolean verifMotdepasse(String motdepasse, String cryptedMotdepasse) {
        if (BCrypt.checkpw(motdepasse, cryptedMotdepasse))
        {
        return true;
        }else{
        return false;
        }
    }

    @Override
    public Utilisateur loginUtilisateur(Utilisateur utilisateur) {
        Utilisateur loggedUtilisateur = getUtilisateurByEmail(utilisateur.getEmail());
        if (loggedUtilisateur != null){
            String cryptedMotdepasse = loggedUtilisateur.getMotdepasse();
            if (BCrypt.checkpw(utilisateur.getMotdepasse(), cryptedMotdepasse))
            {
            return loggedUtilisateur;
            }else{
            return null;
            }
        }else{
        return null;
        }
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
        updatedUtilisateur.setMotdepasse(BCrypt.hashpw(motdepasse, BCrypt.gensalt(12)));
        utilisateurRepository.save(updatedUtilisateur);
        return updatedUtilisateur;
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public void sendEmailConfirmation(Utilisateur utilisateur) {
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
            "mcolle83 JO Studi",
            utilisateur.getEmail(),
            new GoogleAuthenticatorKey.Builder(utilisateur.getGauthsecret()).build());
        String qrCode = null;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200, hintMap);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            qrCode = Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
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
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Bienvenue " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " !" + "\n\n" + "Pour activer votre compte sur le site de réversation des JO 2024, cliquez sur ce lien : " + link + "\n\n" + "Pour obtenir votre code OTP, scannez le QR Code, dans la pièce jointe, avec Google Authenticator.");
            multipart.addBodyPart(textPart);
            if (qrCode != null) {
            MimeBodyPart imgPart = new MimeBodyPart();
            byte[] rawImg = Base64.getDecoder().decode(qrCode);
            ByteArrayDataSource imgDataSource = new ByteArrayDataSource(rawImg,"image/png");
            imgPart.setDataHandler(new DataHandler(imgDataSource));
            imgPart.setHeader("Content-ID", "<qrImage>");
            imgPart.setFileName("GAuthQRCode.png");
            multipart.addBodyPart(imgPart);
            }
            msg.setContent(multipart);
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

    @Override
    public boolean isValidotp(String secret, Integer code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator(
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build()
        );
        return gAuth.authorize(secret, code);
    }
}