package qchar.arfind.excel;

public class UnknownExcelExtensionException extends RuntimeException {
    private String filename;

    public UnknownExcelExtensionException(String filename) {
        this.filename = filename;
    }

    public UnknownExcelExtensionException(String message, String filename) {
        super(message);
        this.filename = filename;
    }

    public UnknownExcelExtensionException(String message, Throwable cause, String filename) {
        super(message, cause);
        this.filename = filename;
    }

    public UnknownExcelExtensionException(Throwable cause, String filename) {
        super(cause);
        this.filename = filename;
    }

    public UnknownExcelExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String filename) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.filename = filename;
    }
}
