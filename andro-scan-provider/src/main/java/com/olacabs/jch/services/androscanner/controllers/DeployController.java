package com.olacabs.jch.services.androscanner.controllers;

import java.io.File;
import java.io.IOException;

import com.olacabs.jch.sdk.spi.DeploySpi;
import com.olacabs.jch.services.androscanner.common.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeployController implements DeploySpi {

    public ProcessBuilder buildCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        return processBuilder;
    }

	public int executeCommand(ProcessBuilder processBuilder) {
       return 0;
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
