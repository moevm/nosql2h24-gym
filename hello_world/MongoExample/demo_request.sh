#!/bin/bash

echo "GET request to /messages"
curl http://localhost:8080/messages

echo -e "\nPOST request to /messages"
curl -X POST http://localhost:8080/messages -H "Content-Type: application/json" -d '{"content": "Hello, World!"}'

echo -e "GET request to /messages after POST request"
curl http://localhost:8080/messages
