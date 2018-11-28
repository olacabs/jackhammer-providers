package com.olacabs.jch.services.nmap.common;

public class Constants {
    public static final String NOHUP = "nohup";
    public static final String SH = "sh";
    public static final String PROVIDER_NAME = "Nmap";
    public static final String SUCCESS_STATUS = "Success";
    public static final String FAILED_STATUS = "Failed";
    public static final String COMPLETED_STATUS = "Completed";
    public static final String TEMP_FILE_PREFIX = "nmap";
    public static final String TEMP_FILE_SUFFIX = ".xml";
    public static final String NMAP_CMD = "nmap";
    public static final String ARG_OUT_PUT_OPTION = "-oX";

    //fields
    public static final String HIGH = "High";
    public static final String NMAP_PORT = "port";
    public static final String PROTOCOL = "protocol";
    public static final String PORT_ID = "portid";
    public static final String REASON = "reason";
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String SERVICE = "service";


    //pre deploy
    public static final String NMAP_PRE_DEPLOY_SCRIPT_PATH = "NMAP_PRE_DEPLOY_SCRIPT_PATH";
    public static final String NMAP_PRE_DEPLOY_ERROR_LOG_PATH = "NMAP_PRE_DEPLOY_ERROR_LOG_PATH";
    public static final String NMAP_PRE_DEPLOY_OUTPUT_LOG_PATH = "NMAP_PRE_DEPLOY_OUTPUT_LOG_PATH";

    //deploy
    public static final String NMAP_DEPLOY_SCRIPT_PATH = "NMAP_DEPLOY_SCRIPT_PATH";
    public static final String NMAP_DEPLOY_ERROR_LOG_PATH = "NMAP_DEPLOY_ERROR_LOG_PATH";
    public static final String NMAP_DEPLOY_OUTPUT_LOG_PATH = "NMAP_DEPLOY_OUTPUT_LOG_PATH";

    //scan
    public static final String SCAN_ERROR_LOG_PATH = "SCAN_ERROR_LOG_PATH";
    public static final String SCAN_OUT_PUT_LOG_PATH = "SCAN_OUT_PUT_LOG_PATH";
}
