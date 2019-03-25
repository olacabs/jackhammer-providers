package com.olacabs.jch.services.exakat.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.exakat.common.ExceptionMessages;
import com.olacabs.jch.services.exakat.common.Constants;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;


@Slf4j
public class ScanController implements ScanSpi {

    public ProcessBuilder buildScanCommand(ScanRequest scanRequest) {
        ProcessBuilder cloneBuilder = new ProcessBuilder();
        ProcessBuilder runBuilder = new ProcessBuilder();
        ProcessBuilder jsonReportBuilder = new ProcessBuilder();
        Map<String, String> envs = cloneBuilder.environment();

        //update builder
        String tempFile = UUID.randomUUID().toString().replace("-", "");
        try {
            String exakatRunner = Constants.EXAKAT_INSTALLATION_PATH + Constants.EXAKAT_RUNNER;
            String[] projectInfo = scanRequest.getTarget().split("/");
            String projectName = projectInfo[projectInfo.length - 1];

            Process process;
            cloneBuilder.command(Constants.PHP_CMD, exakatRunner, Constants.INIT, Constants.P_ARG, projectName, scanRequest.getTarget());
            runBuilder.directory(new File(System.getenv(Constants.EXAKAT_INSTALLATION_PATH)));
            process = cloneBuilder.start();
            process.waitFor();

            runBuilder.command(Constants.PHP_CMD, exakatRunner, Constants.P_ARG, projectName);
            runBuilder.directory(new File(System.getenv(Constants.EXAKAT_INSTALLATION_PATH)));
            process = runBuilder.start();
            process.waitFor();

            StringBuilder resultFile = new StringBuilder();

            resultFile.append(Constants.EXAKAT_INSTALLATION_PATH);
            resultFile.append(Constants.PROJECT_DIR);
            resultFile.append(projectName);
            resultFile.append(Constants.SLASH);
            resultFile.append(tempFile);
            resultFile.append(Constants.JSON_EXTENSION);

            jsonReportBuilder.command(Constants.PHP_CMD, exakatRunner, Constants.REPORT,
                    Constants.P_ARG, projectName, Constants.FORMAT_ARGE, Constants.JSON,
                    Constants.FILE_ARGE, tempFile, Constants.T_ARG, Constants.SECURITY);
            jsonReportBuilder.directory(new File(System.getenv(Constants.EXAKAT_INSTALLATION_PATH)));
            jsonReportBuilder.redirectOutput(new File(envs.get(Constants.SCAN_OUT_PUT_LOG_PATH)));
            jsonReportBuilder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));

            scanRequest.setResultFile(new File(resultFile.toString()));
        } catch (IOException e) {
            log.error("Temp file could not created/IOException while updating wp scan");
        } catch (InterruptedException ie) {
            log.error("InterruptedException while updating wp scan", ie);
        }
        return jsonReportBuilder;
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
            log.error("IOException while running wp scan", io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running wp scan", ie);
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
