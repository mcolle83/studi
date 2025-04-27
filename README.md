Paris Jeux Olympiques 2024
========

**Réalisé par Maxime Colle**

Github mcolle83: https://github.com/mcolle83  

Projet Spring Boot basé sur les Jeux Olympiques de Paris en 2024.

## Lien vers le site

https://studi-e7e2bdae765d.herokuapp.com

## Fonctionnalités

- [x] Inscription/Connexion des utilisateurs
- [x] Ajout/Modification/Suppression d'épreuves
- [x] Liste des épreuves
- [x] Réservation des épreuves
- [x] Paiement
- [x] Liste des achats
- [x] Génération d'un ebillet avec un QRCode

## Installation

Pré-requis : Maven, Java 21, PostgreSQL

Ouvrez le terminal de votre ordinateur, allez dans le dossier d'installation du projet et clonez le dépôt

```
git clone https://github.com/mcolle83/studi.git
```

Après avoir cloné le dépôt, modifiez le dossier application.properties afin de correspondre à votre base de données

```
spring.datasource.url=jdbc:postgresql://localhost:5432/votrebasededonnees
spring.datasource.username=votrepseudo
spring.datasource.password=votremotdepasse
spring.datasource.driver-class-name=org.postgresql.Driver
```

Démarrez votre site local en utilisant le terminal de votre IDE

```
mvn spring-boot:run
```

Utilisez l'URL suivante pour accéder à votre site

```
http://localhost:8080/
```