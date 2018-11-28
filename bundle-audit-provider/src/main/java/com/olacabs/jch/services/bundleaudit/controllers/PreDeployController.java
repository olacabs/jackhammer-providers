package com.olacabs.jch.services.bundleaudit.controllers;

import java.io.File;
import java.io.IOException;

import com.olacabs.jch.sdk.spi.PreDeploySpi;

import com.olacabs.jch.services.bundleaudit.common.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreDeployController implements PreDeploySpi {

    public ProcessBuilder buildCommand() {
        ProcessBuilder builder = new ProcessBuilder(Constants.NOHUP, Constants.SH, System.getenv(Constants.BUNDLE_AUDIT_PRE_DEPLOY_SCRIPT_PATH));
        builder.redirectError(new File(System.getenv(Constants.BUNDLE_AUDIT_PRE_DEPLOY_ERROR_LOG_PATH)));
        builder.redirectOutput(new File(System.getenv(Constants.BUNDLE_AUDIT_PRE_DEPLOY_OUTPUT_LOG_PATH)));
        return builder;
    }

    public int executeCommand(ProcessBuilder processBuilder) {
        Process process;
        int exit;
        try {
            process = processBuilder.start();
            exit = process.waitFor();
        } catch (IOException io) {
            exit = -1;
            log.error("IOException while installing bundle audit", io);
        } catch (InterruptedException ie) {
            exit = -1;
            log.error("InterruptedException while installing bundle audit", ie);
        }
        return exit;
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
