package com.eleks.academy.pharmagator.exceptions;


public class UploadExceptions extends RuntimeException {

    private Error error;

    public UploadExceptions(UploadExceptions.Error code) {
        super(code.getMessage());
        this.error = code;
    }

    public UploadExceptions(UploadExceptions.Error code, String message) {
        super(code.getMessage() + "\n " + message);
        this.error = code;
    }

    public Error getError() {
        return error;
    }

    public enum Error {

        INVALID_FILE_FORMAT("Format of file is invalid"),
        SAVE_WAS_NOT_SUCCESSFUL("Something was bad while saving");

        private String message;

        Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

}
