package com.olacabs.jch.providers.brakeman.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.olacabs.jch.providers.brakeman.common.Constants;
import com.olacabs.jch.providers.brakeman.common.ExceptionMessages;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.providers.brakeman.models.ParsedFinding;
import com.olacabs.jch.providers.brakeman.models.ScanResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        try {
            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            List<ParsedFinding> parsedFindingList = scanResult.getWarnings();
            List<Finding> findingList = new ArrayList<Finding>();
            for (ParsedFinding eachFinding : parsedFindingList) {
                Finding finding = buildFindingRecord(eachFinding);
                findingList.add(finding);
            }
            scanResponse.setFindings(findingList);
            scanResponse.getResultFile().delete();
            scanResponse.setStatus(Constants.SCAN_COMPLETE);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.SCAN_FAIL);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("Result file could not find", io);
        }
        scanResponse.setResultFile(null);
        return scanResponse;
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

    private Finding buildFindingRecord(ParsedFinding parsedFinding) {
        Finding finding = new Finding();
        finding.setTitle(parsedFinding.getWarning_type());
        finding.setFingerprint(getFingerPrint(parsedFinding));
        finding.setDescription(parsedFinding.getMessage());
        finding.setFileName(parsedFinding.getFile());
        finding.setLineNumber(parsedFinding.getLine());
        finding.setExternalLink(parsedFinding.getLink());
        finding.setCode(parsedFinding.getCode());
        finding.setUserInput(parsedFinding.getUser_input());
        if (parsedFinding.getLocation() != null) finding.setLocation(parsedFinding.getLocation().toString());
        finding.setSeverity(getSeverity(parsedFinding.getConfidence()));
        finding.setToolName(Constants.PROVIDER_NAME);
        return finding;
    }

    private String getFingerPrint(ParsedFinding parsedFinding) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parsedFinding.getWarning_type());
        stringBuilder.append(parsedFinding.getLink());
        stringBuilder.append(getSeverity(parsedFinding.getConfidence()));
        stringBuilder.append(Constants.PROVIDER_NAME);
        stringBuilder.append(parsedFinding.getFile());
        stringBuilder.append(parsedFinding.getLine());
        stringBuilder.append(parsedFinding.getCode());
        return DigestUtils.sha256Hex(stringBuilder.toString());
    }

    private String getSeverity(String confidence) {
        return StringUtils.equals(confidence, Constants.WEAK_SEVERITY) ? Constants.INFO_SEVERITY : confidence;
    }
}
