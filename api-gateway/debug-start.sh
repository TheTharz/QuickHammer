#!/bin/bash

echo "🔨 Building API Gateway..."
./mvnw clean package -DskipTests

echo "🐳 Building Docker image..."
docker compose -f docker-compose.debug.yml build

echo "🚀 Starting API Gateway in debug mode..."
docker compose -f docker-compose.debug.yml up -d

echo "✅ API Gateway is running!"
echo "📍 Application: http://localhost:5000"
echo "🐛 Debug port: 5005"
echo ""
echo "To view logs: docker compose -f docker-compose.debug.yml logs -f"
echo "To stop: docker compose -f docker-compose.debug.yml down"
