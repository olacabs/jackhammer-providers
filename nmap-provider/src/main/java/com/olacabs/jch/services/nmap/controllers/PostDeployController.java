package com.olacabs.jch.services.nmap.controllers;

import java.io.File;

import com.olacabs.jch.sdk.spi.PostDeploySpi;
import com.olacabs.jch.services.nmap.common.Constants;

public class PostDeployController implements PostDeploySpi {

    public ProcessBuilder buildCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        return processBuilder;
    }

    public int executeCommand(ProcessBuilder processBuilder) {
        return 0;
    }

    public String getToolName()
    {
        return Constants.PROVIDER_NAME;
    }

    public ProcessBuilder buildCommand() {
        // TODO Auto-generated method stub
        return null;
    }

    public ProcessBuilder buildCommand(File scriptFile) {
        // TODO Auto-generated method stub
        return null;
    }

}
