package com.ui.error;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
class ErrorDto {

    ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    private LocalDateTime timestamp;
    private int status;
    private String message;
}
