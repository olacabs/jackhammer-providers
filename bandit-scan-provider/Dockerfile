#pulling jdk image
FROM openjdk:8

ENV HOME /root
ENV shell /bin/bash

#provider
ENV TOOL_NAME "Bandit"

#bandit scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/bandit/"
RUN /bin/bash -c "mkdir -p /home/src/bandit/logs"
RUN /bin/bash -c "mkdir -p /home/src/bandit/scripts"
RUN /bin/bash -c "mkdir -p /home/src/bandit/provider"

#script envs
ENV WORKSPACE /home/src/bandit
ENV BANDIT_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/BanditPreDeploy.sh
ENV BANDIT_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/BanditDeploy.sh

#logs envs
ENV BANDIT_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/bandit_pre_install_error.log
ENV BANDIT_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/bandit_pre_install_output.log
ENV BANDIT_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/bandit_install_error.log
ENV BANDIT_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/bandit_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUTPUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/BanditPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/BanditDeploy.sh $WORKSPACE/scripts/

#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/bandit-provider.jar $WORKSPACE/provider/

#exposing port 
EXPOSE 9998

#running sdk for bandit
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/bandit-provider.jar server $WORKSPACE/config.yml
#CMD java -server -cp ./provider/*:/home/src/bandit/JchSDK.jar com.olacabs.jch.sdk.application.JchSDKService server $WORKSPACE/config.yml
