FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY checkstyle.xml ./

RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline
COPY src ./src/
RUN ./mvnw clean package -DskipTests -Dcheckstyle.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom" , "-jar", "app.jar"]