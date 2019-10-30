#!/usr/bin/env bash
set -e

echo "Running with parameters [$@]"

exec java -Dcache.enabled=true $@ -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx1024m -Xms1024m -server -jar /app/spring-persistent-web.jar
