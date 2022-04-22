package HCP.interfaces;

public interface INotification<E> {

    void notifyAllEvents(String event);
    void notifyEvent(String event);

    void awaitEvent(String event);


    void notifyAllEvents(String event, E payload);
    void notifyEvent(String event, E payload);
    E awaitEventWithReturn(String event);

}
