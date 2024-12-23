# Build Stage
FROM node:21-alpine3.18 AS builder
WORKDIR /app
COPY *.json ./
RUN npm install
COPY . .
RUN npm run build

# Serve Stage
FROM nginx:stable-alpine3.20
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
