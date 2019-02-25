#pulling jdk image
FROM ubuntu:18.04

RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y git \
    && apt-get install -y iproute2 \
    && rm -rf /var/lib/apt/lists/*
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk

#tool setup
ENV HOME /root
ENV PATH $HOME/.rbenv/bin:$HOME/.rbenv/shims:$PATH
ENV shell /bin/bash

#provider
ENV TOOL_NAME "Dawnscanner"

#dawnscanner scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/dawnscanner/"
RUN /bin/bash -c "mkdir -p /home/src/dawnscanner/logs"
RUN /bin/bash -c "mkdir -p /home/src/dawnscanner/scripts"
RUN /bin/bash -c "mkdir -p /home/src/dawnscanner/provider"

#script envs
ENV WORKSPACE /home/src/dawnscanner
ENV DAWN_SCANNER_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/DawnscannerPreDeploy.sh
ENV DAWN_SCANNER_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/DawnscannerDeploy.sh

#logs envs
ENV DAWN_SCANNER_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/dawnscanner_pre_install_error.log
ENV DAWN_SCANNER_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/dawnscanner_pre_install_output.log
ENV DAWN_SCANNER_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/dawnscanner_install_error.log
ENV DAWN_SCANNER_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/dawnscanner_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUTPUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/DawnscannerPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/DawnscannerDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/dawn-scanner-provider.jar $WORKSPACE/provider/

#exposing port 
EXPOSE 9998

#running sdk for dawnscanner
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/dawn-scanner-provider.jar server $WORKSPACE/config.yml
