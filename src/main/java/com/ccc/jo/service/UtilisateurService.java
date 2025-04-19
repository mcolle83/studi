package com.ccc.jo.service;

import com.ccc.jo.model.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    List<Utilisateur> getAllUtilisateurs();

    Utilisateur getUtilisateurById(Long id);

	Utilisateur getUtilisateurByEmail(String email);

    Utilisateur getUtilisateurByTokenconfirm(String tokenconfirm);

    void createUtilisateur(Utilisateur utilisateur);

    Boolean verifMotdepasse(String motdepasse, String cryptedMotdepasse);

    Utilisateur loginUtilisateur(Utilisateur utilisateur);

    Utilisateur updateUtilisateur(Long id, String nom, String prenom, String email);

    Utilisateur updateMotdepasse(Long id, String motdepasse);

    void deleteUtilisateur(Long id);

    void sendEmailConfirmation(Utilisateur utilisateur);

    void activeUtilisateur(String tokenconfirm);
}