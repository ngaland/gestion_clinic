#!/bin/bash

# Script de démarrage pour l'application Gestion Clinic

echo "🏥 Démarrage de l'application Gestion Clinic..."

# Vérifier si Docker est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez l'installer d'abord."
    exit 1
fi

# Vérifier si Docker Compose est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez l'installer d'abord."
    exit 1
fi

# Arrêter les conteneurs existants
echo "🛑 Arrêt des conteneurs existants..."
docker compose down

# Construire et démarrer les services
echo "🔨 Construction et démarrage des services..."
docker compose up --build -d

# Attendre que les services soient prêts
echo "⏳ Attente du démarrage des services..."
sleep 30

# Vérifier le statut des services
echo "📊 Statut des services:"
docker compose ps

echo ""
echo "✅ Application démarrée avec succès!"
echo ""
echo "🌐 Services disponibles:"
echo "   - Backend API: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   - MinIO Console: http://localhost:9003 (admin/password123)"
echo "   - MinIO API: http://localhost:9002"
echo "   - MailHog UI: http://localhost:8025"
echo "   - PostgreSQL: localhost:5433"
echo ""
echo "📝 Pour voir les logs:"
echo "   docker compose logs -f app"
echo ""
echo "🛑 Pour arrêter l'application:"
echo "   docker compose down"