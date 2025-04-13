package com.ccc.jo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "panierepreuves")
public class PanierEpreuve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idpanier")
    private Panier panier;
    @ManyToOne
    @JoinColumn(name = "idepreuve")
    private Epreuve epreuve;
    private Integer quantite;
    private BigDecimal prixunitaire;
    private BigDecimal prixtotal;

    public PanierEpreuve() {}

    public PanierEpreuve(Panier panier, Epreuve epreuve, Integer quantite, BigDecimal prixunitaire, BigDecimal prixtotal) {
        this.panier = panier;
        this.epreuve = epreuve;
        this.quantite = quantite;
        this.prixunitaire = prixunitaire;
        this.prixtotal = prixtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Panier getPanier() { return panier; }
    public void setPanier(Panier panier) { this.panier = panier; }

    public Epreuve getEpreuve() { return epreuve; }
    public void setEpreuve(Epreuve epreuve) { this.epreuve = epreuve; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getPrixunitaire() { return prixunitaire; }
    public void setPrixunitaire(BigDecimal prixunitaire) { this.prixunitaire = prixunitaire; }

    public BigDecimal getPrixtotal() { return prixtotal; }
    public void setPrixtotal() { this.prixtotal = this.prixunitaire.multiply(new BigDecimal(quantite)); }

}