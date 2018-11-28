package com.olacabs.jch.providers.xanitizer.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.olacabs.jch.sdk.spi.PostDeploySpi;
import com.olacabs.jch.providers.xanitizer.common.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostDeployController implements PostDeploySpi {

	public ProcessBuilder buildCommand() {
		ProcessBuilder builder = new ProcessBuilder(Constants.NOHUP,Constants.SH,System.getenv(Constants.XANITIZER_POST_DEPLOY_SCRIPT_PATH));
		builder.redirectError(new File(System.getenv(Constants.XANITIZER_POST_DEPLOY_ERROR_LOG_PATH)));
		builder.redirectOutput(new File(System.getenv(Constants.XANITIZER_POST_DEPLOY_OUTPUT_LOG_PATH)));
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
