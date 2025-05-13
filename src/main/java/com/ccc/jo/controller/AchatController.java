package com.ccc.jo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.service.AchatEpreuveService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AchatController {

    private final AchatEpreuveService achatepreuveService;

    @Autowired
    public AchatController(AchatEpreuveService achatepreuveService) {
        this.achatepreuveService = achatepreuveService;
    }

    /**
    * Accède à la liste des achats effectués par l'utilisateur
    */
    @GetMapping("/listeachatsutil")
	public String showAchatsutil(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") != null) {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            List<AchatEpreuve> achatepreuve = achatepreuveService.getAllAchatEpreuves();
            model.addAttribute("achatepreuve", achatepreuve);
		    return "listeachatsutil";
        } else {
        return "accueil";
        }
	}

    /**
    * Accède à la liste de tous les achats effectués
    */
    @GetMapping("/listeachatsadmin")
	public String showAchatsadmin(Model model, HttpSession session) {
        if (session.getAttribute("utilisateur") != null) {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            if (utilisateur.getRole().equals("Administrateur")) {
            List<AchatEpreuve> achatepreuve = achatepreuveService.getAllAchatEpreuves();
            Long quantitesolo = achatepreuveService.getAchatEpreuveCountByQuantite(1);
            Long quantiteduo = achatepreuveService.getAchatEpreuveCountByQuantite(2);
            Long quantitefamille = achatepreuveService.getAchatEpreuveCountByQuantite(4);
            model.addAttribute("quantitesolo", quantitesolo);
            model.addAttribute("quantiteduo", quantiteduo);
            model.addAttribute("quantitefamille", quantitefamille);
            model.addAttribute("achatepreuve", achatepreuve);
		    return "listeachatsadmin";
            } else {
            return "accueil";
            }
        } else {
        return "accueil";
        }
	}
}
