package org.kvn.DigitalLibrary.enums;

import lombok.Getter;

@Getter
public enum Operator {
    EQUALS("="),
    LESS_THAN("<"),
    LESS_THAN_EQUAL("<="),
    IN("IN"),
    LIKE("LIKE");

    private String value;

    Operator(String value) {
        this.value = value;
    }

}
