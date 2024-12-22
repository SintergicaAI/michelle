FROM eclipse-temurin:21-jdk-jammy

COPY build/libs/* app.jar

CMD ["java", "-jar", "./app.jar"]
