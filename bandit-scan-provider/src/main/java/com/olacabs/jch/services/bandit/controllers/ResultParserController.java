package com.olacabs.jch.services.bandit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;

import com.olacabs.jch.services.bandit.models.ParsedFinding;
import com.olacabs.jch.services.bandit.common.Constants;
import com.olacabs.jch.services.bandit.common.ExceptionMessages;
import com.olacabs.jch.services.bandit.models.ScanResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        try {
            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            List<ParsedFinding> parsedFindingList = scanResult.getResults();
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

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

    private Finding buildFindingRecord(ParsedFinding parsedFinding) {
        String fileName = parsedFinding.getFilename();
        if (fileName!=null && fileName.contains(Constants.TEMP_COMMON_PREFIX)) {
            String[] filePath = fileName.split(Constants.FILE_SEPARATOR);
            String[] relativePath =  Arrays.copyOfRange(filePath, 3, filePath.length);
            fileName = String.join(Constants.FILE_SEPARATOR,relativePath);
            parsedFinding.setFilename(fileName);
        }
        Finding finding = new Finding();
        finding.setDescription(parsedFinding.getIssue_text());
        finding.setTitle(parsedFinding.getTest_name());
        finding.setExternalLink(parsedFinding.getMore_info());
        finding.setSeverity(parsedFinding.getIssue_severity());
        finding.setCode(parsedFinding.getCode());
        finding.setFileName(parsedFinding.getFilename());
        finding.setLineNumber(parsedFinding.getLine_number());
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(parsedFinding));
        return finding;
    }

    private String getFingerPrint(ParsedFinding parsedFinding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(parsedFinding.getIssue_text());
        fingerprint.append(parsedFinding.getTest_name());
        fingerprint.append(Constants.PROVIDER_NAME);
        fingerprint.append(parsedFinding.getMore_info());
        fingerprint.append(parsedFinding.getIssue_severity());
        fingerprint.append(parsedFinding.getCode());
        fingerprint.append(parsedFinding.getFilename());
        fingerprint.append(parsedFinding.getLine_number());
        return DigestUtils.sha256Hex(fingerprint.toString());
    }
}
