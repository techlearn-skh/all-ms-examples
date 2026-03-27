FROM eclipse-temurin:21-jdk
LABEL maintainer ="kamal"
WORKDIR /myapp-dir
COPY ./build/libs/sb-4xx-demo-0.0.1-SNAPSHOT.jar sb-4xx-demo-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD [ "java", "-jar", "sb-4xx-demo-0.0.1-SNAPSHOT.jar" ]