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

#xanitizer scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/xanitizer/"
RUN /bin/bash -c "mkdir -p /home/src/xanitizer/logs"
RUN /bin/bash -c "mkdir -p /home/src/xanitizer/scripts"
RUN /bin/bash -c "mkdir -p /home/src/xanitizer/scripts/cli_script"
RUN /bin/bash -c "mkdir -p /home/src/xanitizer/provider"
RUN /bin/bash -c "mkdir -p /root/.m2"

#provider
ENV TOOL_NAME "Xanitizer"

#script envs
ENV WORKSPACE /home/src/xanitizer
ENV XANITIZER_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/XanitizerDeploy.sh
ENV XANITIZER_POST_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/XanitizerPostDeploy.sh

#logs envs
ENV XANITIZER_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/xanitizer_install_error.log
ENV XANITIZER_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/xanitizer_install_output.log
ENV XANITIZER_POST_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/xanitizer_post_install_error.log
ENV XANITIZER_POST_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/xanitizer_post_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log

#cli script
ENV XANITIZER_INSTALLED_PATH $WORKSPACE/scripts/cli_script/Xanitizer-4.0.3

#xanitizer library
COPY tools/Xanitizer-4.0.3-lin64-obf.zip /$WORKSPACE/scripts/cli_script/ 
#installation scripts
COPY scripts/XanitizerDeploy.sh $WORKSPACE/scripts/
COPY scripts/XanitizerPostDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#copy setting.xml file
COPY settings.xml /root/.m2/

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/xanitizer-provider.jar $WORKSPACE/provider/

#running sdk for xanitizer
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/xanitizer-provider.jar server $WORKSPACE/config.yml
