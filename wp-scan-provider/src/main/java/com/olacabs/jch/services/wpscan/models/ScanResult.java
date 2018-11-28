package com.olacabs.jch.services.wpscan.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
    private String status;
    private String message;
    private List<ParsedFinding> findings;
}
