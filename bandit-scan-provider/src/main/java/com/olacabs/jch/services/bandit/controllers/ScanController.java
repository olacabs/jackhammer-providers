package com.olacabs.jch.services.bandit.controllers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

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

    public ScanResponse executeScanCommand(final ProcessBuilder builder) {
        ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        try {
            final Duration timeout = Duration.ofMinutes(20);
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
            log.error("InterruptedException while running bandit", ie);
        } catch (ExecutionException ee) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running bandit", ee);
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
