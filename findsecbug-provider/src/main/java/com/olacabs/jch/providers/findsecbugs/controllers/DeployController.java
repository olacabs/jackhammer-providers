package com.olacabs.jch.providers.findsecbugs.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.olacabs.jch.providers.findsecbugs.common.Constants;
import com.olacabs.jch.sdk.spi.DeploySpi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeployController implements DeploySpi {

    public ProcessBuilder buildCommand() {
        ProcessBuilder envBuilder = new ProcessBuilder();
        Map<String, String> env = envBuilder.environment();
        ProcessBuilder builder = new ProcessBuilder(Constants.NOHUP,Constants.SH,env.get(Constants.FIND_SEC_BUGS_DEPLOY_SCRIPT_PATH));
        builder.redirectError(new File(env.get(Constants.FIND_SEC_BUGS_DEPLOY_ERROR_LOG_PATH)));
        builder.redirectOutput(new File(env.get(Constants.FIND_SEC_BUGS_DEPLOY_OUTPUT_LOG_PATH)));
        return builder;
    }

	public int executeCommand(ProcessBuilder processBuilder) {
        Process process;
        int exit;
        try {
            process = processBuilder.start();
            exit =  process.waitFor();
        } catch (IOException io) {
            exit = -1;
            log.error("IOException while installing FindSecBugs",io);
        } catch (InterruptedException ie) {
            exit = -1;
            log.error("InterruptedException while installing FindSecBugs",ie);
        }
        return exit;
	}

    public ProcessBuilder buildCommand(String command) {
        // TODO Auto-generated method stub
        return null;
    }

	public ProcessBuilder buildCommand(File scriptFile) {
		// TODO Auto-generated method stub
		return null;
	}

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
 
}
