package com.yumu.yumu_be.art.entity;

public enum Status {

    ON("ON"),
    NOW("NOW"),
    DONE("DONE");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public static String of(Status status) {
        return status.name();
    }
}
