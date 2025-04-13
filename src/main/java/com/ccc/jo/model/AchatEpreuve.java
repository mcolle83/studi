package com.ccc.jo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "achatepreuves")
public class AchatEpreuve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idachat")
    private Achat achat;
    @ManyToOne
    @JoinColumn(name = "idepreuve")
    private Epreuve epreuve;
    private Integer quantite;
    private BigDecimal prixunitaire;
    private BigDecimal prixtotal;

    public AchatEpreuve() {}

    public AchatEpreuve(Achat achat, Epreuve epreuve, Integer quantite, BigDecimal prixunitaire, BigDecimal prixtotal) {
        this.achat = achat;
        this.epreuve = epreuve;
        this.quantite = quantite;
        this.prixunitaire = prixunitaire;
        this.prixtotal = prixtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Achat getAchat() { return achat; }
    public void setAchat(Achat achat) { this.achat = achat; }

    public Epreuve getEpreuve() { return epreuve; }
    public void setEpreuve(Epreuve epreuve) { this.epreuve = epreuve; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getPrixunitaire() { return prixunitaire; }
    public void setPrixunitaire(BigDecimal prixunitaire) { this.prixunitaire = prixunitaire; }

    public BigDecimal getPrixtotal() { return prixtotal; }
    public void setPrixtotal() { this.prixtotal = this.prixunitaire.multiply(new BigDecimal(quantite)); }

}