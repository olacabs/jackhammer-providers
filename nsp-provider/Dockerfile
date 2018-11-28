#pulling jdk image
FROM openjdk:8

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

#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/nsp-provider.jar $WORKSPACE/provider/

#running sdk for nsp
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/nsp-provider.jar server $WORKSPACE/config.yml
