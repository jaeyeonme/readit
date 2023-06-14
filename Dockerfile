FROM eclipse-temurin:17

WORKDIR /tcat

COPY ./build/libs/blog-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
