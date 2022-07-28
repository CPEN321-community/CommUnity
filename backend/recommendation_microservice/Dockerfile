FROM node:buster-slim

WORKDIR /app
COPY ./package*.json .
RUN npm ci
COPY . .
CMD ["npm", "start"]

