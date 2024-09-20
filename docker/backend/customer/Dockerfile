# build stage
FROM maven:3.9.8-eclipse-temurin-22-jammy AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# running stage
FROM eclipse-temurin:22-jdk
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/customer-*.jar /app/

EXPOSE 8090

ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV DB_URL=mongodb://@mongodb:27017/customer?authSource=admin

CMD java -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} -jar customer-${JAR_VERSION}.jar
