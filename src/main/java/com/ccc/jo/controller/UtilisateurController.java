package com.ccc.jo.controller;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.service.UtilisateurService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.File;
import java.util.List;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

	@GetMapping("/")
	public String showAccueil() {
		return "accueil";
	}

    @GetMapping("/listeclients")
    public String getAllUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "utilisateurs";
    }

    @GetMapping("/inscription")
    public String showInscriptionForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") == null) {
            model.addAttribute("utilisateur", new Utilisateur());
            return "inscription";
        }else{
            return "accueil";
        }
    }

    @PostMapping("/inscription")
    public String createUtilisateur(Model model, @ModelAttribute("utilisateur") Utilisateur utilisateur) {
		if (utilisateurService.getUtilisateurByEmail(utilisateur.getEmail()) == null) {
			if (utilisateur.compareMotdepasse(utilisateur.getMotdepasse(), utilisateur.getConfirmermdp())) {
        	utilisateurService.createUtilisateur(utilisateur);
            //utilisateurService.sendEmailConfirmation(utilisateur); //envoi d'email temporairement desactivé
			model.addAttribute("messageSucces", "Vous vous etes inscrits avec succes");
            return "inscription";
			}else{
			model.addAttribute("messageErreur", "Les deux mots de passes sont differents");
			return "inscription";
		}}else{
			model.addAttribute("messageErreur", "Un utilisateur est deja inscrit avec cet email");
			return "inscription";
		}
    }

    @GetMapping("/active={tokenconfirm}")
	public String showConfirmutilisateur(Model model, @PathVariable String tokenconfirm, HttpSession session) {
        if (utilisateurService.getUtilisateurByTokenconfirm(tokenconfirm) != null){
            utilisateurService.activeUtilisateur(tokenconfirm);
            if (session.getAttribute("utilisateur") == null) {
            model.addAttribute("utilisateur", new Utilisateur());
            model.addAttribute("messageSucces", "Votre compte a ete activee avec succes");
            return "connexion";
        }else{
            return "accueil";
        }}else{
        return "accueil";
        }
	}

	@GetMapping("/connexion")
	public String showConnexionForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") == null) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "connexion";
        }else{
            return "accueil";
        }
	}

	@PostMapping("/connexion")
	public String loginUtilisateur(Model model, @ModelAttribute("utilisateur") Utilisateur utilisateur, HttpSession session) {
        Utilisateur loggedutilisateur = utilisateurService.loginUtilisateur(utilisateur);
		if (loggedutilisateur != null) {
            if (loggedutilisateur.getActive().equals(true)) {
                session.setAttribute("utilisateur", loggedutilisateur);
		        return "redirect:/";
            }else{
                model.addAttribute("messageErreur", "Le compte est inactif");
                return "connexion"; 
        }}else{
			model.addAttribute("messageErreur", "Email ou mot de passe incorrect");
			return "connexion";
        }	
    }

    @GetMapping("/compteclient")
    public String showEditForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") != null) {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            model.addAttribute("utilisateur", utilisateur);
            return "compteclient";
        } else {
            return "accueil";
        }
    }

    @PostMapping("/compteclient")
    public String updateUtilisateur(Model model, @ModelAttribute("utilisateur") Utilisateur updatedUtilisateur, @RequestParam String inputMdpact, @RequestParam String inputNouvmdp, @RequestParam String inputConfirmermdp, @RequestParam(value="action", required=true) String action, HttpSession session) {
        Utilisateur existingUtilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (action.equals("boutonMajprofil")) {
            if (updatedUtilisateur.getNom() != null && updatedUtilisateur.getPrenom() != null && updatedUtilisateur.getEmail() != null && updatedUtilisateur.getConfirmermdp() != null) {
                if (existingUtilisateur.compareMotdepasse(existingUtilisateur.getMotdepasse(), updatedUtilisateur.getConfirmermdp())) {
                    if (!existingUtilisateur.getNom().equals(updatedUtilisateur.getNom()) || !existingUtilisateur.getPrenom().equals(updatedUtilisateur.getPrenom()) || !existingUtilisateur.getEmail().equals(updatedUtilisateur.getEmail())) {
                        if (!existingUtilisateur.getEmail().equals(updatedUtilisateur.getEmail()) && utilisateurService.getUtilisateurByEmail(updatedUtilisateur.getEmail()) != null) {
                            model.addAttribute("messageErreur1", "Un autre utilisateur possede cet email");
                        } else {
                            updatedUtilisateur.setId(existingUtilisateur.getId());
                            updatedUtilisateur = utilisateurService.updateUtilisateur(updatedUtilisateur.getId(), updatedUtilisateur.getNom(), updatedUtilisateur.getPrenom(), updatedUtilisateur.getEmail());
                            session.setAttribute("utilisateur", updatedUtilisateur);
                            model.addAttribute("messageSucces1", "Votre profil a ete mis a jour avec succes");
                        }
                    } else {
                        model.addAttribute("messageErreur1", "Les nouvelles infos ne peuvent pas etre les memes que celles actuelles");
                    }
                } else {
                    model.addAttribute("messageErreur1", "Le mot de passe de confirmation est incorrect");
                }
            } else {
                model.addAttribute("messageErreur1", "Il faut entrer vos nouveaux infos ainsi que votre mot de passe pour confirmer le changement");
            }
        }
        if (action.equals("boutonMajmdp")) {
            if (inputMdpact != null && inputNouvmdp != null && inputConfirmermdp != null) {
                if (existingUtilisateur.getMotdepasse().equals(inputMdpact)) {
                    if (!inputMdpact.equals(inputNouvmdp)) {
                        if (inputNouvmdp.equals(inputConfirmermdp)) {
                            updatedUtilisateur.setId(existingUtilisateur.getId());
                            updatedUtilisateur = utilisateurService.updateMotdepasse(updatedUtilisateur.getId(), inputNouvmdp);                  
                            session.setAttribute("utilisateur", updatedUtilisateur);
                            model.addAttribute("messageSucces2", "Votre mot de passe a ete mis a jour avec succes");
                        } else {
                            model.addAttribute("messageErreur2", "Les deux nouveaux mots de passes sont differents");
                        }
                    } else {
                        model.addAttribute("messageErreur2", "Le nouveau mot de passe ne peut pas etre le meme que celui actuel");
                    }
                } else {
                    model.addAttribute("messageErreur2", "Le mot de passe actuel est incorrect");
                }
            } else {
                model.addAttribute("messageErreur2", "Au moins un des trois champs est vide");
            }
        }
        return "compteclient";
    }
}