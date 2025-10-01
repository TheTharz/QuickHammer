# Step 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Build args for private GitHub package
ARG GITHUB_TOKEN

# Configure Maven to use GitHub Packages
RUN mkdir -p /root/.m2
RUN echo "<settings>\
  <servers>\
    <server>\
      <id>github</id>\
      <username>TheTharz</username>\
      <password>${env.GITHUB_TOKEN}</password>\
    </server>\
  </servers>\
</settings>" > /root/.m2/settings.xml

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
