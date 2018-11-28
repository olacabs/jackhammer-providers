#pulling jdk image
FROM openjdk:8

#brakeman logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/brakeman/"
RUN /bin/bash -c "mkdir -p /home/src/brakeman/logs"
RUN /bin/bash -c "mkdir -p /home/src/brakeman/scripts"
RUN /bin/bash -c "mkdir -p /home/src/brakeman/provider"

#provider
ENV TOOL_NAME "Brakeman"

#script envs
ENV WORKSPACE /home/src/brakeman
ENV BRAKEMAN_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/BrakemanPreDeploy.sh
ENV BRAKEMAN_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/BrakemanDeploy.sh

#logs envs
ENV BRAKEMAN_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/brakeman_pre_install_error.log
ENV BRAKEMAN_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/brakeman_pre_install_output.log
ENV BRAKEMAN_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/brakeman_install_error.log
ENV BRAKEMAN_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/brakeman_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log

#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#installation scripts
COPY scripts/BrakemanPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/BrakemanDeploy.sh $WORKSPACE/scripts/

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/brakeman-provider.jar $WORKSPACE/provider/

#exposing port
EXPOSE 8080

#running sdk for brakeman
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/brakeman-provider.jar server $WORKSPACE/config.yml
