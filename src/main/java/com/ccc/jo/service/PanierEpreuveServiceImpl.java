package com.ccc.jo.service;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccc.jo.model.Epreuve;
import com.ccc.jo.model.Panier;
import com.ccc.jo.model.PanierEpreuve;
import com.ccc.jo.repository.PanierRepository;
import com.ccc.jo.repository.PanierEpreuveRepository;

@Service
public class PanierEpreuveServiceImpl implements PanierEpreuveService {
    private final PanierEpreuveRepository panierepreuveRepository;
    private final PanierRepository panierRepository;
    private final EpreuveService epreuveService;
    private final PanierService panierService;

    @Autowired
    public PanierEpreuveServiceImpl(PanierEpreuveRepository panierepreuveRepository, PanierRepository panierRepository, EpreuveService epreuveService, PanierService panierService) {
        this.panierepreuveRepository = panierepreuveRepository;
        this.panierRepository = panierRepository;
        this.epreuveService = epreuveService;
        this.panierService = panierService;
    }
    
    @Override
    public void addOffreToPanier(Long idpanier, Long idoffre, Integer quantite) {
        Panier panier = panierService.getPanierById(idpanier);
        Epreuve offre = epreuveService.getEpreuveById(idoffre);
        PanierEpreuve panierepreuve = panier.getEpreuves()
                .stream()
                .filter(item -> item.getEpreuve().getId().equals(idoffre))
                .findFirst().orElse(new PanierEpreuve());
        if (panierepreuve.getId() == null) {
            panierepreuve.setPanier(panier);
            panierepreuve.setEpreuve(offre);
            panierepreuve.setQuantite(quantite);
            panierepreuve.setPrixunitaire(offre.getPrix());
        }
        else {
            panierepreuve.setQuantite(quantite);
        }
        panierepreuve.setPrixtotal();
        panier.addOffre(panierepreuve);
        panierepreuveRepository.save(panierepreuve);
        panierRepository.save(panier);
    }

    @Override
    public void removeOffreFromPanier(Long idpanier, Long idoffre) {
        Panier panier = panierService.getPanierById(idpanier);
        PanierEpreuve offreretiree = getPanierEpreuve(idpanier, idoffre);
        panier.removeOffre(offreretiree);
        panierRepository.save(panier);
    }

    @Override
    public void updateOffreQuantite(Long idpanier, Long idoffre, Integer quantite) {
        Panier panier = panierService.getPanierById(idpanier);
        panier.getEpreuves()
                .stream()
                .filter(item -> item.getEpreuve().getId().equals(idoffre))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantite(quantite);
                    item.setPrixunitaire(item.getEpreuve().getPrix());
                    item.setPrixtotal();
                });
        BigDecimal prixtotal = panier.getEpreuves()
                .stream().map(PanierEpreuve ::getPrixtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        panier.setPrixtotal(prixtotal);
        panierRepository.save(panier);
    }

    @Override
    public PanierEpreuve getPanierEpreuve(Long idpanier, Long idoffre) {
        Panier panier = panierService.getPanierById(idpanier);
        return  panier.getEpreuves()
                .stream()
                .filter(item -> item.getEpreuve().getId().equals(idoffre))
                .findFirst().orElse(null);
    }

    @Override
    public Set<PanierEpreuve> getAllPanierEpreuves(Long idpanier) {
        Panier panier = panierService.getPanierById(idpanier);
        return  panier.getEpreuves();
    }
}