package com.olacabs.jch.providers.brakeman.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    private String warning_type;
    private String fingerprint;
    private String check_name;
    private String message;
    private String file;
    private String line;
    private String link;
    private String code;
    private List<HashMap<String,String>> render_path;
    private String user_input;
    private String confidence;
    private int warning_code;
    private HashMap<String,String> location;

}
