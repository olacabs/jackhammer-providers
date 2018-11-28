package com.olacabs.jch.services.nmap.controllers;


import com.olacabs.jch.sdk.models.Finding;
import com.olacabs.jch.sdk.models.ScanResponse;
import com.olacabs.jch.sdk.spi.ResultParserSpi;
import com.olacabs.jch.services.nmap.common.Constants;
import com.olacabs.jch.services.nmap.common.ExceptionMessages;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
            NodeList nodeList = doc.getElementsByTagName(Constants.NMAP_PORT);
            for (int node = 0; node < nodeList.getLength(); node++) {
                findingList.add(getFinding(nodeList.item(node)));
            }
            scanResponse.setFindings(findingList);
            scanResponse.setStatus(Constants.COMPLETED_STATUS);
            scanResponse.getResultFile().delete();
            scanResponse.setResultFile(null);
        } catch (SAXException sae) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.SAX_EXCEPTION);
            log.error("SAXException while parsing namp results", sae);
        } catch (IOException io) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.RESULT_FILE_NOT_FOUND);
            log.error("IOException while parsing nmap results", io);
        } catch (ParserConfigurationException pce) {
            scanResponse.setStatus(Constants.FAILED_STATUS);
            scanResponse.setFailedReasons(ExceptionMessages.PARSER_CONFIGURATION_EXCEPTION);
            log.error("ParserConfigurationException while parsing nmap results", pce);
        }
        return scanResponse;
    }

    private Finding getFinding(Node node) {

        //XMLReaderDOM domReader = new XMLReaderDOM();
        Finding finding = new Finding();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            Element stateElement = getTag(Constants.STATE,element);
            String state = stateElement.getAttribute(Constants.STATE);
            String reason = stateElement.getAttribute(Constants.REASON);

            Element serviceElement = getTag(Constants.SERVICE,element);

            finding.setTitle(serviceElement.getAttribute(Constants.NAME));
            finding.setProtocol(element.getAttribute(Constants.PROTOCOL));
            finding.setPort(element.getAttribute(Constants.PORT_ID));
            finding.setDescription(reason);
            finding.setState(state);
            finding.setToolName(Constants.PROVIDER_NAME);
            finding.setSeverity(Constants.HIGH);
            finding.setFingerprint(getFingerPrint(finding));
        }
        return finding;
    }

    private Element getTag(String tag, Element element) {
        Element eElement = (Element) element.getElementsByTagName(tag).item(0);
        return eElement;
    }

    private String getFingerPrint(Finding finding) {
        StringBuilder  fingerPrint = new StringBuilder();
        fingerPrint.append(Constants.PROVIDER_NAME);
        fingerPrint.append(finding.getTitle());
        fingerPrint.append(finding.getProtocol());
        fingerPrint.append(finding.getPort());
        fingerPrint.append(finding.getDescription());
        fingerPrint.append(finding.getState());
        fingerPrint.append(finding.getSeverity());
        return DigestUtils.sha256Hex(fingerPrint.toString());
    }

    public String getToolName() {
        return Constants.PROVIDER_NAME;
    }
}
