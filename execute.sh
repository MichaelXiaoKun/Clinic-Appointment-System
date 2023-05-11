#!/bin/zsh
# clinic-appointment-system -> name of jar file generated
docker build -t clinic-appointment-system:3.0.5 .
# container_name can be any name preferred
docker stop cas
docker rm cas
docker run -d -p 8080:8080 --name cas clinic-appointment-system:3.0.5