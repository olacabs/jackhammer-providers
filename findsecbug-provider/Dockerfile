#pulling jdk image
FROM openjdk:8
#FROM openjdk:8i

#xanitizer scripts, logs and provider directories
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/"
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/logs"
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/scripts"
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/scripts/cli_script"
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/provider"
RUN /bin/bash -c "mkdir -p /root/.m2"
RUN /bin/bash -c "mkdir -p /home/src/findsecbugs/.m2"
RUN /bin/bash -c "mkdir -p /home/.m2"

#provider
ENV TOOL_NAME "FindSecBugs"

#script envs
ENV WORKSPACE /home/src/findsecbugs
ENV FIND_SEC_BUGS_PRE_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/FindSecBugsPreDeploy.sh
ENV FIND_SEC_BUGS_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/FindSecBugsDeploy.sh
ENV FIND_SEC_BUGS_POST_DEPLOY_SCRIPT_PATH $WORKSPACE/scripts/FindSecBugsPostDeploy.sh

#logs envs
ENV FIND_SEC_BUGS_PRE_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/find_sec_bugs_pre_install_error.log
ENV FIND_SEC_BUGS_PRE_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/find_sec_bugs_pre_install_output.log
ENV FIND_SEC_BUGS_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/find_sec_bugs_install_error.log
ENV FIND_SEC_BUGS_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/find_sec_bugs_install_output.log
ENV FIND_SEC_BUGS_POST_DEPLOY_ERROR_LOG_PATH $WORKSPACE/logs/find_sec_bugs_post_install_error.log
ENV FIND_SEC_BUGS_POST_DEPLOY_OUTPUT_LOG_PATH $WORKSPACE/logs/find_sec_bugs_post_install_output.log
ENV MAVEN_COMPILE_OUT_PUT_PATH $WORKSPACE/logs/maven_compile_out_put_path.log
ENV MAVEN_COMPILE_ERROR_PATH $WORKSPACE/logs/maven_compile_error_path.log
ENV SCAN_ERROR_LOG_PATH $WORKSPACE/logs/scan_error.log
ENV M2_HOME /usr/share/maven

#cli script
ENV FIND_SEC_BUGS_INSTALLED_PATH $WORKSPACE/scripts/cli_script/spotbugs-3.1.7/bin

#installation scripts
COPY scripts/FindSecBugsPreDeploy.sh $WORKSPACE/scripts/
COPY scripts/FindSecBugsDeploy.sh $WORKSPACE/scripts/
COPY scripts/FindSecBugsPostDeploy.sh $WORKSPACE/scripts/

#setting file

COPY settings.xml /root/.m2/
#sdk files
#COPY JchSDK.jar $WORKSPACE
COPY config.yml $WORKSPACE
COPY spotbugs-3.1.7.zip $WORKSPACE/scripts/cli_script/
#working dir
WORKDIR $WORKSPACE

#coping provider 
COPY target/find-sec-bugs-provider.jar $WORKSPACE/provider/

#exposing port 
EXPOSE 9998

#running sdk for xanitizer
CMD java -Xms500m -Xmx2920m -jar $WORKSPACE/provider/find-sec-bugs-provider.jar server $WORKSPACE/config.yml
