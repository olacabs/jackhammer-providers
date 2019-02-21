package com.olacabs.jch.services.nsp.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.nsp.common.Constants;
import com.olacabs.jch.services.nsp.common.ExceptionMessages;

import com.olacabs.jch.services.nsp.models.ParsedFinding;
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
            ParsedFinding[] scanResults = mapper.readValue(new FileReader(scanResponse.getResultFile().getAbsoluteFile()), ParsedFinding[].class);
            for (ParsedFinding parsedFinding : scanResults) {
                Finding finding = buildFindingRecord(parsedFinding);
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
                log.error("Exception while parsing nsp results", jsonMappingException);
            }
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SCAN_RESULT_FILE_NOT_FOUND);
            log.error("Result file could not find", io);
        } catch (Throwable t) {
            log.error("Exception while parsing nsp results", t);
        }
        return scanResponse;
    }

    public Finding buildFindingRecord(ParsedFinding parsedFinding) {
        Finding finding = new Finding();
        String solution = parsedFinding.getPatched_versions() == null ? "" : Constants.UPGRADE_TO_VERSION + parsedFinding.getPatched_versions();
        String vulnerableVersions = parsedFinding.getVulnerable_versions() == null ? "" : parsedFinding.getVulnerable_versions();
        String fileName = parsedFinding.getModule() == null ? "" : parsedFinding.getModule() + Constants.FILE_CONNECTOR + vulnerableVersions;
        finding.setTitle(parsedFinding.getTitle());
        finding.setDescription(parsedFinding.getOverview());
        finding.setSeverity(Constants.MEDIUM);
        finding.setCvssScore(parsedFinding.getCvss_score());
        finding.setFileName(parsedFinding.getModule() + Constants.FILE_CONNECTOR + parsedFinding.getVulnerable_versions());
        finding.setExternalLink(parsedFinding.getAdvisory());
        finding.setSolution(solution);
        finding.setFileName(fileName);
        finding.setToolName(Constants.PROVIDER_NAME);
        finding.setFingerprint(getFingerPrint(finding));
        return finding;

    }

    private String getFingerPrint(Finding finding) {
        StringBuilder fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getDescription());
        fingerPrint.append(finding.getCvssScore());
        fingerPrint.append(finding.getExternalLink());
        fingerPrint.append(finding.getSeverity());
        fingerPrint.append(finding.getFileName());
        fingerPrint.append(finding.getSolution());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
