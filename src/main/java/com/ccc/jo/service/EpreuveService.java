package com.ccc.jo.service;

import com.ccc.jo.model.Epreuve;

import java.util.List;

public interface EpreuveService {
    List<Epreuve> getAllEpreuves();

    Epreuve getEpreuveById(Long id);

    void createEpreuve(Epreuve epreuve);

    void updateEpreuve(Epreuve updatedEpreuve);

    void deleteEpreuve(Long id);

    void reduceEpreuveCapacite(Long id, Integer quantite);
}