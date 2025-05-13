package com.ccc.jo.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.model.Panier;
import com.ccc.jo.model.Epreuve;
import com.ccc.jo.model.PanierEpreuve;
import com.ccc.jo.model.Request;
import com.ccc.jo.service.PanierService;
import com.ccc.jo.service.PanierEpreuveService;

import jakarta.servlet.http.HttpSession;

import java.util.Set;

@Controller
public class PanierController {

    private final PanierService panierService;
    private final PanierEpreuveService panierepreuveService;
    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @Autowired
    public PanierController(PanierService panierService, PanierEpreuveService panierepreuveService) {
        this.panierService = panierService;
        this.panierepreuveService = panierepreuveService;
    }
    
    /**
    * Ajoute une épreuve dans le panier après avoir cliqué soit sur le bouton "Réserver solo (1 place)", soit sur le bouton "Réserver duo (2 places)", soit sur le bouton "Réserver famille (4 places)"
    */
    @PostMapping("/offres")
	public String addPanier(Model model, @ModelAttribute("epreuve") Epreuve epreuve, @RequestParam(value="action", required=true) String action, HttpSession session) {
        String idsession = session.getId();
        Long idoffre = Long.parseLong(action.replaceAll("[^0-9]", ""));
        Long idpanier = null;
        Integer quantite = null;
        String boutonReserverSolo = "boutonReserverSolo" + idoffre;
        String boutonReserverDuo = "boutonReserverDuo" + idoffre;
        String boutonReserverFamille = "boutonReserverFamille" + idoffre;
        if (action.equals(boutonReserverSolo)) {
            quantite = 1;
        }
        if (action.equals(boutonReserverDuo)) {
            quantite = 2;
        }
        if (action.equals(boutonReserverFamille)) {
            quantite = 4;
        }
        
        if (panierService.getPanierByIdsession(idsession) == null) {
            idpanier = panierService.initializeNewPanier(idsession);
        }else{
            idpanier = panierService.getPanierByIdsession(idsession).getId();
        }
        panierepreuveService.addOffreToPanier(idpanier, idoffre, quantite);
        return "redirect:/offres";
	}

    /**
    * Accède au panier
    */
    @GetMapping("/panier")
	public String showPanier(Model model, HttpSession session) {
        String idsession = session.getId();
        Panier panier = null;
        Long idpanier = null;
        Set<PanierEpreuve> panierepreuve = null;
        if (panierService.getPanierByIdsession(idsession) != null) {
        panier = panierService.getPanierByIdsession(idsession);
        idpanier = panier.getId();
        panierepreuve = panierepreuveService.getAllPanierEpreuves(idpanier);
        }
        model.addAttribute("panierepreuve", panierepreuve);
        model.addAttribute("panier", panier);
		return "panier";
	}

    /**
    * Modifie le nombre de places réservées pour une épreuve après avoir cliqué sur le bouton "-" ou le bouton "+"
    */
    @PostMapping("/panier")
	public String manageOffre(Model model, @RequestParam(value="action", required=true) String action, HttpSession session) {
        Long idpanier = panierService.getPanierByIdsession(session.getId()).getId();
        Long idoffre = Long.parseLong(action.replaceAll("[^0-9]", ""));
        String boutonMoins = "boutonMoins" + idoffre;
        String boutonPlus = "boutonPlus" + idoffre;
        String boutonSupprimer = "boutonSupprimer" + idoffre;
        if (action.equals(boutonMoins)) {
            Integer quantite = panierepreuveService.getPanierEpreuve(idpanier, idoffre).getQuantite();
            --quantite;
            if (quantite < 1) {quantite = 1;} else if (quantite == 3) {quantite = 2;}
            panierepreuveService.updateOffreQuantite(idpanier, idoffre, quantite);
        }
        if (action.equals(boutonPlus)) {
            Integer quantite = panierepreuveService.getPanierEpreuve(idpanier, idoffre).getQuantite();
            ++quantite;
            if (quantite == 3) {quantite = 4;} else if (quantite > 4) {quantite = 4;}
            panierepreuveService.updateOffreQuantite(idpanier, idoffre, quantite);
        }
        if (action.equals(boutonSupprimer)) {
            panierepreuveService.removeOffreFromPanier(idpanier, idoffre);
        }
		return "redirect:/panier";
	}

    /**
    * Accède au paiement
    */
    @GetMapping("/paiement")
    public String showCard(Request request, Model model, HttpSession session){
        if (panierService.getPanierByIdsession(session.getId()) == null || session.getAttribute("utilisateur") == null){
            return "accueil";
        }
        Panier panier = panierService.getPanierByIdsession(session.getId());
        if (panierepreuveService.getAllPanierEpreuves(panier.getId()).isEmpty()){
            return "accueil";
        }
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        request.setAmount(panier.getPrixtotal().longValue());
        request.setEmail(utilisateur.getEmail());
        request.setProductName("Reservation JO");
        model.addAttribute("publicKey", publicKey);
        model.addAttribute("amount", request.getAmount());
        model.addAttribute("email", request.getEmail());
        model.addAttribute("productName", request.getProductName());
        return "checkout";
    }

    /**
    * Se déconnecte de la session et vide le panier
    */
	@GetMapping("/deconnexion")
	public String logoutUtilisateur(Model model, HttpSession session) {
        if (panierService.getPanierByIdsession(session.getId()) != null) {
        Panier panier = panierService.getPanierByIdsession(session.getId());
        Long idpanier = panier.getId();
        panierService.clearPanier(idpanier);
        }
		session.invalidate();
		return "redirect:/";
	}
}
