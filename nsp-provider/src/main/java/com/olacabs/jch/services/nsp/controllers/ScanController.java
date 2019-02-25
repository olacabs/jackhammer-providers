package com.olacabs.jch.services.nsp.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.nsp.common.Constants;
import com.olacabs.jch.services.nsp.common.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder builder = new ProcessBuilder();
        ProcessBuilder npmInstallBuilder = new ProcessBuilder();
        npmInstallBuilder.command(Constants.NMP_CMD,Constants.INSTALL);
        npmInstallBuilder.directory(new File(scanRequest.getTarget()));
        Map<String, String> envs = builder.environment();
        File tempFile = null;
        try {
            tempFile = File.createTempFile(Constants.TEMP_FILE_PREFIX, Constants.TEMP_FILE_SUFFIX);
            scanRequest.setResultFile(tempFile);
            Process process;
            process = npmInstallBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            log.error("Temp file could not created");
        } catch (InterruptedException ie) {
            log.error("InterruptedException while running npm command", ie);
        } catch (Exception e) {
            log.error("Exception while running nmap command",e);
        }
        builder.command(Constants.NSP_CMD,Constants.ARG_CHECK_OPTION,scanRequest.getTarget(),Constants.ARG_REPORT_OPTION,Constants.JSON,Constants.ARG_REDIRECT,tempFile.getAbsolutePath());
        builder.redirectOutput(ProcessBuilder.Redirect.appendTo(tempFile.getAbsoluteFile()));
        builder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
        return builder;
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
