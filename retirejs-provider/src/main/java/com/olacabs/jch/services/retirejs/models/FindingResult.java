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
public class FindingResult {
    private String version;
    private String component;
    private String detection;
    private List<Vulnerabilitiy> vulnerabilities;
}
