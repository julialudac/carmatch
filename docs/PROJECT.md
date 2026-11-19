# CarMatch — Documentation technique du projet

## 1. Présentation

CarMatch est une API REST de recommandation automobile développée avec **Java 25 et Spring Boot 4**.

Son objectif est de proposer des véhicules correspondant aux besoins d'un utilisateur, en distinguant deux types de critères :

- **Critères éliminatoires** : ils déterminent si un véhicule peut être retenu.
- **Critères de compatibilité** : ils permettent de calculer un score et de classer les véhicules retenus.

Le projet constitue un MVP orienté backend, sans interface graphique.

## 2. Architecture générale

Le projet repose sur une architecture en couches afin de séparer les responsabilités.

```text
Client HTTP (Postman)
        |
        v
Controller
        |
        v
Service
        |
        v
Repository
        |
        v
H2 Database
```

### 2.1 Controller

Package : `com.carmatch.controller`

Les contrôleurs exposent les fonctionnalités via des endpoints REST.

Classes principales :

- `CarController` : gestion élémentaire des voitures.
- `CarRecommendationController` : réception des critères de recherche et retour des recommandations.

Les contrôleurs ne contiennent pas la logique métier. Ils délèguent le traitement aux services.

### 2.2 Service

Package : `com.carmatch.service`

Les services contiennent la logique applicative.

- `CarService` : opérations de consultation et de persistance des voitures.
- `CarRecommendationService` : filtrage, calcul des scores et classement des recommandations.

`CarRecommendationService` utilise `CarService` pour récupérer le catalogue des voitures.

Les dépendances sont injectées par constructeur, ce qui permet de limiter le couplage et de faciliter les tests unitaires.

### 2.3 Repository

Package : `com.carmatch.repository`

`CarRepository` étend `JpaRepository<Car, Long>`.

Spring Data JPA fournit automatiquement l'implémentation des opérations CRUD et de consultation, sans nécessiter l'écriture manuelle d'une classe d'implémentation.

### 2.4 Model et DTO

Packages :

- `com.carmatch.model`
- `com.carmatch.model.enums`
- `com.carmatch.dto`

**Car** est l'entité JPA représentant un véhicule enregistré en base.

Les énumérations définissent les valeurs métier contrôlées :

- `FuelType` : type de motorisation.
- `BodyStyle` : type de carrosserie.
- `TripType` : type de trajet.

Les DTO permettent de transporter les données entre le client HTTP et l'application :

- `CarSearchRequest` : critères transmis pour une recherche.
- `CarRecommendation` : voiture accompagnée de son score de compatibilité.

`CarSearchRequest` est un record Java, choisi pour représenter un objet de données dont les composants sont immuables.

## 3. Modèle de données

### 3.1 Entité Car

L'entité `Car` contient les caractéristiques nécessaires au fonctionnement du moteur de recommandation.

| Attribut | Type | Description |
|---|---|---|
| `id` | Long | Identifiant généré par la base |
| `brand` | String | Marque |
| `model` | String | Modèle |
| `price` | BigDecimal | Prix du véhicule |
| `fuelType` | FuelType | Type de motorisation |
| `bodyStyle` | BodyStyle | Carrosserie |
| `seats` | Integer | Nombre de places |
| `trunkCapacity` | Integer | Volume du coffre en litres |

Le prix est représenté par `BigDecimal` afin d'éviter les imprécisions liées aux nombres flottants.

Les énumérations sont persistées sous forme de chaînes de caractères avec `@Enumerated(EnumType.STRING)`, plutôt que sous forme d'ordinaux numériques.

### 3.2 CarSearchRequest

Le record `CarSearchRequest` regroupe les critères de recherche :

| Attribut | Type | Description |
|---|---|---|
| `maxBudget` | BigDecimal | Budget maximal |
| `tripTypes` | Set<TripType> | Types de trajets envisagés |
| `fuelType` | FuelType | Motorisation souhaitée |
| `passengers` | Integer | Nombre minimal de places nécessaires |
| `minTrunkCapacity` | Integer | Capacité minimale du coffre |
| `bodyStyle` | BodyStyle | Carrosserie souhaitée |

Les critères sont facultatifs. Une valeur absente signifie que l'utilisateur ne souhaite pas imposer de contrainte sur ce critère.

Le constructeur compact du record effectue une copie défensive de `tripTypes` avec `Set.copyOf()` lorsque l'ensemble n'est pas nul.

## 4. Fonctionnement du moteur de recommandation

Le traitement est réalisé dans `CarRecommendationService`.

Il se déroule en trois phases :

1. Récupération du catalogue.
2. Filtrage des véhicules éligibles.
3. Calcul des scores et classement.

### 4.1 Filtrage des véhicules

Les critères suivants sont éliminatoires :

| Critère | Condition de sélection |
|---|---|
| Budget | Prix inférieur ou égal au budget maximal |
| Motorisation | Correspondance exacte avec le carburant demandé |
| Passagers | Nombre de places supérieur ou égal au besoin |
| Coffre | Volume supérieur ou égal au minimum demandé |

Les préférences de trajet et de carrosserie ne sont pas éliminatoires. Elles influencent uniquement le score de compatibilité.

Pour le MVP, le catalogue est récupéré via `CarService.getAllCars()`, puis filtré en mémoire. Cette approche est suffisante pour un catalogue de petite taille. Une évolution vers des requêtes ciblées en base pourra être envisagée si le volume de données augmente.

### 4.2 Compatibilité avec les trajets

La classe `TripCompatibility` fournit une grille de scores entre les types de carrosserie et les types de trajets.

Les scores sont définis manuellement dans une structure de maps imbriquées.

Exemples :

- Hatchback / Ville : 100.
- SUV / Autoroute : 90.
- Wagon / Route de campagne : 95.
- Crossover / Ville : 85.

Ces valeurs sont des appréciations métier indicatives, destinées au fonctionnement du MVP. Elles ne constituent pas des mesures automobiles universelles.

Lorsque plusieurs types de trajets sont sélectionnés, leur score moyen est calculé.

Lorsqu'aucun trajet n'est renseigné, le critère est neutre.

### 4.3 Compatibilité avec la carrosserie

La classe `BodyStyleCompatibility` compare la carrosserie du véhicule avec celle recherchée.

Règles actuelles :

- Aucune préférence renseignée : score neutre de 100.
- Correspondance : 100.
- Absence de correspondance : 70.

La différence entre une correspondance exacte et une préférence non renseignée est volontaire : le score de 100 représente ici une neutralité ou une correspondance parfaite, et non une garantie de satisfaction absolue.

### 4.4 Calcul du score global

Les critères de compatibilité renseignés sont pondérés de manière égale dans la version actuelle.

Formule :

```text
Score global = moyenne des scores des critères renseignés
```

Les critères non renseignés sont exclus du calcul, afin de ne pas fausser la moyenne.

Exemple :

- Compatibilité trajets : 85.
- Compatibilité carrosserie : 100.

```text
(85 + 100) / 2 = 92,5
```

Le résultat est arrondi à l'entier le plus proche avec `Math.round()`, soit 93.

Lorsqu'aucun critère de compatibilité n'est renseigné, le score par défaut est 100.

### 4.5 Classement des recommandations

Les voitures éligibles sont transformées en objets `CarRecommendation`, contenant :

- La voiture.
- Son score de compatibilité.

Le classement est effectué par score décroissant avec `Comparator.comparingInt(...).reversed()`.

Les recommandations sont retournées sous forme de liste.

## 5. API REST

### Endpoints

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/cars` | Récupération de toutes les voitures |
| POST | `/cars` | Enregistrement d'une voiture |
| POST | `/cars/recommendations` | Recherche et classement des voitures |

### Validation des données

Les données entrantes sont validées avec Jakarta Bean Validation.

Exemples de contraintes utilisées :

- `@NotBlank` pour les chaînes obligatoires.
- `@NotNull` pour les valeurs obligatoires.
- `@Positive` pour les prix.
- `@Min` et `@Max` pour certaines limites numériques.
- `@PositiveOrZero` pour les capacités pouvant être nulles.

`@Valid` déclenche la validation des objets reçus dans les corps des requêtes HTTP.

### Gestion des erreurs

`GlobalExceptionHandler`, annoté avec `@RestControllerAdvice`, centralise la gestion de certaines erreurs de validation.

Les erreurs de validation des requêtes sont notamment transformées en réponses HTTP `400 Bad Request` contenant les messages associés aux champs invalides.

Une recherche valide ne retournant aucun véhicule produit une réponse `200 OK` avec une liste vide.

## 6. Persistance et configuration

CarMatch utilise H2 comme base relationnelle embarquée.

La configuration actuelle utilise une base en mémoire :

```properties
spring.datasource.url=jdbc:h2:mem:carmatch
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Les données sont perdues à l'arrêt de l'application.

La génération et la mise à jour du schéma sont actuellement confiées à Hibernate via `ddl-auto=update`. Cette configuration est adaptée au développement, mais une gestion versionnée des migrations serait préférable pour un environnement de production.

## 7. Stratégie de tests

Le projet utilise JUnit 5 et Mockito.

### Tests unitaires

`CarRecommendationServiceTest` vérifie notamment :

- Le filtrage des voitures selon les critères éliminatoires.
- Le calcul des scores de compatibilité.
- Le classement des voitures.
- L'orchestration de la recommandation.

Mockito permet de simuler `CarService` afin de tester la logique métier sans dépendre d'une véritable base de données.

### Tests de persistance

`CarRepositoryTest` utilise `@DataJpaTest` pour vérifier les opérations de persistance avec le contexte JPA et la base H2.

Ces tests couvrent notamment :

- L'injection du repository.
- L'enregistrement d'une voiture.
- La génération de son identifiant.
- La récupération des voitures.
- La recherche par identifiant.

### Tests manuels

Les endpoints ont également été testés avec Postman.

Les fichiers de requêtes de démonstration sont regroupés dans `requests/`.

Les tests manuels ont permis de vérifier le parcours complet entre le client HTTP, les contrôleurs, les services, la persistance et les réponses JSON.

## 8. État actuel et limites

Le MVP couvre les fonctionnalités principales de recommandation.

Limites actuelles :

- Catalogue de démonstration ajouté manuellement.
- Base de données temporaire en mémoire.
- Scores définis manuellement et non personnalisables.
- Pondération égale des critères de compatibilité.
- Absence d'interface graphique.
- Tests automatisés du contrôleur restant à développer.
- Réponse de recommandation encore directement liée à l'entité `Car`.

## 9. Évolutions envisageables

Plusieurs améliorations peuvent être envisagées :

- Initialisation automatique d'un catalogue de démonstration.
- Création de DTO de réponse indépendants des entités JPA.
- Ajout de tests d'intégration HTTP avec MockMvc.
- Ajout de points forts et de compromis pour chaque recommandation.
- Pondération configurable des critères de compatibilité.
- Enrichissement des caractéristiques automobiles.
- Migration vers une base de données persistante.
- Développement d'une interface utilisateur.

Ces évolutions ne font pas partie du périmètre actuel du MVP.