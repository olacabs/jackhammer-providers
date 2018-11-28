package com.olacabs.jch.services.bundleaudit.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.bundleaudit.common.Constants;
import com.olacabs.jch.services.bundleaudit.common.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {
    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        List<String> command = new ArrayList<String>();
        command.add(Constants.BUNDLE_AUDIT_CMD);
        command.add("check");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(new File(scanRequest.getTarget()));
        builder.redirectError(new File(System.getenv(Constants.SCAN_ERROR_LOG_PATH)));
        return builder;
    }

    public ScanResponse executeScanCommand(ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        BufferedReader bufferedReader = null;
        try {
            Process process;
            process = builder.start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            scanResponse.setResultReader(bufferedReader);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running bundle audit", io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running bundle audit", ie);
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
