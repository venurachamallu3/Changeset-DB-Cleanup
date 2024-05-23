package com.changeset.cleanup.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorMessage {

    private LocalDateTime localDateTime;
    private String message;
    private String details;
}
