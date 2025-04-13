package com.ccc.jo.service;

import java.util.List;
import java.util.Set;

import com.ccc.jo.model.AchatEpreuve;

public interface AchatEpreuveService {
    List<AchatEpreuve> getAllAchatEpreuves();

    AchatEpreuve getAchatEpreuveById(Long id);

    void createAchatEpreuve(AchatEpreuve achatepreuve);

    void updateAchatEpreuve(AchatEpreuve updatedAchatepreuve);

    void deleteAchatEpreuve(Long id);

    Set<AchatEpreuve> getAllAchatEpreuves(Long idachat);
}