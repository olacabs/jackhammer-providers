package com.olacabs.jch.services.retirejs.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vulnerabilitiy {
    private List<String> info;
    private String severity;
    private Identifier identifiers;
    private String below;
    private String atOrAbove;
}
