package com.allgoing.global.payload;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {


    private String message;
    private String code;
    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }
    public ErrorResponse(final ErrorCode code, final String message) {
        this.message = message;
        this.code = code.getCode();

    }
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);

    }
}