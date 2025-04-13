package com.ccc.jo.repository;

import com.ccc.jo.model.Epreuve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpreuveRepository extends JpaRepository<Epreuve, Long> {
    Epreuve findByNom(String nom);
}
