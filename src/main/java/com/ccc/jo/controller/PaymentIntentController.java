package com.ccc.jo.controller;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.model.Panier;
import com.ccc.jo.model.PanierEpreuve;
import com.ccc.jo.model.Request;
import com.ccc.jo.model.Response;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.service.AchatEpreuveService;
import com.ccc.jo.service.AchatService;
import com.ccc.jo.service.EpreuveService;
import com.ccc.jo.service.PanierEpreuveService;
import com.ccc.jo.service.PanierService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentIntentController {

    private final PanierService panierService;
    private final PanierEpreuveService panierepreuveService;
    private final AchatService achatService;
    private final AchatEpreuveService achatepreuveService;
    private final EpreuveService epreuveService;

    @Autowired
    public PaymentIntentController(PanierService panierService, PanierEpreuveService panierepreuveService, AchatService achatService, AchatEpreuveService achatepreuveService, EpreuveService epreuveService) {
        this.panierService = panierService;
        this.panierepreuveService = panierepreuveService;
        this.achatService = achatService;
        this.achatepreuveService = achatepreuveService;
        this.epreuveService = epreuveService;
    }

    /**
    * Finalise le paiement et déplace le contenu du panier dans la liste des achats
    */
    @PostMapping("/create-payment-intent")
    public Response createPaymentIntent(@RequestBody Request request, HttpSession session)
            throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount() * 100L)
                        .putMetadata("productName",
                                request.getProductName())
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams
                                        .AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent intent =
                PaymentIntent.create(params);

        if (session.getAttribute("utilisateur") != null && panierService.getPanierByIdsession(session.getId()) != null){
                Panier panier = panierService.getPanierByIdsession(session.getId());
                Long idpanier = panier.getId();
                if (!panierepreuveService.getAllPanierEpreuves(panier.getId()).isEmpty()){
                Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
                LocalDateTime dateactuelle = LocalDateTime.now();
                Long idachat = achatService.createAchat(utilisateur, panier.getPrixtotal(), dateactuelle);
                Set<PanierEpreuve> panierepreuves = panierepreuveService.getAllPanierEpreuves(panier.getId());
                for (PanierEpreuve panierepreuve : panierepreuves) {
                        AchatEpreuve achatepreuve = new AchatEpreuve();
                        achatepreuve.setAchat(achatService.getAchatById(idachat));
                        achatepreuve.setEpreuve(panierepreuve.getEpreuve());
                        achatepreuve.setQuantite(panierepreuve.getQuantite());
                        achatepreuve.setPrixunitaire(panierepreuve.getPrixunitaire());
                        achatepreuveService.createAchatEpreuve(achatepreuve);
                        epreuveService.reduceEpreuveCapacite(panierepreuve.getEpreuve().getId(), panierepreuve.getQuantite());
                }
                Achat achat = achatService.getAchatById(idachat);
                achatService.sendEmailAchat(utilisateur, achat);
                panierService.clearPanier(idpanier);
        }}

        return new Response(intent.getId(),
                intent.getClientSecret());
    }
}