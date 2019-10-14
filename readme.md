# Demo Spring boot web application with persistence

# Work with Database
- start DB
    ```
    docker-compose -f ./deploy/docker-compose.yml up -d 
    ```
- stop DB
    ```
    docker-compose -f ./deploy/docker-compose.yml down # don't cleanup volumes
    docker-compose -f ./deploy/docker-compose.yml down -v # do cleanup volumes
    ```
- cleanup docker volumes (to remove DB data)
    ```
  docker system prune --volumes
   ```
# DB migration
```
./gradlew -Dflyway.configFiles=flyway/flyway.config flywayMigrate -i
```