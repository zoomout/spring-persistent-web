# Demo Spring boot web application with persistence

# Quick start
 - To start the application run: `./quick_start.sh`

# Test
 - unit test
 ```
./gradlew cleanTest test
```
- integration test
```
./gradlew cleanTest integrationTest
```

# Start in docker container
- build docker image
    ```
    ./gradlew buildDockerImage
    ```
- start docker compose (web application and database)
    ```
    docker-compose -f ./deploy/docker-compose.yml up -d
    ```
- stop docker compose
    ```
    docker-compose -f ./deploy/docker-compose.yml down -v
    ```
# DB migration
```
./gradlew -Dflyway.configFiles=flyway/flyway.config flywayMigrate -i
```

# Enable cache
To enable cache:
 - in IDE add variable `cache.enabled=true` into Run configuration | Default: false
 - in docker-compose `ENABLE_CACHE=true ./quick_start.sh` | Default: false
 - in docker image `./gradlew buildDockerImage -PcacheEnabled=true` | Default: true

# Test coverage
To generate test coverage report using Jacoco Plugin:
```
./gradlew cleanTest test integrationTest jacocoTestReport
```

# Jetty vs Tomcat performance comparison
Load test is done using gatling project here: https://github.com/zoomout/gatling-web-test 

Jetty percentiles table
![jetty_table](performance/reports/jetty-table.jpg)
Tomcat percentiles table
![tomcat_table](performance/reports/tomcat-table.jpg)
Jetty percentiles graph
![jetty_graph](performance/reports/jetty-graph.jpg)
Tomcat percentiles graph
![tomcat_graph](performance/reports/tomcat-graph.jpg)

