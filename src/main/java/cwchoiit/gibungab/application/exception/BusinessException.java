package cwchoiit.gibungab.application.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(ErrorCode.BAD_REQUEST, message);
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(ErrorCode.NOT_FOUND, message);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(ErrorCode.FORBIDDEN, message);
    }

    public static BusinessException of(ErrorCode errorCode, String message) {
        return new BusinessException(errorCode, message);
    }
}
