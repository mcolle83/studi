package com.ccc.jo.repository;

import java.util.List;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchatRepository extends JpaRepository<Achat, Long> {
    List<Achat> findAllByUtilisateur(Utilisateur utilisateur);
}
