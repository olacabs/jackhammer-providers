
package com.olacabs.jch.services.bundleaudit.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

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

    public ScanResponse executeScanCommand(final ProcessBuilder builder) {
        final ScanResponse scanResponse = new ScanResponse();
        scanResponse.setStartTime(System.currentTimeMillis());
        try {
            final Duration timeout = Duration.ofMinutes(5);
            ExecutorService executor = Executors.newSingleThreadExecutor();

            final Future<String> handler = executor.submit(new Callable() {
                @Override
                public String call() throws Exception {
                    log.info("started scanning.....");
                    Process process;
                    process = builder.start();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    process.waitFor();
                    scanResponse.setResultReader(bufferedReader);
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
            log.error("InterruptedException while running bundle audit", ie);
        } catch (ExecutionException ee) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running bundle audit", ee);
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

