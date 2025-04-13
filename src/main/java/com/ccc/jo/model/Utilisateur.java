package com.ccc.jo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nom;
    @Column(nullable=false)
    private String prenom;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String motdepasse;
    @Transient
    private String confirmermdp;
    @Column(nullable=false)
    private String role;
    @Column(nullable=false, unique=true)
    private String cle;
    @Column(nullable=false, unique=true)
    private String tokenconfirm;
    @Column(nullable=false)
    private Boolean active;

    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email, String motdepasse, String role, String cle, String tokenconfirm, Boolean active) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.role = role;
        this.cle = cle;
        this.tokenconfirm = tokenconfirm;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotdepasse() { return motdepasse; }
    public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }

    public String getConfirmermdp() { return confirmermdp; }
    public void setConfirmermdp(String confirmermdp) { this.confirmermdp = confirmermdp; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCle() { return cle; }
    public void setCle(String cle) { this.cle = cle; }

    public String getTokenconfirm() { return tokenconfirm; }
    public void setTokenconfirm(String tokenconfirm) { this.tokenconfirm = tokenconfirm; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Boolean compareMotdepasse(String motdepasse, String confirmermdp) { return motdepasse.equals(confirmermdp); }

}