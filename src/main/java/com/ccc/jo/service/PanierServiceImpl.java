package com.ccc.jo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import com.ccc.jo.model.Panier;
import com.ccc.jo.repository.PanierRepository;
import com.ccc.jo.repository.PanierEpreuveRepository;

@Service
public class PanierServiceImpl implements PanierService {
    private final PanierRepository panierRepository;
    private final PanierEpreuveRepository panierepreuveRepository;

    @Autowired
    public PanierServiceImpl(PanierRepository panierRepository, PanierEpreuveRepository panierepreuveRepository) {
        this.panierRepository = panierRepository;
        this.panierepreuveRepository = panierepreuveRepository;
    }

    @Override
    public Panier getPanierById(Long id) {
        Panier panier = panierRepository.findById(id).orElse(null);
        BigDecimal prixtotal = panier.getPrixtotal();
        panier.setPrixtotal(prixtotal);
        return panierRepository.save(panier);
    }

    @Transactional
    @Override
    public void clearPanier(Long id) {
        Panier panier = getPanierById(id);
        panierepreuveRepository.deleteAllByPanierId(id);
        panier.getEpreuves().clear();
        panierRepository.deleteById(id);
    }

    @Override
    public BigDecimal getPrixtotal(Long id) {
        Panier panier = getPanierById(id);
        return panier.getPrixtotal();
    }

    @Override
    public Long initializeNewPanier(String idsession) {
        Panier newpanier = new Panier();
        newpanier.setIdsession(idsession);
        return panierRepository.save(newpanier).getId();
    }

    @Override
    public Panier getPanierByIdsession(String idsession) {
        return panierRepository.findByIdsession(idsession);
    }
}