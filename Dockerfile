FROM eclipse-temurin:17-jdk-jammy

COPY build/libs/* app.jar

CMD ["java", "-jar", "./app.jar"]
