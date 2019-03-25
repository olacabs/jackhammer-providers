package com.olacabs.jch.services.exakat.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.exakat.common.Constants;
import com.olacabs.jch.services.exakat.common.ExceptionMessages;

import com.olacabs.jch.services.exakat.models.ParsedFinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        try {
            HashMap<String, String> resultMap = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), HashMap.class);
            List<Finding> findingList = new ArrayList<Finding>();
            for (Map.Entry<String, String> resultEntry : resultMap.entrySet()) {
                String fileResults = resultEntry.getValue();
                HashMap<String, String> fileMapValue = new ObjectMapper().readValue(fileResults, HashMap.class);
                String messages = fileMapValue.get("messages");
                HashMap<String, String> messageValue = new ObjectMapper().readValue(messages, HashMap.class);
                for (Map.Entry<String, String> messageEntry : messageValue.entrySet()) {
                    String lineNo = messageEntry.getKey();
                    List<List<Map<String, String>>> lineFindings = new ObjectMapper().readValue(messageEntry.getValue(), List.class);
                    if (lineFindings.size() > 0 && lineFindings.get(0) != null) {
                        buildFindingRecord(findingList, lineNo, lineFindings.get(0), resultEntry.getKey());
                    }
                }
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

    private void buildFindingRecord(List<Finding> findingList, String lineNo, List<Map<String, String>> parsedFinding, String fileName) {

        for (Map<String, String> eachFinding : parsedFinding) {
            Finding finding = new Finding();
            finding.setDescription(eachFinding.get("source"));
            finding.setTitle(eachFinding.get("message"));
            finding.setSeverity(getSeverity(eachFinding.get("message")));
            finding.setLineNumber(lineNo);
            finding.setFileName(fileName);
            finding.setToolName(Constants.PROVIDER_NAME);
            finding.setFingerprint(getFingerPrint(finding));
            findingList.add(finding);
        }
    }

    private String getSeverity(String severity) {
        if (StringUtils.equalsAnyIgnoreCase(severity, "Major")) {
            return "Critical";
        }
        if (StringUtils.equalsAnyIgnoreCase(severity, "warning")) {
            return "High";
        }
        return severity;
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getDescription());
        fingerPrint.append(finding.getSeverity());
        fingerPrint.append(finding.getFileName());
        fingerPrint.append(finding.getLineNumber());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
