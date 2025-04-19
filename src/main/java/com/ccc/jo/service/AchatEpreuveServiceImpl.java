package com.ccc.jo.service;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.repository.AchatEpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AchatEpreuveServiceImpl implements AchatEpreuveService {

    private final AchatEpreuveRepository achatepreuveRepository;
    private final AchatService achatService;

    @Autowired
    public AchatEpreuveServiceImpl(AchatEpreuveRepository achatepreuveRepository, AchatService achatService) {
        this.achatepreuveRepository = achatepreuveRepository;
        this.achatService = achatService;
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
    public void updateAchatEpreuve(AchatEpreuve newInfosAchatepreuve) {
        AchatEpreuve updatedAchatepreuve = achatepreuveRepository.findById(newInfosAchatepreuve.getId()).orElse(null);
        updatedAchatepreuve.setAchat(newInfosAchatepreuve.getAchat());
        updatedAchatepreuve.setEpreuve(newInfosAchatepreuve.getEpreuve());
        updatedAchatepreuve.setQuantite(newInfosAchatepreuve.getQuantite());
        updatedAchatepreuve.setPrixunitaire(newInfosAchatepreuve.getPrixunitaire());
        updatedAchatepreuve.setPrixtotal();
        achatepreuveRepository.save(updatedAchatepreuve);
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

    @Override
    public long getAchatEpreuveCountByQuantite(Integer quantite){
        return achatepreuveRepository.countByQuantite(quantite);
    }
}