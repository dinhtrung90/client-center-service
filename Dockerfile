FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8086

# Install vim
RUN apt-get update && apt-get install -y vim

# Create user
RUN useradd --create-home clientcenter && \
    mkdir -p /home/clientcenter/ && \
    mkdir -p /home/clientcenter/logs

# Copy file to container
COPY "target/clientcenter-service.jar" "/home/clientcenter/clientcenter-service.jar"
COPY "etc/entrypoint.sh" "/home/clientcenter/"

# Edit permissions
RUN chown clientcenter:clientcenter /home/clientcenter/* && \
    chmod 700 /home/clientcenter/entrypoint.sh

# Change working dir
WORKDIR /home/clientcenter

# Entry point
ENTRYPOINT ./entrypoint.sh
