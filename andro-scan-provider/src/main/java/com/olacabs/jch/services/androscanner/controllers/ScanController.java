package com.olacabs.jch.services.androscanner.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.androscanner.common.Constants;
import com.olacabs.jch.services.androscanner.common.ExceptionMessages;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> envs = processBuilder.environment();
        File tempFile = null;
        try {
            tempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.TEMP_FILE_SUFFIX);
        } catch (IOException e) {
            log.error("Temp file could not created");
        }

        scanRequest.setResultFile(tempFile);
        processBuilder.command(Constants.PYTHON_CMD,Constants.ANDRO_SCAN_SCRIPT,
                Constants.ANDRO_E_ARG,Constants.ANDRO_TWO_ARG,Constants.ANDRO_F_ARG,
               scanRequest.getTarget(),Constants.ANDRO_J_ARG,tempFile.getAbsolutePath());
        processBuilder.directory(new File(envs.get(Constants.ANDRO_SCANNER_INSTALLATION_PATH)));
        processBuilder.redirectError(new File(envs.get(Constants.ANDRO_SCANNER_ERROR_LOG_PATH)));
        return processBuilder;
    }

    public ScanResponse executeScanCommand(ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        try {
            Process process;
            process = builder.start();
            process.waitFor();
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running nsp", io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running nsp", ie);
        }
        return scanResponse;
    }

    public String getToolName() {
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
