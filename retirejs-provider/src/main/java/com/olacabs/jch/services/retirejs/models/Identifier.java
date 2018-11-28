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
public class Identifier {
    private String issue;
    private List<String> CVE;
    private String summary;
}
