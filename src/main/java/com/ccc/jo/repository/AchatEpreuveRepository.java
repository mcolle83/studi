package com.ccc.jo.repository;

import com.ccc.jo.model.AchatEpreuve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchatEpreuveRepository extends JpaRepository<AchatEpreuve, Long> {
    long countByQuantite(Integer quantite);
}