package com.olacabs.jch.services.bundleaudit.controllers;


import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;

import com.olacabs.jch.services.bundleaudit.common.Constants;
import com.olacabs.jch.services.bundleaudit.common.ExceptionMessages;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        BufferedReader resultReader = scanResponse.getResultReader();
        List<Finding> findingList = new ArrayList<Finding>();
        try {
            String line = null;
            while ((line = resultReader.readLine()) != null) {
                if (line.startsWith(Constants.NAME)) {
                    Finding finding = new Finding();
                    String description = line.substring((Constants.NAME).length());
                    description = description + " " + resultReader.readLine().substring(Constants.VERSION.length());
                    String advisory = resultReader.readLine().substring(Constants.ADVISORY.length());
                    String severity = resultReader.readLine().substring(Constants.CRITICALITY.length());
                    if (StringUtils.equals(severity, Constants.UNKNOWN)) severity = Constants.LOW;
                    String url = resultReader.readLine().substring(Constants.URL.length());
                    String title = resultReader.readLine().substring(Constants.TITLE.length());
                    String solution = resultReader.readLine().substring(Constants.SOLUTION.length());
                    String fingerPrint = getFingerPrint(url,title,solution,description,severity,advisory);
                    //build finding record
                    finding.setTitle(title);
                    finding.setDescription(description);
                    finding.setSeverity(severity);
                    finding.setSolution(solution);
                    finding.setAdvisory(advisory);
                    finding.setExternalLink(url);
                    finding.setToolName(Constants.PROVIDER_NAME);
                    finding.setFingerprint(fingerPrint);
                    //add finding
                    findingList.add(finding);
                }
            }
        } catch (IOException io) {
            log.error("IOException while parsing bundle audit results", io);
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
        }
        scanResponse.setResultReader(null);
        scanResponse.setStatus(Constants.COMPLETED_STATUS);
        scanResponse.setFindings(findingList);
        return scanResponse;
    }

    private String getFingerPrint(String url,String bugType,String solution,String description,String severity,String advisory) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(url);
        fingerprint.append(bugType);
        fingerprint.append(solution);
        fingerprint.append(description);
        fingerprint.append(severity);
        fingerprint.append(advisory);
        fingerprint.append(Constants.PROVIDER_NAME);
        return DigestUtils.sha256Hex(fingerprint.toString());
    }
    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
