# Clinic-Appointment-System
This is the prototype for showing how Clinic Appointment System performs with the help of Java JDBC and Spring Boot Framework 

## How to Run:
• Download and open **Docker Desktop** in PC/Mac/Linux (link->https://www.docker.com/products/docker-desktop/)
• Run the following commands in terminal:
  ```console
  # clinic-appointment-system -> name of jar file generated
  docker build -t clinic-appointment-system:3.0.5 .
  # container_name can be any name preferred
  docker run -d -p 8080:8080 --name <container_name> clinic-appointment-system:3.0.5
  ```
