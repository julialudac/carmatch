# CarMatch

**CarMatch** est une API REST développée en Java avec Spring Boot. Elle permet de rechercher des voitures adaptées aux besoins d'un utilisateur à partir de plusieurs critères, puis de classer les résultats selon leur compatibilité.

Le projet a été conçu comme un projet personnel d'apprentissage de Java et Spring Boot, avec une approche orientée API et une architecture en couches.

## Fonctionnalités

- Enregistrer une voiture dans le catalogue.
- Consulter les voitures enregistrées.
- Rechercher des voitures selon plusieurs critères :
  - Budget maximal.
  - Type de carburant.
  - Nombre de passagers.
  - Capacité minimale du coffre.
- Prendre en compte les préférences d'utilisation :
  - Types de trajets (ville, routes de campagne, autoroute).
  - Type de carrosserie souhaité.
- Calculer un score de compatibilité pour chaque voiture éligible.
- Classer les recommandations par score décroissant.

Les critères de budget, carburant, nombre de places et capacité du coffre sont éliminatoires. Les préférences de trajet et de carrosserie influencent le score de compatibilité.

## Technologies utilisées

| Technologie        | Utilisation                               |
| ------------------ | ----------------------------------------- |
| Java 25            | Langage de programmation                  |
| Spring Boot 4      | Framework applicatif                      |
| Spring Web MVC     | Création de l'API REST                    |
| Spring Data JPA    | Accès aux données                         |
| Hibernate          | Implémentation JPA                        |
| H2 Database        | Base de données relationnelle embarquée   |
| Jakarta Validation | Validation des données reçues             |
| Maven              | Gestion des dépendances et compilation    |
| JUnit 5            | Tests automatisés                         |
| Mockito            | Simulation des dépendances dans les tests |

## Architecture

Le projet suit une architecture en couches :

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database (H2)
```

- **Controller** : reçoit les requêtes HTTP, valide les données entrantes et retourne les réponses.
- **Service** : contient la logique métier, notamment le filtrage et le classement des voitures.
- **Repository** : assure les opérations de persistance grâce à Spring Data JPA.
- **Model** : contient les entités et les énumérations métier.
- **DTO** : définit les objets utilisés pour transporter les données entre les différentes couches et l'API.

## Installation et lancement

### Prérequis

- Java 25 ou version compatible.
- Maven (ou le Maven Wrapper fourni avec le projet).
- Git.

### Récupération du projet

```bash
git clone <URL_DU_DEPOT>
cd CarMatch
```

### Lancement

Sous Windows :

```bash
.\mvnw.cmd spring-boot:run
```

Sous Linux ou macOS :

```bash
./mvnw spring-boot:run
```

L'API est ensuite accessible à l'adresse :

`http://localhost:8080`

### Base de données

CarMatch utilise H2 en mémoire pour le stockage des voitures.

La base est initialisée au démarrage de l'application et les données sont perdues à son arrêt.

La console H2 est accessible à :

`http://localhost:8080/h2-console`

Configuration actuelle :

- JDBC URL : `jdbc:h2:mem:carmatch`
- Username : `sa`
- Password : *(vide)*

## Utilisation de l'API

Les requêtes peuvent être exécutées avec Postman.

Les fichiers de requêtes HTTP de démonstration sont disponibles dans le dossier `requests/`.

### Endpoints disponibles

| Méthode | Endpoint                | Description                   |
| ------- | ----------------------- | ----------------------------- |
| GET     | `/cars`                 | Récupérer toutes les voitures |
| POST    | `/cars`                 | Ajouter une voiture           |
| POST    | `/cars/recommendations` | Obtenir des recommandations   |

### Ajouter une voiture

`POST /cars`

En-tête :

`Content-Type: application/json`

Exemple de corps JSON :

```json
{
  "brand": "Kia",
  "model": "e-Niro",
  "price": 19990,
  "fuelType": "ELECTRIC",
  "bodyStyle": "CROSSOVER",
  "seats": 5,
  "trunkCapacity": 451
}
```

Une voiture correctement enregistrée retourne le statut HTTP `201 Created`.

### Rechercher des voitures

`POST /cars/recommendations`

Exemple de corps JSON :

```json
{
  "maxBudget": 20000,
  "tripTypes": ["CITY", "HIGHWAY"],
  "fuelType": "ELECTRIC",
  "passengers": 4,
  "minTrunkCapacity": 400,
  "bodyStyle": "CROSSOVER"
}
```

La réponse contient une liste de recommandations avec les informations de chaque voiture et son score de compatibilité.

Exemple simplifié de résultat :

```json
[
  {
    "car": {
      "brand": "Kia",
      "model": "e-Niro"
    },
    "compatibilityScore": 93
  }
]
```

Le score est calculé à partir des préférences renseignées. Les critères non spécifiés ne pénalisent pas la voiture.

Si aucune voiture ne correspond aux critères éliminatoires, l'API retourne une liste vide avec le statut HTTP `200 OK`.

## Tests

Le projet comprend des tests automatisés portant notamment sur :

- La persistance des voitures avec Spring Data JPA.
- Le filtrage selon les critères de recherche.
- Le calcul des scores de compatibilité.
- Le classement des recommandations.

Les tests unitaires du service utilisent Mockito pour isoler la logique métier de ses dépendances.

Pour lancer les tests :

```bash
./mvnw test
```

Sous Windows :

```bash
.\mvnw.cmd test
```

## État du projet

Le MVP permet actuellement :

- La gestion élémentaire du catalogue de voitures.
- La recherche multicritère.
- Le calcul et le classement des scores de compatibilité.
- L'exposition des fonctionnalités via une API REST.

Le projet reste volontairement centré sur le backend : aucune interface graphique n'est actuellement proposée.

## Améliorations envisageables

- Ajouter un catalogue de démonstration initialisé automatiquement.
- Améliorer la structure des DTO de réponse pour découpler complètement l'API des entités JPA.
- Ajouter des tests automatisés du contrôleur et des endpoints HTTP.
- Enrichir les recommandations avec les points forts et les compromis de chaque véhicule.
- Remplacer H2 par une base de données persistante pour un déploiement réel.
- Ajouter une interface utilisateur.
