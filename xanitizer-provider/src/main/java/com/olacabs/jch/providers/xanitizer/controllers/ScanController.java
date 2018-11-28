package com.olacabs.jch.providers.xanitizer.controllers;

import java.io.File;
import java.io.IOException;

import com.olacabs.jch.providers.xanitizer.common.ExceptionMessages;
import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.providers.xanitizer.common.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder builder = new ProcessBuilder();
        String rootDirectory = Constants.ROOT_DIRECTORY + scanRequest.getTarget();

        //scan error log and running directory
        builder.redirectError(new File(System.getenv(Constants.SCAN_ERROR_LOG_PATH)));
        builder.directory(new File(System.getenv(Constants.XANITIZER_INSTALLED_PATH)));
        try {
            //build scan cmd
            File tempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.TEMP_FILE_SUFFIX);
            String findingsListReportOutputFile = Constants.FINDINGS_LIST_REPORT_OUTPUT_FILE + tempFile.getAbsolutePath();
            builder.command(
                    Constants.CLI_SCRIPT_COMMAND,
                    rootDirectory,
                    findingsListReportOutputFile,
                    Constants.LOG_LEVEL,
                    Constants.CREATE_SNAPSHOT
            );
            //set scan result file
            scanRequest.setResultFile(tempFile);
        } catch (IOException io) {
            log.error("IOException while building Xanitizer scan command", io);
        }
        return builder;
    }


    public ScanResponse executeScanCommand(ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        Process process;
        try {
            process = builder.start();
            process.waitFor();
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running Xanitizer", io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running Xanitizer", ie);
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
