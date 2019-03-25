#pulling jdk image
FROM ubuntu:18.04

RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y git \
    && apt-get install -y iproute2 \
    && apt-get install -y inetutils-ping \
    && rm -rf /var/lib/apt/lists/*
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk

#provider
ENV TOOL_NAME "Nsp"

#nsp scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/nsp/"
RUN /bin/bash -c "mkdir -p /home/src/nsp/logs"
RUN /bin/bash -c "mkdir -p /home/src/nsp/scripts"
RUN /bin/bash -c "mkdir -p /home/src/nsp/provider"

#script envs
ENV WORKSPACE /home/src/nsp
ENV NSP_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/NspPreDeploy.sh
ENV NSP_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/NspDeploy.sh

#logs envs
ENV NSP_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/nsp_pre_install_error.log
ENV NSP_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/nsp_pre_install_output.log
ENV NSP_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/nsp_install_error.log
ENV NSP_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/nsp_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/NspPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/NspDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/

#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/nsp-provider.jar $WORKSPACE/provider/

#running sdk for nsp
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/nsp-provider.jar server $WORKSPACE/config.yml
