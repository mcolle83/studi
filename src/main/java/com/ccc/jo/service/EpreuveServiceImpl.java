package com.ccc.jo.service;

import com.ccc.jo.model.Epreuve;
import com.ccc.jo.repository.EpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void updateEpreuve(Epreuve updatedEpreuve) {
        Epreuve existingEpreuve = epreuveRepository.findById(updatedEpreuve.getId()).orElse(null);
        updatedEpreuve.setNom(existingEpreuve.getNom());
        updatedEpreuve.setDiscipline(existingEpreuve.getDiscipline());
        updatedEpreuve.setLieu(existingEpreuve.getLieu());
        updatedEpreuve.setVille(existingEpreuve.getVille());
        updatedEpreuve.setDate(existingEpreuve.getDate());
        updatedEpreuve.setCapacite(existingEpreuve.getCapacite());
        updatedEpreuve.setPrix(existingEpreuve.getPrix());
        updatedEpreuve.setDescription(existingEpreuve.getDescription());
        updatedEpreuve.setImage(existingEpreuve.getImage());
        epreuveRepository.save(updatedEpreuve);
    }

    @Override
    public void deleteEpreuve(Long id) {
        epreuveRepository.deleteById(id);
    }

}