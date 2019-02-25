package com.olacabs.jch.services.arachni.controllers;

import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.arachni.common.ExceptionMessages;
import com.olacabs.jch.services.arachni.common.Constants;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
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
            NodeList nodeList = doc.getElementsByTagName(Constants.FINDING_TAG);
            for (int node = 0; node < nodeList.getLength(); node++) {
                findingList.add(getFinding(nodeList.item(node)));
            }
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
        } catch (SAXException sae) {
            if(scanResponse.getResultFile().length() == 0) {
                scanResponse.setStatus(Constants.COMPLETED_STATUS);
                log.error("SAXException while parsing findSecBugs results", sae);
                log.error("resulting file size... {} {}", scanResponse.getResultFile().length());
            } else {
                scanResponse.setStatus(Constants.FAILED_STATUS);
                scanResponse.setFailedReasons(ExceptionMessages.SAX_EXCEPTION);
                log.error("SAXException while parsing findSecBugs results", sae);
                log.error("resulting file size... {} {}", scanResponse.getResultFile().length());
            }
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.RESULT_FILE_NOT_FOUND);
            log.error("IOException while parsing arachni results", io);
        } catch (ParserConfigurationException pce) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.PARSER_CONFIGURATION_EXCEPTION);
            log.error("ParserConfigurationException while parsing arachni results", pce);
        }
        scanResponse.setFindings(findingList);
        scanResponse.setStatus(Constants.COMPLETED_STATUS);
//        scanResponse.getResultFile().delete();
//        scanResponse.getArachniAfrFile().delete();
        return scanResponse;

    }


    private Finding getFinding(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Finding finding = new Finding();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String severity = StringUtils.capitalize(getTagValue(Constants.SEVERITY, element));
            if (StringUtils.equals(severity, Constants.INFORMATIONAL)) severity = Constants.INFO;
            finding.setTitle(getTagValue(Constants.NAME, element));
            finding.setDescription(getTagValue(Constants.DESCRIPTION, element));
            finding.setSolution(getTagValue(Constants.SOLUTION, element));
            finding.setSeverity(severity);
            finding.setCweCode(getTagValue(Constants.CWE, element));
            finding.setExternalLink(getExternalUrl(element));
            finding.setRequest(getTagValue(Constants.REQUEST, element));
            finding.setResponse(getTagValue(Constants.RESPONSE, element));
            finding.setToolName(Constants.PROVIDER_NAME);
            finding.setFingerprint(getFingerPrint(finding));
        }
        return finding;
    }

    private String getExternalUrl(Element element) {
        List<String> urls = new ArrayList<String>();
        NodeList nodeList = element.getElementsByTagName(Constants.REFERENCE);
        for (int node = 0; node < nodeList.getLength(); node++) {
            NamedNodeMap namedNodeMap = nodeList.item(node).getAttributes();
            urls.add(namedNodeMap.getNamedItem(Constants.URL).getNodeValue());
        }
        return StringUtils.join(Constants.COMMA_SEPARATOR, urls);
    }

    private String getTagValue(String tag, Element element) {
        String tagValue = "";
        if (element.getElementsByTagName(tag) != null && element.getElementsByTagName(tag).item(0) !=null) {
            tagValue = element.getElementsByTagName(tag).item(0).getTextContent();
        }
        return tagValue;
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(Constants.PROVIDER_NAME);
        fingerprint.append(finding.getTitle());
        fingerprint.append(finding.getDescription());
        fingerprint.append(finding.getSolution());
        fingerprint.append(finding.getCweCode());
        fingerprint.append(finding.getExternalLink());
        fingerprint.append(finding.getRequest());
        fingerprint.append(finding.getResponse());
        return DigestUtils.sha256Hex(fingerprint.toString());
    }
    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
