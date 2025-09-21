# syntax=docker/dockerfile:1

# ====== build ======
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Baixa dependências em cache
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:go-offline

# Compila
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

# ====== runtime ======
FROM eclipse-temurin:21-jre
WORKDIR /app

# (opcional) flags de JVM
ENV JAVA_OPTS=""

# Copia o jar empacotado
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
