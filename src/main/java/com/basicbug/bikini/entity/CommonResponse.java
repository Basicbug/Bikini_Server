package com.basicbug.bikini.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {

    private T content;
}
