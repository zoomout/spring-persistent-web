# Demo Spring boot web application with persistence

# Work with Database
- start DB
    ```
    docker-compose -f ./deploy/docker-compose.yml up -d 
    ```
- stop DB
    ```
    docker-compose -f ./deploy/docker-compose.yml down
    ```

# DB migration
```
./gradlew -Dflyway.configFiles=flyway/flyway.config flywayMigrate -i
```