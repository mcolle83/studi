package com.ccc.jo.service;

import com.ccc.jo.model.PanierEpreuve;

import java.util.Set;

public interface PanierEpreuveService {
    void addOffreToPanier(Long idpanier, Long idoffre, Integer quantite);
    void removeOffreFromPanier(Long idpanier, Long idoffre);
    void updateOffreQuantite(Long idpanier, Long idoffre, Integer quantite);
    PanierEpreuve getPanierEpreuve(Long idpanier, Long idoffre);
    Set<PanierEpreuve> getAllPanierEpreuves(Long idpanier);
}