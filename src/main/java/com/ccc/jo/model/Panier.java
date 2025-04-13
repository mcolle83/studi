package com.ccc.jo.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

@Entity
@Table(name = "paniers")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idsession;
    @OneToMany(mappedBy="panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PanierEpreuve> epreuves = new HashSet<>();
    private BigDecimal prixtotal = BigDecimal.ZERO;

    public Panier() {}

    public Panier(String idsession, Set<PanierEpreuve> epreuves, BigDecimal prixtotal) {
        this.idsession = idsession;
        this.epreuves = epreuves;
        this.prixtotal = prixtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdsession() { return idsession; }
    public void setIdsession(String idsession) { this.idsession = idsession; }

    public Set<PanierEpreuve> getEpreuves() { return epreuves; }
    public void setEpreuves(Set<PanierEpreuve> epreuves) { this.epreuves = epreuves; }

    public BigDecimal getPrixtotal() { return prixtotal; }
    public void setPrixtotal(BigDecimal prixtotal) { this.prixtotal = prixtotal; }

    public void addOffre(PanierEpreuve offre) {
        this.epreuves.add(offre);
        offre.setPanier(this);
        updatePrixtotal();
    }

    public void removeOffre(PanierEpreuve offre) {
        this.epreuves.remove(offre);
        offre.setPanier(null);
        updatePrixtotal();
    }

    private void updatePrixtotal() {
        this.prixtotal = epreuves.stream().map(item -> {
            BigDecimal prixunitaire = item.getPrixunitaire();
            if (prixunitaire == null) {
                return  BigDecimal.ZERO;
            }
            return prixunitaire.multiply(BigDecimal.valueOf(item.getQuantite()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
