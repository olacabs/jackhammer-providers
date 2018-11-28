#pulling jdk image
FROM openjdk:8

#provider
ENV TOOL_NAME "RetireJs"

#retirejs scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/retirejs/"
RUN /bin/bash -c "mkdir -p /home/src/retirejs/logs"
RUN /bin/bash -c "mkdir -p /home/src/retirejs/scripts"
RUN /bin/bash -c "mkdir -p /home/src/retirejs/provider"

#script envs
ENV WORKSPACE /home/src/retirejs
ENV RETIRE_JS_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/RetireJsPreDeploy.sh
ENV RETIRE_JS_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/RetireJsDeploy.sh

#logs envs
ENV RETIRE_JS_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/retire_js_pre_install_error.log
ENV RETIRE_JS_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/retire_js_pre_install_output.log
ENV RETIRE_JS_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/retire_js_install_error.log
ENV RETIRE_JS_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/retire_js_install_output.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV SCAN_OUT_PUT_LOG_PATH $WORKSPACE/logs/scan_output.log

#installation scripts
COPY scripts/RetireJsPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/RetireJsDeploy.sh $WORKSPACE/scripts/

#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE

#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/retirejs-provider.jar $WORKSPACE/provider/

#exposing port 
EXPOSE 9998

#running sdk for retirejs
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/retirejs-provider.jar server $WORKSPACE/config.yml
