package myRetail.productpricing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {

    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorMessage(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
