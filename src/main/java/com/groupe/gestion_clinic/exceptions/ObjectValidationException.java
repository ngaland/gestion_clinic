package com.groupe.gestion_clinic.exceptions;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class ObjectValidationException extends RuntimeException{

    private final Set<String> violations;
    private final String source;
}
