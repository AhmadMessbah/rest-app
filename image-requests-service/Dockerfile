FROM python:3.12-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY src ./src
COPY filebeat.yml .
RUN apt-get update && apt-get install -y curl && \
    curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-8.15.2-linux-x86_64.tar.gz && \
    tar xzvf filebeat-8.15.2-linux-x86_64.tar.gz && \
    mv filebeat-8.15.2-linux-x86_64/filebeat /usr/local/bin/ && \
    rm -rf filebeat-8.15.2-linux-x86_64.tar.gz
RUN mkdir /logs && chmod -R 777 /logs
# Fix filebeat.yml permissions
RUN chmod 644 filebeat.yml
# Run Filebeat only if LOGSTASH_HOST is set, otherwise run Uvicorn only
CMD ["sh", "-c", "if [ -n \"$LOGSTASH_HOST\" ]; then filebeat -e -c filebeat.yml & fi; uvicorn src.main:app --host 0.0.0.0 --port 8000"]