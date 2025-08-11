#!/bin/bash
set -euo pipefail

API="http://localhost:8080/api/v1"
ADMIN="$API/admin"
HDR="Content-Type: application/json"

echo "=== Create an item (so itemId exists) ==="
ITEM_ID=$(curl -s -X POST "$API/item" -H "$HDR" -d '{"name":"Laptop"}' | jq -r '.id')
echo "ITEM_ID=$ITEM_ID"

echo "=== Add bets (one bad, one good) ==="
curl -s -X POST "$API/apuesta" -H "$HDR" \
  -d "{\"itemId\":$ITEM_ID,\"usuarioNombre\":\"nicolas*\",\"montoApuesta\":1000}" >/dev/null
curl -s -X POST "$API/apuesta" -H "$HDR" \
  -d "{\"itemId\":$ITEM_ID,\"usuarioNombre\":\"nicolas\",\"montoApuesta\":2000}" >/dev/null

echo "=== BEFORE cleanup ==="
curl -s "$ADMIN/apuestas" | jq
echo

echo "=== Wait ~2 minutes for the cron to run ==="
sleep 130

echo "=== AFTER cleanup ==="
curl -s "$ADMIN/apuestas" | jq
echo
