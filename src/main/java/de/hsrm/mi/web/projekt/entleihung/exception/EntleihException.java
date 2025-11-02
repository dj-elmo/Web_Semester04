package de.hsrm.mi.web.projekt.entleihung.exception;

public class EntleihException extends RuntimeException {

    public EntleihException(String message) {
        super(message);
    }

    public EntleihException(String message, Throwable cause) {
        super(message, cause);
    }

}
