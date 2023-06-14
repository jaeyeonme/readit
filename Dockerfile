FROM eclipse-temurin:17

WORKDIR /app

ENTRYPOINT ["java","-jar","/app/blog-0.0.1-SNAPSHOT.jar"]
