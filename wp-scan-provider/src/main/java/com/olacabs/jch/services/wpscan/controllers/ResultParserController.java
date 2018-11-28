package com.olacabs.jch.services.wpscan.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.wpscan.common.Constants;
import com.olacabs.jch.services.wpscan.common.ExceptionMessages;

import com.olacabs.jch.services.wpscan.models.ParsedFinding;
import com.olacabs.jch.services.wpscan.models.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        try {
            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            List<ParsedFinding> parsedFindingList = scanResult.getFindings();
            List<Finding> findingList = new ArrayList<Finding>();
            for (ParsedFinding eachFinding : parsedFindingList) {
                Finding finding = buildFindingRecord(eachFinding);
                findingList.add(finding);
            }
            scanResponse.setFindings(findingList);
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SCAN_RESULT_FILE_NOT_FOUND);
            log.error("Result file could not find", io);
        }
        return scanResponse;
    }

    private Finding buildFindingRecord(ParsedFinding parsedFinding) {
        Finding finding = new Finding();
        finding.setDescription(parsedFinding.getDescription());
        finding.setTitle(parsedFinding.getTitle());
        finding.setExternalLink(parsedFinding.getExternalLink());
        finding.setSeverity(parsedFinding.getSeverity());
        finding.setSolution(parsedFinding.getSolution());
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(finding));
        return finding;
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder  fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getDescription());
        fingerPrint.append(finding.getSeverity());
        fingerPrint.append(finding.getExternalLink());
        fingerPrint.append(finding.getSolution());
        fingerPrint.append(finding.getSeverity());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
