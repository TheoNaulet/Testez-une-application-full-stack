# Projet - Tests et Installation

Ce document explique comment **installer, lancer et exécuter les tests** de ce projet, qui est composé :
✅ D'un **frontend en Angular**
✅ D'un **backend en Java Spring Boot**

## 📌 Prérequis

Assurez-vous d'avoir installé les technologies suivantes :
- **Node.js** et **npm/yarn** (pour le frontend)
- **Java 1.8+** et **Maven** (pour le backend)

---

# 🚀 Installation et Lancement

## **1️⃣ Backend (Java Spring Boot)**

### **Installation des dépendances**
```sh
mvn clean install
```

### **Lancement du backend**
```sh
mvn spring-boot:run
```
📌 Par défaut, l'application utilise une base de données **H2** en environnement de test.

Si vous souhaitez utiliser **MySQL**, configurez `application.properties` avec votre connexion :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## **2️⃣ Frontend (Angular)**

### **Installation des dépendances**
```sh
npm install
```
ou
```sh
yarn install
```

### **Lancement du frontend**
```sh
npm start
```
L'application sera disponible sur **http://localhost:4200**.

---

# 🧪 Exécution des Tests

## **📌 Tests Backend**

### **Tests unitaires et d'intégration**
```sh
mvn clean test
```
📌 Ce test exécute **JUnit + Mockito** pour les tests unitaires et **MockMvc + Spring Boot Test** pour les tests d'intégration.

### **Générer un rapport de couverture (Jacoco)**
```sh
mvn clean test jacoco:report
```
Le rapport sera disponible dans **`target/site/jacoco/index.html`**.

---

## **📌 Tests Frontend**

### **Tests unitaires avec Jest**

### Installation

Jest est déjà inclus dans les dépendances du projet. Si ce n'est pas le cas, installez-le avec :
```sh
npm install --save-dev jest
```
ou
```sh
yarn add --dev jest
```

### Exécution des tests unitaires

Lancez les tests Jest avec la commande :
```sh
npm test
```
ou, pour exécuter les tests en mode "watch" :
```sh
npm run test:watch
```

### Génération du coverage

Pour générer un rapport de couverture de code avec Jest :
```sh
npm run test -- --coverage
```
Cela produira un dossier **coverage/** avec un rapport détaillé.

### Configuration

La configuration de Jest se trouve dans le fichier **jest.config.js**.

### Structure des tests Jest

Les fichiers de tests doivent suivre la convention `*.spec.ts`.

Exemple :
```
/project-root
 ├── src
 │   ├── components
 │   │   ├── Button.ts
 │   │   ├── Button.spec.ts
```

---

### **Tests End-to-End avec Cypress**

#### **Installation**
Si Cypress n'est pas encore installé, ajoutez-le avec :
```sh
npm install --save-dev cypress
```
ou
```sh
yarn add --dev cypress
```

#### **Lancer Cypress en mode UI**
```sh
npm run cypress:open
```
#### **Exécuter les tests en mode headless**
```sh
npm run cypress:run
```

#### **Générer un rapport de coverage pour Cypress**
```sh
npm run e2e:coverage
```

### **Structure des tests Cypress**
Les tests Cypress sont situés dans `cypress/e2e/` et suivent la convention `*.cy.ts`.

Exemple :
```
/project-root
 ├── cypress
 │   ├── e2e
 │   │   ├── login.cy.ts
 │   │   ├── dashboard.cy.ts
```

### **Configuration Cypress**
La configuration de Cypress est définie dans le fichier **cypress.config.ts**.

📌 Les tests Cypress simulent **un utilisateur réel interagissant avec l’application**.

---

# 🔎 Bonnes pratiques

- **Séparer les tests unitaires, d’intégration et E2E**.
- **Mocker les services externes** pour les tests unitaires.
- **Générer et analyser les rapports de couverture**.
- **Exécuter les tests avant chaque commit**.

---

# 📌 Commandes utiles

| Action | Backend | Frontend |
|--------|---------|---------|
| Installer les dépendances | `mvn clean install` | `npm install` |
| Lancer le projet | `mvn spring-boot:run` | `npm start` |
| Exécuter les tests | `mvn clean test` | `npm test` |
| Exécuter les tests en watch mode | - | `npm run test:watch` |
| Lancer les tests E2E | - | `npm run cypress:run` |
| Générer un rapport de coverage | `mvn jacoco:report` | `npm run test -- --coverage` |

---

# 🎯 Conclusion

Ce guide fournit toutes les étapes pour **installer, exécuter et tester** l'application.
Assurez-vous de suivre les bonnes pratiques pour garantir un **code fiable et robuste**.

🚀 **Bon développement et happy testing !**
