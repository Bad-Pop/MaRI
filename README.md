# MaRI - Ma Recherche Immobilière

MaRI est un projet personnel et open-source ayant pour but de simplifier la recherche d'un bien immobilier, à l'achat comme à la  
location. L'ambition est de proposer un outil facilitant la vie des personnes à la recherche d'un bien, en proposant  
une suite d'outils adaptés et utiles dans la phase recherches.

## Fonctionnalités

| Nom                                                | Description                                                                                            | Disponibilité |
|----------------------------------------------------|--------------------------------------------------------------------------------------------------------|--------------|
| Sauvegarde d'annonces                              | Sauvegarder des annonces pour les retrouver facilement                                                 | 🟠           |
| Sécuriser les appels à l'api                       | Sécuriser les appels avec des tokens                                                                   | 🔴           |
| Enrichir une annonce sauvegardée avec des critères | Permettre d'ajouter des critères à une annonce. ex : le mode de chauffage                              | 🔴           |
| Partage d'annonces sauvegardées                    | Partager un lien temporaire ou éternel vers une annonce sauvegardée                                    | 🔴           |
| Extraire les données d'une annonce                 | Permettre de sauvegarder des annonces en extrayant leurs données depuis le site d'origine              | 🔴           |
| Extraire les images d'une annonce                  | Permettre lors de l'extracton des données d'une annonce de récupérer les liens des images de l'annonce | 🔴           |
| Recherche d'informations cadastrales               | Rechercher des informations dans le cadastre du bien et autour                                         | 🔴           |

## Stack technique

### Back-end

- Quarkus
- PostgreSQL
- Jackson
- Lombok
- Hibernate Panache
- Vavr
- OpenAPI
- Liquibase
- TestContainer
- Docker
