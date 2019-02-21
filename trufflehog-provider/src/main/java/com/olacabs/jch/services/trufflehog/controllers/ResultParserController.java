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
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader objReader = null;
            try {
                String strCurrentLine;
                objReader = new BufferedReader(new FileReader(scanResponse.getResultFile().getAbsoluteFile()));
                while ((strCurrentLine = objReader.readLine()) != null) {
                    sb.append(strCurrentLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (objReader != null)
                        objReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
//            ScanResult scanResult = objectMapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ScanResult.class);
            ScanResult scanResult = objectMapper.readValue(sb.toString().replace(" +"," ").replace("\t"," "), ScanResult.class);
            for (ParsedFinding parsedFinding : scanResult.getFoundIssues()) {
                log.info("parsed findings reading ...{} {}",parsedFinding.getStringsFound().size());
                Finding finding = buildDetailFindingRecord(parsedFinding);
                findingList.add(finding);
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
        finding.setDescription("Keys Found: " + StringUtils.join(parsedFinding.getStringsFound(), ",").substring(0, 2000));
        finding.setSeverity(severity);
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setLocation("Branch: " + parsedFinding.getBranch() + ",Commit Hash: " + parsedFinding.getCommitHash() + ",Commit Date: " + parsedFinding.getDate());
        finding.setFileName(parsedFinding.getPath());
        finding.setFingerprint(getFingerPrint(parsedFinding));
        return finding;
    }

    private String getFingerPrint(ParsedFinding parsedFinding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(parsedFinding.getBranch());
        fingerprint.append(parsedFinding.getCommitHash());
        fingerprint.append(parsedFinding.getCommit());
        fingerprint.append(parsedFinding.getStringsFound());
        fingerprint.append(parsedFinding.getDate());
        fingerprint.append(parsedFinding.getPath());
        fingerprint.append(Constants.PROVIDER_NAME);
        return DigestUtils.sha256Hex(fingerprint.toString());
    }


    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

}
