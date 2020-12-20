package com.basicbug.bikini.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonResponse<T> {

    private T result;
    private HttpStatus status;
}
