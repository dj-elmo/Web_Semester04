package de.hsrm.mi.web.projekt.messaging;

public class FrontendNachrichtEvent {

    public enum EventTyp {
        DOENER
    }

    public enum Operation {
        CREATE, UPDATE, DELETE, BOOKED, RETURNED
    }

    private EventTyp typ;
    private long id;
    private Operation operation;

    public FrontendNachrichtEvent() {
    }

    public FrontendNachrichtEvent(EventTyp typ, long id, Operation operation) {
        this.typ = typ;
        this.id = id;
        this.operation = operation;
    }

    public EventTyp getTyp() {
        return typ;
    }

    public void setTyp(EventTyp typ) {
        this.typ = typ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}