package com.olacabs.jch.providers.findsecbugs.controllers;

import com.olacabs.jch.providers.findsecbugs.common.Constants;
import com.olacabs.jch.providers.findsecbugs.common.ExceptionMessages;
import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
            NodeList nodeList = doc.getElementsByTagName(Constants.BUG_INSTANCE);
            for (int node = 0; node < nodeList.getLength(); node++) {
                findingList.add(getFinding(nodeList.item(node)));
            }
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
        } catch (SAXException sae) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SAX_EXCEPTION);
            log.error("SAXException while parsing findSecBugs results",sae);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.RESULT_FILE_NOT_FOUND);
            log.error("IOException while parsing findSecBugs results",io);
        }  catch (ParserConfigurationException pce) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.PARSER_CONFIGURATION_EXCEPTION);
            log.error("ParserConfigurationException while parsing findSecBugs results",pce);
        }
        scanResponse.setFindings(findingList);
        return scanResponse;
    }

    private Finding getFinding(Node node) {

        //XMLReaderDOM domReader = new XMLReaderDOM();
        Finding finding = new Finding();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            finding.setTitle(getTagValue(Constants.SHORT_MESSAGE, element));
            finding.setDescription(getTagValue(Constants.LONG_MESSAGE, element));
            finding.setLocation(getLocation(element));
            finding.setExternalLink(Constants.EXTERNAL_LINK + element.getAttribute(Constants.TYPE));
            finding.setFileName(getFile(element));
            finding.setLineNumber(getLine(element));
            finding.setToolName(Constants.PROVIDER_NAME);
            finding.setSeverity(getSeverity(element));
            finding.setFingerprint(getFingerPrint(finding));
        }
        return finding;
    }

    private String getLocation(Element element) {
        String location = Constants.CLASS;
        Element eElement = getTag(Constants.METHOD_ATTR,element);
        location += eElement.getAttribute(Constants.CLASS_NAME);
        location += Constants.LOCATION_SEPARATOR + Constants.METHOD  + eElement.getAttribute(Constants.NAME_ATTR);
        return location;
    }

    private String getFile(Element element) {
        Element eElement = getTag(Constants.SOURCE_LINE,element);
        return eElement.getAttribute(Constants.SOURCE_PATH);
    }

    private String getLine(Element element) {
        NodeList nodeList  = element.getElementsByTagName(Constants.SOURCE_LINE);
        String lineNumber = "";
        for (int sibling = 0; sibling < nodeList.getLength(); sibling++) {
            Element siblingElement = (Element)  nodeList.item(sibling);
            if(siblingElement.getAttribute(Constants.PRIMARY)!=null) {
                lineNumber = siblingElement.getAttribute(Constants.START);
                break;
            }
        }
        return lineNumber;
    }
    private String getSeverity(Element element) {
        int priority = Integer.parseInt(element.getAttribute(Constants.PRIORITY));
        String severity;
        switch (priority) {
            case 1:
                severity = Constants.CRITICAL;
                break;
            case 2:
                severity = Constants.HIGH;
                break;
            case 3:
                severity = Constants.MEDIUM;
                break;
            case 4:
                severity = Constants.LOW;
                break;
            case 5:
                severity = Constants.INFO;
                break;
            default:
                severity = Constants.LOW;
                break;
        }
        return severity;
    }
    private Element getTag(String tag, Element element) {
        Element eElement = (Element) element.getElementsByTagName(Constants.SOURCE_LINE).item(0);
        return eElement;
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
        fingerPrint.append(finding.getLocation());
        fingerPrint.append(finding.getLineNumber());
        fingerPrint.append(finding.getFileName());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }
    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }

}
