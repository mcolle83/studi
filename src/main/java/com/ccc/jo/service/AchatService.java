package com.ccc.jo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ccc.jo.model.Achat;
import com.ccc.jo.model.Utilisateur;

public interface AchatService {
    List<Achat> getAllAchats();

    Achat getAchatById(Long id);

    Long createAchat(Utilisateur utilisateur, BigDecimal prixtotal, LocalDateTime date);

    void updateAchat(Achat updatedAchat);

    void deleteAchat(Long id);

    List<Achat> getAllAchatsByUtilisateur(Utilisateur utilisateur);

    void sendEmailAchat(Utilisateur utilisateur, Achat achat);
}