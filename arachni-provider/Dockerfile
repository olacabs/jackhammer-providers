#pulling jdk image
FROM ubuntu:18.04
  
RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y --no-install-recommends git \
    && apt-get install -y --no-install-recommends iproute2 \
    && apt-get install -y --no-install-recommends openjdk-8-jdk \
    && apt-get install -y inetutils-ping \
    && rm -rf /var/lib/apt/lists/*
#RUN apt-get update 
#RUN apt-get install -y openjdk-8-jdk

#provider
ENV TOOL_NAME "Arachni"

#arachni scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/arachni/"
RUN /bin/bash -c "mkdir -p /home/src/arachni/logs"
RUN /bin/bash -c "mkdir -p /home/src/arachni/scripts"
RUN /bin/bash -c "mkdir -p /home/src/arachni/provider"
RUN /bin/bash -c "mkdir -p /home/src/arachni/scanner"
#script envs
ENV WORKSPACE /home/src/arachni
ENV ARACHNI_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/ArachniPreDeploy.sh
ENV ARACHNI_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/ArachniDeploy.sh

#scanner installtion path
ENV ARACHNI_INSTALLATION_PATH $WORKSPACE/scanner/bin
#logs envs
ENV ARACHNI_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/arachni_pre_install_error.log
ENV ARACHNI_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/arachni_pre_install_output.log
ENV ARACHNI_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/arachni_install_error.log
ENV ARACHNI_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/arachni_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/ArachniPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/ArachniDeploy.sh $WORKSPACE/scripts/
#host setup
COPY scripts/docker-host.sh $WORKSPACE/scripts/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/arachni-provider.jar $WORKSPACE/provider/


#exposing port 
EXPOSE 9998

#running sdk for arachni
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/arachni-provider.jar server $WORKSPACE/config.yml
