# Projet MOBG5

Ce dépôt contient les sources du projet place2rate.

## Description

L'application permet d'enregistrer des endroits spécifiques autour de sa position. Une fois un endroit enregistré, l'utilisateur qui est connecté peut commenter, ajouter des photos et noter l'endroit. L'utilisateur peut également rechercher des endroits afin de consulter toutes les fonctionnalités citées.

## Persistance des données

L'application conserve un historique des recherches effectuées. Cet historique est persisté dans la base de données locale de l'application.

Les données relatives aux commentaires, aux photos et aux notes sont persistés via firebase : https://place2rate-default-rtdb.europe-west1.firebasedatabase.app/

## Nice to have

Possibilité de retrouver les endroits sur une carte.

Service rest : Pour collecter les données relatives aux endroits, des appels au service rest suivant sont effectués : https://maps.googleapis.com/maps/api/place/details/output?parameters

## Auteur

**Marika Winska** g55047
