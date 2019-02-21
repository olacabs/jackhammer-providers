package com.olacabs.jch.services.trufflehog.controllers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
import com.olacabs.jch.services.trufflehog.common.Constants;
import com.olacabs.jch.services.trufflehog.common.ExceptionMessages;

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
        processBuilder.command(Constants.PYTHON_CMD, Constants.TRUFFLE_HOG_SCAN_SCRIPT, Constants.TRUFFLE_HOG_REPO_ARG, scanRequest.getTarget(), Constants.OUTPUT_FILE_ARG, tempFile.getAbsolutePath());
        processBuilder.directory(new File(envs.get(Constants.TRUFFLE_HOG_INSTALLED_PATH)));
        processBuilder.redirectError(new File(envs.get(Constants.TRUFFLE_HOG_ERROR_LOG_PATH)));
        return processBuilder;
    }

    public ScanResponse executeScanCommand(final ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        try {
            final Duration timeout = Duration.ofMinutes(15);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future<String> handler = executor.submit(new Callable() {
                @Override
                public String call() throws Exception {
                    log.info("started scanning the repo.....");
                    Process process;
                    process = builder.start();
                    process.waitFor();
                    return "Success";
                }
            });
            try {
                handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                log.info("scanning completed.....");
            } catch (TimeoutException e) {
                handler.cancel(true);
                log.info("TimeoutException while cloning the repo.....");
            }
            executor.shutdownNow();
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running nsp", ie);
        } catch (Exception e) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.EXCEPTION);
            log.error("IOException while running nsp", e);
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
