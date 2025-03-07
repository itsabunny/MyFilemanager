# Byggfas: Använd JDK 23 för att bygga applikationen
FROM eclipse-temurin:23-jdk AS build

# Sätt arbetskatalogen i containern
WORKDIR /app

# Kopiera projektfilerna till containern (Fixat!)
COPY . .

# Se till att `gradlew` har exekveringsrättigheter
RUN chmod +x ./gradlew

# Bygg applikationen med Gradle
RUN ./gradlew build --no-daemon

# Runtime-fas: Använd JRE 23 för att köra applikationen
FROM eclipse-temurin:23-jre

# Sätt arbetskatalogen i containern
WORKDIR /app

# Kopiera den byggda JAR-filen från build-steget
COPY --from=build /app/build/libs/*.jar app.jar

# Exponera port 8080
EXPOSE 8080

# Kör applikationen
ENTRYPOINT ["java", "-jar", "app.jar"]
