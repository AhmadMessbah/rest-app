FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/api-gateway.jar .
COPY filebeat.yml .
RUN apt-get update && apt-get install -y curl && \
    curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-8.15.2-linux-x86_64.tar.gz && \
    tar xzvf filebeat-8.15.2-linux-x86_64.tar.gz && \
    mv filebeat-8.15.2-linux-x86_64/filebeat /usr/local/bin/ && \
    rm -rf filebeat-8.15.2-linux-x86_64.tar.gz
RUN mkdir /logs && chmod -R 777 /logs
CMD ["sh", "-c", "filebeat -e -c filebeat.yml & java -jar api-gateway.jar"]