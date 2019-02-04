package com.olacabs.jch.services.dawnscanner.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;

import com.olacabs.jch.services.dawnscanner.models.ParsedFinding;
import com.olacabs.jch.services.dawnscanner.common.Constants;
import com.olacabs.jch.services.dawnscanner.common.ExceptionMessages;
import com.olacabs.jch.services.dawnscanner.models.ScanResult;

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
            List<ParsedFinding> parsedFindingList = scanResult.getVulnerabilities();
            List<Finding> findingList = new ArrayList<Finding>();
            for (ParsedFinding eachFinding : parsedFindingList) {
                Finding finding = buildFindingRecord(eachFinding);
                findingList.add(finding);
            }
            scanResponse.setFindings(findingList);
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
        } catch (JsonMappingException jsonMappingException) {
            if (StringUtils.equals(jsonMappingException.getMessage(), ExceptionMessages.NO_CONTENT_MESSAGE)) {
                scanResponse.setStatus(Constants.COMPLETED_STATUS);
            } else {
                scanResponse.setStatus(Constants.FAILED_STATUS);
                scanResponse.setFailedReasons(ExceptionMessages.PARSING_EXCEPTION);
                log.error("Exception while parsing retire results", jsonMappingException);
            }
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SCAN_RESULT_FILE_NOT_FOUND);
            log.error("Result file could not find", io);
        }
        return scanResponse;
    }

    private Finding buildFindingRecord(ParsedFinding parsedFinding) {
        Finding finding = new Finding();
        finding.setDescription(parsedFinding.getMessage());
        finding.setTitle(parsedFinding.getName());
        finding.setExternalLink(parsedFinding.getCve_link());
        finding.setSeverity(parsedFinding.getSeverity());
        finding.setSolution(parsedFinding.getRemediation());
        finding.setCvssScore(parsedFinding.getCvss_score());
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(parsedFinding));
        return finding;
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

    private String getFingerPrint(ParsedFinding parsedFinding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(parsedFinding.getName());
        fingerprint.append(parsedFinding.getMessage());
        fingerprint.append(Constants.PROVIDER_NAME);
        fingerprint.append(parsedFinding.getSeverity());
        fingerprint.append(parsedFinding.getCve_link());
        fingerprint.append(parsedFinding.getRemediation());
        fingerprint.append(parsedFinding.getCvss_score());
        return DigestUtils.sha256Hex(fingerprint.toString());
    }
}
