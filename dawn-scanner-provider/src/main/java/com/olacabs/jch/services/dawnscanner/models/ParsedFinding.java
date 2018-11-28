package com.olacabs.jch.services.dawnscanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    private String name;
    private String cve_link;
    private String severity;
    private String cvss_score;
    private String message;
    private String remediation;
}
