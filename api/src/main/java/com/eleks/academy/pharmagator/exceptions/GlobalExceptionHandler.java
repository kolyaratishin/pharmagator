package com.eleks.academy.pharmagator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UploadExceptions.class)
    public void springHandleProductExceptions(HttpServletResponse response, UploadExceptions ex) throws IOException {
        if(ex.getError().equals(UploadExceptions.Error.INVALID_FILE_FORMAT)){
            response.sendError(HttpStatus.BAD_REQUEST.value(),
                    UploadExceptions.Error.INVALID_FILE_FORMAT.getMessage());
        }

        if(ex.getError().equals(UploadExceptions.Error.SAVE_WAS_NOT_SUCCESSFUL)){
            response.sendError(HttpStatus.NO_CONTENT.value(),
                    UploadExceptions.Error.SAVE_WAS_NOT_SUCCESSFUL.getMessage());
        }
    }

}
