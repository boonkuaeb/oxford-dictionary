FROM java:8
WORKDIR /
ADD  target/dictionary-1.0.0-SNAPSHOT.jar //
EXPOSE 8080
ENTRYPOINT [ "java","-jar", "/dictionary-1.0.0-SNAPSHOT.jar"]
