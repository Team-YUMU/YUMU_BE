package com.yumu.yumu_be.art.repository.domain;

public enum Status {

    ON("ON"),
    NOW("NOW"),
    DONE("DONE");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
