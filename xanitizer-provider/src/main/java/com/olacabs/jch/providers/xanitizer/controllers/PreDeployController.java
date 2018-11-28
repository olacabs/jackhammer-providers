package com.olacabs.jch.providers.xanitizer.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.olacabs.jch.sdk.spi.PreDeploySpi;

import com.olacabs.jch.providers.xanitizer.common.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreDeployController implements PreDeploySpi {

	public ProcessBuilder  buildCommand() {
		ProcessBuilder builder = new ProcessBuilder();
		return builder;
	}

	public int executeCommand(ProcessBuilder processBuilder) {
		 return 0;
	}

	public String getToolName() {
	    return Constants.PROVIDER_NAME;
	}
	

	public ProcessBuilder buildCommand(File scriptFile) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessBuilder buildCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
