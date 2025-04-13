package com.ccc.jo.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "achats")
public class Achat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idutilisateur")
    private Utilisateur utilisateur;
    @OneToMany(mappedBy="achat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AchatEpreuve> epreuves = new HashSet<>();
    private BigDecimal prixtotal = BigDecimal.ZERO;
    @Column(nullable=false)
    private LocalDateTime date;
    @Column(nullable=false, unique=true)
    private String cle;

    public Achat() {}

    public Achat(Utilisateur utilisateur, Set<AchatEpreuve> epreuves, BigDecimal prixtotal, LocalDateTime date, String cle) {
        this.utilisateur = utilisateur;
        this.epreuves = epreuves;
        this.prixtotal = prixtotal;
        this.date = date;
        this.cle = cle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Set<AchatEpreuve> getEpreuves() { return epreuves; }
    public void setEpreuves(Set<AchatEpreuve> epreuves) { this.epreuves = epreuves; }

    public BigDecimal getPrixtotal() { return prixtotal; }
    public void setPrixtotal(BigDecimal prixtotal) { this.prixtotal = prixtotal; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getCle() { return cle; }
    public void setCle(String cle) { this.cle = cle; }
}