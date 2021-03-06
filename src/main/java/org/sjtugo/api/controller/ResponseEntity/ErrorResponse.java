package org.sjtugo.api.controller.ResponseEntity;

import lombok.Data;


@Data
public class ErrorResponse {
    private int code;
    private String message;

    public ErrorResponse (int code, String message){
        this.code = code;
        this.message = message;
    }

    public ErrorResponse (Exception e) {
        this.code =500;
        this.message = e.getMessage();
    }

}