#pulling jdk image
FROM ubuntu:18.04
  
RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y git \
    && apt-get install -y iproute2 \
    && rm -rf /var/lib/apt/lists/*
RUN apt-get update 
RUN apt-get install -y openjdk-8-jdk

#provider
ENV TOOL_NAME "AndroScanner"

#andro scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/andro/"
RUN /bin/bash -c "mkdir -p /home/src/andro/logs"
RUN /bin/bash -c "mkdir -p /home/src/andro/scripts"
RUN /bin/bash -c "mkdir -p /home/src/andro/provider"
RUN /bin/bash -c "mkdir -p /home/src/andro/scanner"

#script envs
ENV WORKSPACE /home/src/andro
ENV ANDRO_SCANNER_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/AndroScannerPreDeploy.sh
ENV ANDRO_SCANNER_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/AndroScannerDeploy.sh

#logs envs
ENV ANDRO_SCANNER_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/andro_pre_install_error.log
ENV ANDRO_SCANNER_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/andro_pre_install_output.log
ENV ANDRO_SCANNER_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/andro_install_error.log
ENV ANDRO_SCANNER_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/andro_install_output.log
ENV ANDRO_SCANNER_INSTALLATION_PATH $WORKSPACE/scanner
ENV ANDRO_SCANNER_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV ANDRO_SCANNER_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/AndroScannerPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/AndroScannerDeploy.sh $WORKSPACE/scripts/

#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/andro-scan-provider.jar $WORKSPACE/provider/

#coping scanner 
COPY androscan /home/src/andro/scanner/


#running sdk for andro
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/andro-scan-provider.jar server $WORKSPACE/config.yml
