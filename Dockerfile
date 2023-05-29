FROM eclipse-temurin:17

WORKDIR /tcat

COPY blog-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","blog.jar"]
