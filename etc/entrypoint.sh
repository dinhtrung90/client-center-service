#!/bin/bash

set -x

# Running java app
java -server \
     -Dserver.port=8086 \
     -jar /home/clientcenter/clientcenter-service.jar
