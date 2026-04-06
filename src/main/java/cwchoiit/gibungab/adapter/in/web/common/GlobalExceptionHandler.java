package cwchoiit.gibungab.adapter.in.web.common;

import cwchoiit.gibungab.application.exception.BusinessException;
import cwchoiit.gibungab.application.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        HttpStatus status = mapToHttpStatus(e.getErrorCode());
        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(status.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
    }

    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case NOT_FOUND, CATEGORY_NOT_FOUND, EXPENSE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case BAD_REQUEST, INVALID_REFRESH_TOKEN, EXPIRED_REFRESH_TOKEN,
                 DUPLICATE_CATEGORY, INVALID_CATEGORY,
                 OAUTH_TOKEN_EXCHANGE_FAILED, OAUTH_USER_INFO_FAILED -> HttpStatus.BAD_REQUEST;
        };
    }
}
