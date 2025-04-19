package com.ccc.jo.controller;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.service.UtilisateurService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/mentionslegales")
	public String showMentionsLegales() {
		return "mentionslegales";
	}

    @GetMapping("/politiqueconfid")
	public String showPolitiqueConfid() {
		return "politiqueconfid";
	}

    @GetMapping("/cgv")
	public String showCGV() {
		return "cgv";
	}

    @GetMapping("/contact")
	public String showContact() {
		return "contact";
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
            utilisateurService.sendEmailConfirmation(utilisateur);
			model.addAttribute("messageSucces", "Vous vous êtes inscrits avec succès");
            return "inscription";
			}else{
			model.addAttribute("messageErreur", "Les deux mots de passes sont différents");
			return "inscription";
		}}else{
			model.addAttribute("messageErreur", "Un utilisateur est déjà inscrit avec cet email");
			return "inscription";
		}
    }

    @GetMapping("/active={tokenconfirm}")
	public String showConfirmutilisateur(Model model, @PathVariable String tokenconfirm, HttpSession session) {
        if (utilisateurService.getUtilisateurByTokenconfirm(tokenconfirm) != null){
            utilisateurService.activeUtilisateur(tokenconfirm);
            if (session.getAttribute("utilisateur") == null) {
            model.addAttribute("utilisateur", new Utilisateur());
            model.addAttribute("messageSucces", "Votre compte a été activée avec succès");
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
                if (utilisateurService.verifMotdepasse(updatedUtilisateur.getConfirmermdp(), existingUtilisateur.getMotdepasse())) {
                    if (!existingUtilisateur.getNom().equals(updatedUtilisateur.getNom()) || !existingUtilisateur.getPrenom().equals(updatedUtilisateur.getPrenom()) || !existingUtilisateur.getEmail().equals(updatedUtilisateur.getEmail())) {
                        if (!existingUtilisateur.getEmail().equals(updatedUtilisateur.getEmail()) && utilisateurService.getUtilisateurByEmail(updatedUtilisateur.getEmail()) != null) {
                            model.addAttribute("messageErreur1", "Un autre utilisateur possede cet email");
                        } else {
                            updatedUtilisateur.setId(existingUtilisateur.getId());
                            updatedUtilisateur = utilisateurService.updateUtilisateur(updatedUtilisateur.getId(), updatedUtilisateur.getNom(), updatedUtilisateur.getPrenom(), updatedUtilisateur.getEmail());
                            session.setAttribute("utilisateur", updatedUtilisateur);
                            model.addAttribute("messageSucces1", "Votre profil a été mis à jour avec succès");
                        }
                    } else {
                        model.addAttribute("messageErreur1", "Les nouvelles infos ne peuvent pas être les mêmes que celles actuelles");
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
                if (utilisateurService.verifMotdepasse(inputMdpact, existingUtilisateur.getMotdepasse())) {
                    if (!inputMdpact.equals(inputNouvmdp)) {
                        if (inputNouvmdp.equals(inputConfirmermdp)) {
                            updatedUtilisateur.setId(existingUtilisateur.getId());
                            updatedUtilisateur = utilisateurService.updateMotdepasse(updatedUtilisateur.getId(), inputNouvmdp);                  
                            session.setAttribute("utilisateur", updatedUtilisateur);
                            model.addAttribute("messageSucces2", "Votre mot de passe a été mis à jour avec succès");
                        } else {
                            model.addAttribute("messageErreur2", "Les deux nouveaux mots de passes sont différents");
                        }
                    } else {
                        model.addAttribute("messageErreur2", "Le nouveau mot de passe ne peut pas être le même que celui actuel");
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