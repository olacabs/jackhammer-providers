package com.olacabs.jch.services.retirejs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScanResult {
    private String version;
    private String start;
    private String time;
    private List<ParsedFinding> data;
    private List<String> messages;
    private List<String> errors;
}
