package com.ccc.jo.service;

import com.ccc.jo.model.Epreuve;
import com.ccc.jo.repository.EpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpreuveServiceImpl implements EpreuveService {

    private final EpreuveRepository epreuveRepository;

    @Autowired
    public EpreuveServiceImpl(EpreuveRepository epreuveRepository) {
        this.epreuveRepository = epreuveRepository;
    }

    @Override
    public List<Epreuve> getAllEpreuves() {
        return epreuveRepository.findAll();
    }

    @Override
    public Epreuve getEpreuveById(Long id) {
        return epreuveRepository.findById(id).orElse(null);
    }

    @Override
    public void createEpreuve(Epreuve epreuve) {
        Epreuve newEpreuve = new Epreuve();
        newEpreuve.setNom(epreuve.getNom());
		newEpreuve.setDiscipline(epreuve.getDiscipline());
        newEpreuve.setLieu(epreuve.getLieu());
        newEpreuve.setVille(epreuve.getVille());
        newEpreuve.setDate(epreuve.getDate());
        newEpreuve.setCapacite(epreuve.getCapacite());
        newEpreuve.setPrix(epreuve.getPrix());
        newEpreuve.setDescription(epreuve.getDescription());
        newEpreuve.setImage(epreuve.getImage());
        epreuveRepository.save(newEpreuve);
    }

    @Override
    public void updateEpreuve(Epreuve newInfosEpreuve) {
        Epreuve updatedEpreuve = epreuveRepository.findById(newInfosEpreuve.getId()).orElse(null);
        updatedEpreuve.setNom(newInfosEpreuve.getNom());
        updatedEpreuve.setDiscipline(newInfosEpreuve.getDiscipline());
        updatedEpreuve.setLieu(newInfosEpreuve.getLieu());
        updatedEpreuve.setVille(newInfosEpreuve.getVille());
        updatedEpreuve.setDate(newInfosEpreuve.getDate());
        updatedEpreuve.setCapacite(newInfosEpreuve.getCapacite());
        updatedEpreuve.setPrix(newInfosEpreuve.getPrix());
        updatedEpreuve.setDescription(newInfosEpreuve.getDescription());
        updatedEpreuve.setImage(newInfosEpreuve.getImage());
        epreuveRepository.save(updatedEpreuve);
    }

    @Override
    public void deleteEpreuve(Long id) {
        epreuveRepository.deleteById(id);
    }

    @Override
    public void reduceEpreuveCapacite(Long id, Integer quantite) {
        Epreuve updatedEpreuve = epreuveRepository.findById(id).orElse(null);
        updatedEpreuve.setCapacite(updatedEpreuve.getCapacite() - quantite);
        epreuveRepository.save(updatedEpreuve);
    }

}