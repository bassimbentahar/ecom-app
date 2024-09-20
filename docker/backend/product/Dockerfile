#build stage
FROM maven:3.9.8-eclipse-temurin-22-jammy AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


#running stage
FROM eclipse-temurin:21-jdk
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/product-*.jar /app/

EXPOSE 8050

ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
# on donne le nom du container se trouvant dans DockerCompose
ENV DB_URL=r2dbc:postgresql://postgres:5432/product

CMD java -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} -jar product-${JAR_VERSION}.jar




