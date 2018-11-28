package com.olacabs.jch.providers.brakeman.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.olacabs.jch.providers.brakeman.common.Constants;
import com.olacabs.jch.providers.brakeman.common.ExceptionMessages;
import com.olacabs.jch.sdk.models.ScanRequest;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ScanSpi;
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
        builder.command(Constants.BRAKEMAN_CMD, "-A","-q","-p",scanRequest.getTarget(), "-o",tempFile.getAbsolutePath());
        builder.redirectError(new File(envs.get(Constants.SCAN_ERROR_LOG_PATH)));
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
            scanResponse.setStatus(Constants.SCAN_FAIL);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("IOException while running brakeman scan",io);
        } catch (InterruptedException ie) {
            scanResponse.setStatus(Constants.SCAN_FAIL);
            scanResponse.setFailedReasons(ExceptionMessages.INTERRUPTED_EXCEPTION);
            log.error("InterruptedException while running brakeman scan",ie);
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
