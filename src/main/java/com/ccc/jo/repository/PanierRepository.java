package com.ccc.jo.repository;

import com.ccc.jo.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {
    Panier findByIdsession(String idsession);
}
