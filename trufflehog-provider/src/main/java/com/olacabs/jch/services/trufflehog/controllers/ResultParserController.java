package com.olacabs.jch.services.trufflehog.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.trufflehog.common.ExceptionMessages;
import com.olacabs.jch.services.trufflehog.common.Constants;

import com.olacabs.jch.services.trufflehog.models.ParsedFinding;
import com.olacabs.jch.services.trufflehog.models.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        log.info("result file size {} {}", scanResponse.getResultFile().getAbsoluteFile().length());
        List<Finding> findingList = new ArrayList<Finding>();

        try {

            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            for (ParsedFinding parsedFinding : scanResult.getFoundIssues()) {
                log.info("parsed findings reading ...{} {}", parsedFinding.getStringsFound().size());
                try {
                    Finding finding = buildDetailFindingRecord(parsedFinding);
                    findingList.add(finding);
                } catch (Exception e) {
                    log.error("Error while building finding obj..{}..{}", e);
                }
            }
            scanResponse.setFindings(findingList);
            scanResponse.getResultFile().delete();
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
        } catch (JsonMappingException jsonMappingException) {
            if (StringUtils.equals(jsonMappingException.getMessage(), ExceptionMessages.NO_CONTENT_MESSAGE)) {
                scanResponse.setStatus(Constants.COMPLETED_STATUS);
            } else {
                scanResponse.setStatus(Constants.FAILED_STATUS);
                scanResponse.setFailedReasons(ExceptionMessages.PARSING_EXCEPTION);
                log.error("Exception while parsing retire results", jsonMappingException);
            }
        } catch (IOException io) {
            scanResponse.setStatus(Constants.SCAN_FAIL);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("Result file could not find", io);
        }
        scanResponse.setResultFile(null);
        return scanResponse;
    }

    private Finding buildDetailFindingRecord(ParsedFinding parsedFinding) {
        Finding finding = new Finding();
        String severity = Constants.HIGH;
        finding.setTitle(parsedFinding.getReason());
        finding.setDescription("Keys Found: " + getFoundStrings(parsedFinding));
        finding.setSeverity(severity);
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setLocation(getLocation(parsedFinding));
        finding.setFileName(parsedFinding.getPath());
        finding.setFingerprint(getFingerPrint(parsedFinding));
        return finding;
    }

    private String getFingerPrint(ParsedFinding parsedFinding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(parsedFinding.getBranch());
        fingerprint.append(parsedFinding.getCommitHash());
        fingerprint.append(parsedFinding.getCommit());
        fingerprint.append(getFoundStrings(parsedFinding));
        fingerprint.append(parsedFinding.getDate());
        fingerprint.append(parsedFinding.getPath());
        fingerprint.append(Constants.PROVIDER_NAME);
        return DigestUtils.sha256Hex(fingerprint.toString());
    }

    public String getLocation(ParsedFinding parsedFinding) {
        StringBuilder stringBuilder = new StringBuilder();
        if (parsedFinding.getBranch() != null) stringBuilder.append("Branch: " + parsedFinding.getBranch());
        if (parsedFinding.getCommitHash() != null) stringBuilder.append(" Commit Hash:" + parsedFinding.getCommitHash());
        if (parsedFinding.getDate() != null) stringBuilder.append(" Commit Date:" + parsedFinding.getDate());
        return stringBuilder.toString();
    }

    public String getFoundStrings(ParsedFinding parsedFinding) {
        if (parsedFinding.getStringsFound() == null || parsedFinding.getStringsFound().size() == 0)
            return StringUtils.EMPTY;
        String stringsFound = StringUtils.join(parsedFinding.getStringsFound(), ",");
        if (stringsFound.length() < 2001)
            return stringsFound;
        return stringsFound.substring(0, 2000);
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

}
