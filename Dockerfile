FROM kumorelease-docker-virtual.artylab.expedia.biz/stratus/primer-base-dropwizard:8-1.6.0
# FROM openjdk:8-jdk

# Install application
COPY service/target/doppler-metastore-service.jar /app/bin/
COPY service/src/main/resources/config/* /app/conf/

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "/app/bin/doppler-metastore-service.jar", "server", "/app/conf/config/dev.yml"]
