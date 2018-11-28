package com.olacabs.jch.services.androscanner.controllers;

import java.io.File;

import com.olacabs.jch.sdk.spi.PreDeploySpi;

import com.olacabs.jch.services.androscanner.common.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreDeployController implements PreDeploySpi {

    public ProcessBuilder buildCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        return processBuilder;
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
