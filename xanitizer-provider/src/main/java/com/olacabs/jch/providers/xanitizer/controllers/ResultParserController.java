package com.olacabs.jch.providers.xanitizer.controllers;

import com.olacabs.jch.providers.xanitizer.common.ExceptionMessages;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.olacabs.jch.providers.xanitizer.common.Constants;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ResultParserController implements ResultParserSpi {

    public ScanResponse parseResults(ScanResponse scanResponse) {
        File xmlFile = scanResponse.getResultFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        List<Finding> findingList = new ArrayList<Finding>();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(Constants.XANITIZER_FINDING);
            for (int node = 0; node < nodeList.getLength(); node++) {
                findingList.add(getFinding(nodeList.item(node)));
            }
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
        } catch (SAXException sae) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SAX_EXCEPTION);
            log.error("SAXException while parsing xanitizer results", sae);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.RESULT_FILE_NOT_FOUND);
            log.error("IOException while parsing xanitizer results", io);
        } catch (ParserConfigurationException pce) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.PARSER_CONFIGURATION_EXCEPTION);
            log.error("ParserConfigurationException while parsing xanitizer results", pce);
        }
        scanResponse.setFindings(findingList);
        return scanResponse;
    }

    private Finding getFinding(Node node) {

        //XMLReaderDOM domReader = new XMLReaderDOM();
        Finding finding = new Finding();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            finding.setTitle(getTagValue(Constants.PROBLEM_TYPE, element));
            finding.setDescription(getTagValue(Constants.DESCRIPTION, element));
//            finding.setLocation(getTagValue(Constants.CLASS, element));
            finding.setFileName(getTagValue(Constants.FILE, element));
            finding.setLineNumber(getTagValue(Constants.LINE, element));
            finding.setCvssScore(getTagValue(Constants.CWE_NUMBER,element));
            finding.setToolName(Constants.PROVIDER_NAME);
            finding.setSeverity(getSeverity(element));
            finding.setFingerprint(getFingerPrint(finding));
        }
        return finding;
    }

    private String getSeverity(Element element) {
        Double priority = Double.parseDouble(getTagValue(Constants.RATING,element));
        String severity;
        if (0.0 <= priority && priority <= 1.9) {
            severity = Constants.INFO;
        } else if (2.0 <= priority && priority <= 3.9) {
            severity = Constants.LOW;
        } else if (4.0 <= priority && priority <= 4.9) {
            severity = Constants.MEDIUM;
        } else if (5.0 <= priority && priority <= 7.9) {
            severity = Constants.HIGH;
        } else if (8.0 <= priority && priority <= 10.0) {
            severity = Constants.CRITICAL;
        } else {
            severity = Constants.LOW;
        }
        return severity;
    }

    private String getTagValue(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder  fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getDescription());
        fingerPrint.append(finding.getSeverity());
        fingerPrint.append(finding.getLineNumber());
        fingerPrint.append(finding.getFileName());
        fingerPrint.append(finding.getCvssScore());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
