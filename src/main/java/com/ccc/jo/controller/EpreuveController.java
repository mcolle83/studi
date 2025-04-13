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

	@GetMapping("/offres")
	public String showOffres(Model model) {
        List<Epreuve> epreuves = epreuveService.getAllEpreuves();
        model.addAttribute("epreuves", epreuves);
		return "offres";
	}

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

    @PostMapping("/creationoffre")
    public String createOffre(Model model, @ModelAttribute("epreuve") Epreuve epreuve) {
        epreuveService.createEpreuve(epreuve);
		model.addAttribute("messageSucces", "L epreuve a ete cree avec succes");
        return "creationoffre";
    }

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

    @PostMapping("/gestionoffres")
    public String updateOffre(Model model, @ModelAttribute("epreuve") Epreuve epreuve, @RequestParam(value="action", required=true) String action) {
        if (action.equals("boutonModifieroffre")) {
            epreuveService.updateEpreuve(epreuve);
		    model.addAttribute("messageSucces1", "L epreuve a ete modifiee avec succes");
        }
        if (action.equals("boutonSupprimeroffre")) {
            epreuveService.deleteEpreuve(epreuve.getId());
            model.addAttribute("messageSucces2", "L epreuve a ete supprimee avec succes");
        }
        return "gestionoffres";
    }
}