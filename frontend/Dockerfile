FROM node:20-alpine

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm ci

COPY . .

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "4200"]

EXPOSE 4200
