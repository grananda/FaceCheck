# Step 1: Build the app in image 'builder'
FROM node:12-slim as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run generate

# Step 2: Use build output from 'builder'
FROM nginx:stable-alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist /usr/share/nginx/html
