# messaging-dapr

## Building the dependency locally

1. Download the repo

```shell
git clone git@github.com:mcruzdev/quarkus-dapr.git
```

2. Build the application

```shell
cd quarkus-dapr
git switch reactive-messaging-dapr
cd quarkus-reactive-messaging-dapr
mvn clean install -DskipTests
```

## Running the application

1. Init Dapr
```shell
dapr init
```

2. Execute the application in Dev mode

```shell
quarkus dev
```

3. Execute the Dapr sidecar

```shell
dapr run --app-id myapp --dapr-http-port 3500 --app-port 8080 --dapr-grpc-port 50001
```

4. See the log generated by `SubscribeResource.java`

```java
    @Outgoing("orders.each.second")
    @NonBlocking
    public Multi<String> produce() {
        return Multi.createFrom().ticks()
              .every(Duration.ofSeconds(1))
              .map(l ->{
                  Log.infof("producing message to orders.each.second %s", l);
                  return l.toString();
              });
    }
```
