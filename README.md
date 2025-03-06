# MaRI - Ma Recherche Immobilière

MaRI est un projet personnel et open-source ayant pour but de simplifier la recherche d'un bien immobilier, à l'achat comme à la  
location. L'ambition est de proposer un outil facilitant la vie des personnes à la recherche d'un bien, en proposant  
une suite d'outils adaptés et utiles dans la phase recherches.

## Fonctionnalités

| Nom                                                | Description                                                                                            | Disponibilité |
|----------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------|
| Authentification avec Auth0                        |                                                                                                        | 🟢            |
| Gestion d'annonces                                 | Gérer des annonces favorites                                                                           | 🟢            |
| Partage d'annonces                                 | Partager des annonces à d'autres personnes non authentifiées                                           | 🟢            |
| Calculer les mensualités d'un crédit               |                                                                                                        | 🟢            |
| Calculer sa capacité d'emprunt                     |                                                                                                        | 🟢            |
| Gestion d'adresses en GeoJson                      | Pouvoir gérer des adresses avec le standard GeoJson                                                    | 🟠            |
| Documentation OpenAPI                              | Mettre en place la documentation OpenAPI des ressources                                                | 🔴            |
| Enrichir une annonce sauvegardée avec des critères | Permettre d'ajouter des critères à une annonce. ex : le mode de chauffage                              | 🔴            |
| Extraire les données d'une annonce                 | Permettre de sauvegarder des annonces en extrayant leurs données depuis le site d'origine              | 🔴            |
| Extraire les images d'une annonce                  | Permettre lors de l'extracton des données d'une annonce de récupérer les liens des images de l'annonce | 🔴            |
| Recherche d'informations cadastrales               | Rechercher des informations dans le cadastre ou dans le dvf du bien et autour                          | 🔴            |

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

## Lancer l'application en local

Pour lacer l'appli en local, il faut rajouter un fichier .env à la racine du module `application` avec les propriétés suivantes :

```properties
mari.oidc.auth-server-url=
mari.oidc.client-id=
mari.oidc.client-secret=
test-username=
test-password=
```

Il faut ensuite lancer le docker-compose à la racine du projet.
Enfin, démarrer l'application avec le profil `dev`.
