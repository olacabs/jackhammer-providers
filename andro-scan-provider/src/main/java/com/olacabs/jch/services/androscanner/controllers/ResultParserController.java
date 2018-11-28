package com.olacabs.jch.services.androscanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.androscanner.common.ExceptionMessages;
import com.olacabs.jch.services.androscanner.common.Constants;

import com.olacabs.jch.services.androscanner.models.FindingDetail;
import com.olacabs.jch.services.androscanner.models.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        List<Finding> findingList = new ArrayList<Finding>();
        try {
            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            Map<String, FindingDetail> findingDetails = scanResult.getDetails();
            for (Map.Entry<String, FindingDetail> eachFinding : findingDetails.entrySet()) {
                Finding finding = buildDetailFindingRecord(eachFinding);
                findingList.add(finding);
            }
            List<String> sensitiveInfo = scanResult.getSensitive();
            for (String sensitiveData : sensitiveInfo) {
                Finding finding = buildSensitiveFindingRecord(sensitiveData);
                findingList.add(finding);
            }
            scanResponse.setFindings(findingList);
            scanResponse.getResultFile().delete();
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.SCAN_FAIL);
            scanResponse.setFailedReasons(ExceptionMessages.IO_EXCEPTION);
            log.error("Result file could not find", io);
        }
        scanResponse.setResultFile(null);
        return scanResponse;
    }

    private Finding buildSensitiveFindingRecord(String sensitiveData) {
        Finding finding = new Finding();
        finding.setTitle(Constants.FINDING_TITLE);
        finding.setDescription(Constants.FINDING_DESCRIPTION);
        finding.setSeverity(Constants.INFO);
        finding.setCode(sensitiveData);
        finding.setSolution(Constants.FINDING_SOLUTION);
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(finding));
        return finding;
    }

    private Finding buildDetailFindingRecord(Map.Entry<String, FindingDetail> findingDetail) {
        Finding finding = new Finding();
        String severity = findingDetail.getValue().getLevel();
        if (StringUtils.equals(severity, Constants.WARNING)) severity = Constants.HIGH;
        if (StringUtils.equals(severity, Constants.NOTICE)) severity = Constants.LOW;
        finding.setTitle(findingDetail.getKey());
        finding.setDescription(findingDetail.getValue().getTitle());
        finding.setUserInput(findingDetail.getValue().getSummary());
        finding.setCveCode(findingDetail.getValue().getCve_number());
        finding.setSeverity(severity);
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(finding));
        return finding;
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(Constants.PROVIDER_NAME);
        fingerprint.append(finding.getTitle());
        fingerprint.append(finding.getDescription());
        fingerprint.append(finding.getSeverity());
        fingerprint.append(finding.getCode());
        fingerprint.append(finding.getSolution());
        fingerprint.append(finding.getCveCode());
        fingerprint.append(finding.getUserInput());
        return DigestUtils.sha256Hex(fingerprint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

}
