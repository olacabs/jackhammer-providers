#pulling jdk image
FROM ubuntu:18.04

RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y git \
    && apt-get install -y iproute2 \
    && apt-get install -y openjdk-8-jdk \
    && apt-get install -y inetutils-ping \
    && rm -rf /var/lib/apt/lists/*

#provider
ENV TOOL_NAME "TruffleHog"

#truffle hog scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/logs"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/scripts"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/scripts/cli_script"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/scripts/cli_script/truffleHog"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/provider"
RUN /bin/bash -c "mkdir -p /home/src/truffle_hog/scanner"

#script envs
ENV WORKSPACE /home/src/truffle_hog
ENV TRUFFLE_HOG_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/TruffleHogPreDeploy.sh
ENV TRUFFLE_HOG_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/TruffleHogDeploy.sh

#logs envs
ENV TRUFFLE_HOG_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/truffle_pre_install_error.log
ENV TRUFFLE_HOG_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/truffle_pre_install_output.log
ENV TRUFFLE_HOG_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/truffle_install_error.log
ENV TRUFFLE_HOG_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/truffle_install_output.log
ENV TRUFFLE_HOG_INSTALLATION_PATH $WORKSPACE/scanner
ENV TRUFFLE_HOG_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV TRUFFLE_HOG_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#cli script
ENV TRUFFLE_HOG_INSTALLED_PATH $WORKSPACE/scripts/cli_script/truffleHog

#truffle hog library
COPY tools/truffleHog /$WORKSPACE/scripts/cli_script/truffleHog

#installation scripts
COPY scripts/TruffleHogPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/TruffleHogDeploy.sh $WORKSPACE/scripts/

#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/

#sdk files
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/truffle-hog-provider.jar $WORKSPACE/provider/

#exposing port 
EXPOSE 8080

#running sdk for truffle hog
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/truffle-hog-provider.jar server $WORKSPACE/config.yml
