package com.ccc.jo.controller;

import com.ccc.jo.model.Epreuve;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.service.EpreuveService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class EpreuveController {

    private final EpreuveService epreuveService;

    @Autowired
    public EpreuveController(EpreuveService epreuveService) {
        this.epreuveService = epreuveService;
    }

    /**
    * Accède à la liste des offres
    */
	@GetMapping("/offres")
	public String showOffres(Model model) {
        List<Epreuve> epreuves = epreuveService.getAllEpreuves();
        model.addAttribute("epreuves", epreuves);
		return "offres";
	}

    /**
    * Accède au formulaire de création d'offre
    */
    @GetMapping("/creationoffre")
	public String showCreateOffreForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") != null) {
        Utilisateur existingUtilisateur = (Utilisateur) session.getAttribute("utilisateur");
            if (existingUtilisateur.getRole().equals("Administrateur")) {
                model.addAttribute("epreuve", new Epreuve());
		        return "creationoffre";
            } else {
                return "accueil";
            }
        } else {
            return "accueil";
        }
	}

    /**
    * Crée une offre
    */
    @PostMapping("/creationoffre")
    public String createOffre(Model model, @ModelAttribute("epreuve") Epreuve epreuve) {
        epreuveService.createEpreuve(epreuve);
		model.addAttribute("messageSucces", "L'épreuve a été crée avec succès");
        return "creationoffre";
    }

    /**
    * Accède au formulaire de gestion des offres
    */
    @GetMapping("/gestionoffres")
	public String showOffresForm(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") != null) {
        Utilisateur existingUtilisateur = (Utilisateur) session.getAttribute("utilisateur");
            if (existingUtilisateur.getRole().equals("Administrateur")) {
                List<Epreuve> epreuves = epreuveService.getAllEpreuves();
                model.addAttribute("epreuves", epreuves);
                model.addAttribute("epreuve", new Epreuve());
		        return "gestionoffres";
            } else {
                return "accueil";
            }
        } else {
            return "accueil";
        }
	}

    /**
    * Met à jour une offre après avoir cliqué sur le bouton "Modifier l'offre" ou le bouton "Supprimer l'offre"
    */
    @PostMapping("/gestionoffres")
    public String updateOffre(Model model, @ModelAttribute("epreuve") Epreuve epreuve, @RequestParam(value="action", required=true) String action) {
        if (action.equals("boutonModifieroffre")) {
            epreuveService.updateEpreuve(epreuve);
		    model.addAttribute("messageSucces1", "L'épreuve a été modifiée avec succès");
        }
        if (action.equals("boutonSupprimeroffre")) {
            epreuveService.deleteEpreuve(epreuve.getId());
            model.addAttribute("messageSucces2", "L'épreuve a été supprimée avec succès");
        }
        List<Epreuve> epreuves = epreuveService.getAllEpreuves();
        model.addAttribute("epreuves", epreuves);
        return "gestionoffres";
    }
}