# Stage 1: Build Stage
FROM node:21-alpine3.18 AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

# Stage 2: Serve Stage
FROM nginx:stable-alpine

# Копируем билд из Vue
COPY --from=builder /app/dist /usr/share/nginx/html

# Копируем стандартный конфиг Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Экспонируем стандартный порт
EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
