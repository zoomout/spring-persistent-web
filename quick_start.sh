#!/usr/bin/env bash

function health_check() {
    done=0
    tries=0
    echo "Web server health check: '404' server is up, '000' - no connection, else - some other problem"
    while [[ "${done}" -eq "0" && "${tries}" -lt "10" ]]; do
        echo " * Attempt #${tries}..."
        sleep 2
        status=$(curl -fso /dev/null -w "%{http_code}" "http://localhost:10080")
        if [[ "$status" -eq "404" ]]; then
            done=1
        fi
        echo "Response code is: $status"
        ((tries+=1))
    done
    if [[ "${done}" -eq "0" ]]; then
        echo "Web server is not healthy. Giving up..."
        exit 1
    fi

}

echo " * Build docker image"
./gradlew buildDockerImage -PcacheEnabled=${ENABLE_CACHE:=false}

echo " * Start docker compose"
docker-compose -f ./deploy/docker-compose.yml up -d

health_check

echo " * Configure database"
./gradlew -Dflyway.configFiles=flyway/flyway.config flywayMigrate -i

echo " * Make REST call"
curl -i -X POST http://localhost:10080/products -H 'Content-Type: application/json' -d '{"title": "ProductTitle"}'

echo "To stop: docker-compose -f ./deploy/docker-compose.yml down -v"
