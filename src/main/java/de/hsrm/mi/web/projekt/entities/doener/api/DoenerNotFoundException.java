package de.hsrm.mi.web.projekt.entities.doener.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoenerNotFoundException extends RuntimeException {

    public DoenerNotFoundException(long id) {
        super("Kein Döner mit ID " + id + " gefunden.");
    }
}