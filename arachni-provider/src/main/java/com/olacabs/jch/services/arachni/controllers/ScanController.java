package com.olacabs.jch.services.arachni.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.arachni.common.ExceptionMessages;
import com.olacabs.jch.services.arachni.common.Constants;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder arachniBuilder = new ProcessBuilder();
        ProcessBuilder arachniReporterBuilder = new ProcessBuilder();
        Map<String, String> envs = arachniBuilder.environment();
        File tempFile = null;
        File afrTempFile = null;
        try {
            Process process;
            tempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.TEMP_FILE_SUFFIX);
            afrTempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.AFR_TEMP_FILE_SUFFIX);
            arachniBuilder.directory(new File(envs.get(Constants.ARACHNI_INSTALLATION_PATH)));
            arachniBuilder.command(Constants.ARACHNI_CMD, scanRequest.getTarget().replace("\"",""), Constants.ARACHNI_REPORT_SAVE_ARG, afrTempFile.getAbsolutePath());
            arachniBuilder.redirectOutput(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
            arachniBuilder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
            process = arachniBuilder.start();
            process.waitFor();

            //reporter
            arachniReporterBuilder.directory(new File(envs.get(Constants.ARACHNI_INSTALLATION_PATH)));
            String outputFileArg = Constants.ARACHNI_REPORTER_OUT_FILE_ARG + tempFile.getAbsolutePath();
            scanRequest.setResultFile(tempFile);
            arachniReporterBuilder.command(Constants.ARACHNI_REPORTER_CMD, afrTempFile.getAbsolutePath(), outputFileArg);
            arachniReporterBuilder.redirectOutput(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
            arachniReporterBuilder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));

        } catch (IOException e) {
            log.error("Temp file could not created");
        } catch (InterruptedException ie) {
            log.error("InterruptedException while running arachni ",ie);
        } catch (Exception e) {
            log.error("Exception while running arachni ",e);
        }
        return arachniReporterBuilder;
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
