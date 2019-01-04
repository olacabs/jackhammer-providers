package com.olacabs.jch.services.bandit.models;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    private String issue_confidence;
    private String issue_text;
    private String issue_severity;
    private String test_name;
    private String line_number;
    private String filename;
    private String more_info;
    private String code;

    @Ignore
    private String[] line_range;
    @Ignore
    private String test_id;
}
