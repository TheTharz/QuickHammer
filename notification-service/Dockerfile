# Step 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Build args for private GitHub package
ARG GITHUB_TOKEN

# Configure Maven to use GitHub Packages
RUN mkdir -p /root/.m2

# Create settings.xml with proper token substitution
RUN echo '<settings>' > /root/.m2/settings.xml && \
    echo '  <servers>' >> /root/.m2/settings.xml && \
    echo '    <server>' >> /root/.m2/settings.xml && \
    echo '      <id>github</id>' >> /root/.m2/settings.xml && \
    echo '      <username>TheTharz</username>' >> /root/.m2/settings.xml && \
    echo "      <password>${GITHUB_TOKEN}</password>" >> /root/.m2/settings.xml && \
    echo '    </server>' >> /root/.m2/settings.xml && \
    echo '  </servers>' >> /root/.m2/settings.xml && \
    echo '</settings>' >> /root/.m2/settings.xml

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
