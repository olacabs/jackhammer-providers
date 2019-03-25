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
ENV TOOL_NAME "Exakat"

#exacat scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/Exakat/"
RUN /bin/bash -c "mkdir -p /home/src/Exakat/logs"
RUN /bin/bash -c "mkdir -p /home/src/Exakat/scripts"
RUN /bin/bash -c "mkdir -p /home/src/Exakat/provider"
RUN /bin/bash -c "mkdir -p /home/src/Exakat/scanner"

#script envs
ENV PATH /root/.rbenv/bin:/root/.rbenv/shims:$PATH
ENV WORKSPACE /home/src/Exakat
ENV EXAKAT_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/ExakatPreDeploy.sh
ENV EXAKAT_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/ExakatDeploy.sh
ENV EXAKAT_POST_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/ExakatPostDeploy.sh

#logs envs
ENV EXAKAT_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/exacat_pre_install_error.log
ENV EXAKAT_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/exacat_pre_install_output.log
ENV EXAKAT_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/exacat_install_error.log
ENV EXAKAT_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/exacat_install_output.log
ENV EXAKAT_POST_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/exacat_post_install_error.log
ENV EXAKAT_POST_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/exacat_post_install_output.log

ENV EXAKAT_INSTALLATION_PATH $WORKSPACE/scanner
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/ExakatPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/ExakatDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/exakat-provider.jar $WORKSPACE/provider/

#running sdk for exacat
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/exakat-provider.jar server $WORKSPACE/config.yml
