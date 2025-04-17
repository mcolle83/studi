package com.ccc.jo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "epreuves")
public class Epreuve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nom;
    @Column(nullable=false)
    private String discipline;
    @Column(nullable=false)
    private String lieu;
    @Column(nullable=false)
    private String ville;
    @Column(nullable=false)
    private LocalDateTime date;
    @Column(nullable=false)
    private Long capacite;
    @Column(nullable=false)
    private BigDecimal prix;
    @Column(nullable=true)
    private String description;
    @Column(nullable=false)
    private String image;

    public Epreuve() {}

    public Epreuve(String nom, String discipline, String lieu, String ville, LocalDateTime date, Long capacite, BigDecimal prix, String description, String image) {
        this.nom = nom;
        this.discipline = discipline;
        this.lieu = lieu;
        this.ville = ville;
        this.date = date;
        this.capacite = capacite;
        this.prix = prix;
        this.description = description;
        this.image = image;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDiscipline() { return discipline; }
    public void setDiscipline(String discipline) { this.discipline = discipline; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Long getCapacite() { return capacite; }
    public void setCapacite(Long capacite) { this.capacite = capacite; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

}