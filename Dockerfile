


# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copie pom.xml e baixe dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie o código-fonte
COPY src ./src

# Build normal (mas não copia o JAR ainda)
RUN mvn clean package -DskipTests && cp target/*.jar target/historicosaude.jar

# Runtime stage para desenvolvimento com hot reload
FROM maven:3.9.9-eclipse-temurin-21 AS dev

WORKDIR /app

# Copie o código-fonte e pom.xml (serão sobrescritos por volumes)
COPY pom.xml .
COPY src ./src

EXPOSE 8080

VOLUME ["/app/src", "/app/target", "/app/pom.xml"]

# Rodar em modo desenvolvimento com hot reload
ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
