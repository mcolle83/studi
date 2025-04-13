package com.ccc.jo.service;

import com.ccc.jo.model.Panier;

import java.math.BigDecimal;

public interface PanierService {
    Panier getPanierById(Long id);
    void clearPanier(Long id);
    BigDecimal getPrixtotal(Long id);
    Long initializeNewPanier(String idsession);
    Panier getPanierByIdsession(String idsession);
}