package com.olacabs.jch.providers.brakeman.controllers;

import java.io.File;
import java.io.IOException;

import com.olacabs.jch.providers.brakeman.common.Constants;
import com.olacabs.jch.sdk.spi.DeploySpi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeployController implements DeploySpi {

    public ProcessBuilder buildCommand() {
        ProcessBuilder builder = new ProcessBuilder(Constants.NOHUP,Constants.SH,System.getenv(Constants.BRAKEMAN_DEPLOY_SCRIPT_PATH));
        builder.redirectError(new File(System.getenv(Constants.BRAKEMAN_DEPLOY_ERROR_LOG_PATH)));
        builder.redirectOutput(new File(System.getenv(Constants.BRAKEMAN_DEPLOY_OUTPUT_LOG_PATH)));
        return builder;
    }

	public int executeCommand(ProcessBuilder processBuilder) {
        Process process;
        int exit;
        try {
            process = processBuilder.start();
            exit =  process.waitFor();
        } catch (IOException e) {
            exit = -1;
            log.error("IOException while installing brakeman"  +e);
        } catch (InterruptedException ie) {
            exit = -1;
            log.error("Interrupted Exception while installing brakeman", ie);
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
