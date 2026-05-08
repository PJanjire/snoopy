package com.snoopy.dto;

public class MailResponse {

    private boolean status;
    private String message;

    public MailResponse() {
    }

    public MailResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}