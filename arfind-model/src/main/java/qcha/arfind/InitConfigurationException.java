package qcha.arfind;

public class InitConfigurationException extends RuntimeException {

    public InitConfigurationException() {
    }

    public InitConfigurationException(String message) {
        super(message);
    }

    public InitConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitConfigurationException(Throwable cause) {
        super(cause);
    }

    public InitConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
