# Tests du Backend (Java Spring)

Ce document détaille les tests implémentés dans ce backend développé avec **Java Spring Boot**. 
Il couvre les tests unitaires avec **JUnit et Mockito** ainsi que les tests d'intégration avec **MockMvc et Spring Boot Test**.

## Prérequis

Avant d'exécuter les tests, assurez-vous d'avoir installé **Maven** et **Java 1.8**.

### Installation des dépendances

Si ce n'est pas déjà fait, installez les dépendances du projet avec :

```sh
mvn clean install
```

---

## Configuration de la base de données de test (H2)

Les tests utilisent une base de données en mémoire **H2**. 
La configuration se fait via `src/test/resources/application-test.properties`.

💡 **Exemple de configuration à adapter dans `application-test.properties` :**

```properties
spring.datasource.url=jdbc:h2:mem:<test-db>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Cela permet d'exécuter les tests sans affecter la base de données principale.

---

## Tests unitaires et d'intégration

Les tests sont organisés en deux types :
- **Tests unitaires** : vérification du comportement des composants isolés (services, modèles). Situés dans `test/java/com/openclassrooms/starterjwt/unit/`
- **Tests d'intégration** : validation du bon fonctionnement de l'application avec MockMvc et Spring Boot Test. Situés dans `test/java/com/openclassrooms/starterjwt/integration/`

### Exécution des tests

Les tests unitaires et d'intégration sont exécutés ensemble avec :

```sh
mvn clean test
```

---

## Génération du coverage avec Jacoco

Le projet utilise **Jacoco** pour analyser la couverture de code des tests.

### Générer un rapport de couverture

```sh
mvn clean test jacoco:report
```

Le rapport sera généré dans `target/site/jacoco/index.html`.

### Configuration de Jacoco (dans `pom.xml`)

Jacoco est configuré pour vérifier que **90% du code est couvert** par les tests :

```xml
<execution>
    <id>jacoco-check</id>
    <goals>
        <goal>check</goal>
    </goals>
    <configuration>
        <rules>
            <rule>
                <element>PACKAGE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.9</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</execution>
```

---

## Commandes utiles

| Action | Commande |
|--------|---------|
| Exécuter tous les tests (unitaires + intégration) | `mvn clean test` |
| Générer un rapport de couverture Jacoco | `mvn clean test jacoco:report` |
| Voir le rapport de couverture | `target/site/jacoco/index.html` |

---

## Conclusion

Ce guide vous permet d'exécuter et de comprendre les tests du backend. 
Assurez-vous de bien respecter les bonnes pratiques et d'intégrer les tests dans votre workflow CI/CD.

🚀 **Happy Testing !**
