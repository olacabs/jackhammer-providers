package com.olacabs.jch.services.exakat.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFinding {
    private String title;
    private String description;
    private String severity;
    private String externalLink;
    private String solution;
}
