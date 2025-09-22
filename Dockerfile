# Multi-stage build pour optimiser la taille de l'image
FROM eclipse-temurin:17-jdk AS build

# Installer Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .

# Télécharger les dépendances (cache Docker)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Image de production
FROM eclipse-temurin:17-jre

# Installer curl pour les health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Créer un utilisateur non-root pour la sécurité
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Changer le propriétaire du fichier
RUN chown spring:spring app.jar

# Utiliser l'utilisateur non-root
USER spring:spring

# Exposer le port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]