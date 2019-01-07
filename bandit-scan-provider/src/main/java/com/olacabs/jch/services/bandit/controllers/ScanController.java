package com.olacabs.jch.services.bandit.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.bandit.common.Constants;
import com.olacabs.jch.services.bandit.common.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder builder = new ProcessBuilder();
        Map<String, String> envs = builder.environment();
        File tempFile = null;
        try {
            tempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.TEMP_FILE_SUFFIX);
        } catch (IOException e) {
            log.error("Temp file could not created");
        }
        scanRequest.setResultFile(tempFile);

        builder.command(Constants.BANDIT_CMD, Constants.WRITE_REPORT_TO_FILENAME_CMD, tempFile.getAbsolutePath(),Constants.OUTPUT_FILE_FORMAT_CMD, Constants.OUTPUT_FILE_FORMAT,Constants.FIND_AND_PROCESS_FILES_IN_SUBDIRECTORIES,scanRequest.getTarget());
       // builder.directory(new File(scanRequest.getTarget()));

        builder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
        return builder;
    }

    public ScanResponse executeScanCommand(ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        try {

            log.info("process execution started");
            Process process;
            process = builder.start();
            process.waitFor();
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running bandit", io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running bandit", ie);
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
