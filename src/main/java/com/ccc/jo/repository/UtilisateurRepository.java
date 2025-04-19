package com.ccc.jo.repository;

import com.ccc.jo.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
    Utilisateur findByTokenconfirm(String tokenconfirm);
}