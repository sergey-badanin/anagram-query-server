FROM maven:3.9.4-eclipse-temurin-17 as build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:17-jre-jammy as app
COPY --from=build /app/target/anagram-service-0.0.1-jar-with-dependencies.jar /anagram-service.jar
CMD ["java", "-jar", "/anagram-service.jar", "8091"]
