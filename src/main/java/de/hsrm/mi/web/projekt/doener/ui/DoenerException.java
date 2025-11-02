package de.hsrm.mi.web.projekt.doener.ui;

public class DoenerException extends RuntimeException{

    public DoenerException(String message) {
        super(message);
    }

    public DoenerException(String message, Throwable cause) {
        super(message, cause);
    }
}
