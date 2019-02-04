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
ENV TOOL_NAME "Nmap"

#nmap scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/nmap/"
RUN /bin/bash -c "mkdir -p /home/src/nmap/logs"
RUN /bin/bash -c "mkdir -p /home/src/nmap/scripts"
RUN /bin/bash -c "mkdir -p /home/src/nmap/provider"

#script envs
ENV WORKSPACE /home/src/nmap
ENV NMAP_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/NmapPreDeploy.sh
ENV NMAP_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/NmapDeploy.sh

#logs envs
ENV NMAP_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/nmap_pre_install_error.log
ENV NMAP_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/nmap_pre_install_output.log
ENV NMAP_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/nmap_install_error.log
ENV NMAP_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/nmap_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/NmapPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/NmapDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/nmap-provider.jar $WORKSPACE/provider/

#running sdk for nmap
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/nmap-provider.jar server $WORKSPACE/config.yml
