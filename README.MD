

```
mvn package -DskipTests=true docker:build -Dtodo-profile=default

```


```docker
docker run --name dictionary-default -p8080:8080  -d dictionary-img-default

```

