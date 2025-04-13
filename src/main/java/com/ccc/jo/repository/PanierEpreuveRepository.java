package com.ccc.jo.repository;

import com.ccc.jo.model.PanierEpreuve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanierEpreuveRepository extends JpaRepository<PanierEpreuve, Long> {
    void deleteAllByPanierId(Long id);
}
