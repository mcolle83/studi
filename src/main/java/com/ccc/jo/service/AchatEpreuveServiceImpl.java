package com.ccc.jo.service;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.model.Panier;
import com.ccc.jo.model.PanierEpreuve;
import com.ccc.jo.model.Utilisateur;
import com.ccc.jo.repository.AchatRepository;
import com.ccc.jo.repository.AchatEpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class AchatEpreuveServiceImpl implements AchatEpreuveService {

    private final AchatEpreuveRepository achatepreuveRepository;
    private final AchatRepository achatRepository;
    private final AchatService achatService;
    private final EpreuveService epreuveService;

    @Autowired
    public AchatEpreuveServiceImpl(AchatEpreuveRepository achatepreuveRepository, AchatRepository achatRepository, AchatService achatService, EpreuveService epreuveService) {
        this.achatepreuveRepository = achatepreuveRepository;
        this.achatRepository = achatRepository;
        this.achatService = achatService;
        this.epreuveService = epreuveService;
    }

    @Override
    public List<AchatEpreuve> getAllAchatEpreuves() {
        return achatepreuveRepository.findAll();
    }

    @Override
    public AchatEpreuve getAchatEpreuveById(Long id) {
        return achatepreuveRepository.findById(id).orElse(null);
    }

    @Override
    public void createAchatEpreuve(AchatEpreuve achatepreuve) {
        AchatEpreuve newAchatepreuve = new AchatEpreuve();
        newAchatepreuve.setAchat(achatepreuve.getAchat());
        newAchatepreuve.setEpreuve(achatepreuve.getEpreuve());
        newAchatepreuve.setQuantite(achatepreuve.getQuantite());
        newAchatepreuve.setPrixunitaire(achatepreuve.getPrixunitaire());
        newAchatepreuve.setPrixtotal();
        achatepreuveRepository.save(newAchatepreuve);
    }

    @Override
    public void updateAchatEpreuve(AchatEpreuve updatedAchatepreuve) {
    }

    @Override
    public void deleteAchatEpreuve(Long id) {
        achatepreuveRepository.deleteById(id);
    }

    @Override
    public Set<AchatEpreuve> getAllAchatEpreuves(Long idachat) {
        Achat achat = achatService.getAchatById(idachat);
        return  achat.getEpreuves();
    }
}