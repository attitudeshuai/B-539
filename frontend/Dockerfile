FROM node:20-slim AS build
WORKDIR /app
COPY package.json ./
RUN npm config set registry https://registry.npmmirror.com && \
    npm config set fetch-retry-maxtimeout 600000 && \
    npm install --no-audit --verbose --legacy-peer-deps
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
