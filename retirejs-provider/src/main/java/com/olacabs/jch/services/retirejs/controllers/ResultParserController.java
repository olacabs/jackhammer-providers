package com.olacabs.jch.services.retirejs.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.retirejs.common.Constants;
import com.olacabs.jch.services.retirejs.common.ExceptionMessages;
import com.olacabs.jch.services.retirejs.models.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        log.info("result file {} {}", scanResponse.getResultFile().getAbsoluteFile());
        ObjectMapper mapper = new ObjectMapper();
        List<Finding> findingList = new ArrayList<Finding>();
        try {
            ScanResult scanResult = mapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            for (ParsedFinding parsedFinding : scanResult.getData()) {
                String file = parsedFinding.getFile();
                FindingResult findingResult = parsedFinding.getResults().get(0);
                List<Vulnerabilitiy> vulnerabilitiyList = findingResult.getVulnerabilities();

                for (Vulnerabilitiy vulnerabilitiy : vulnerabilitiyList) {
                    Finding finding = new Finding();
                    Identifier identifier = vulnerabilitiy.getIdentifiers();
                    String cve = "";
                    if(identifier.getCVE()!=null && identifier.getCVE().size() > 0) cve = identifier.getCVE().get(0);
                    finding.setTitle(identifier.getSummary());
                    finding.setCveCode(cve);
                    finding.setExternalLink(StringUtils.join(vulnerabilitiy.getInfo(), Constants.LINK_SEPARATOR));
                    finding.setSeverity(StringUtils.capitalize(vulnerabilitiy.getSeverity()));
                    finding.setFileName(file);
                    finding.setToolName(Constants.PROVIDER_NAME);
                    finding.setFingerprint(getFingerPrint(finding));
                    findingList.add(finding);
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
        } catch (Exception e) {
            log.error("Exception while parsing results", e);
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.PARSING_EXCEPTION);
        }
        return scanResponse;
    }
    private String getFingerPrint(Finding finding) {
        StringBuilder  fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getCveCode());
        fingerPrint.append(finding.getExternalLink());
        fingerPrint.append(finding.getSeverity());
        fingerPrint.append(finding.getFileName());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
