# FROM maven:3-openjdk-17 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests
#
# # Run stage
# FROM openjdk:17-jdk-slim
# WORKDIR /app
# COPY --from=build /app/target/library_management_service-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","app.jar"]

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/library_management_service-0.0.1-SNAPSHOT.jar library_management_service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","library_management_service-0.0.1-SNAPSHOT.jar"]

