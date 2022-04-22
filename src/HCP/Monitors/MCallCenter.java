package HCP.Monitors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.interfaces.INotification;

public class MCallCenter<E> implements INotification<E> {

    private final ReentrantLock rl;
    private final Map<String, NotificationPacket<E>> conditions;


    public MCallCenter() {
        this.rl = new ReentrantLock();

        this.conditions = new HashMap<>();
        this.registryEvent("EVR");
        this.registryEvent("WTR");
        this.registryEvent("MDW");
        this.registryEvent("MDR");
        this.registryEvent("RequestMDW");
        this.registryEvent("RequestEVR");
    }

    public void registryEvent(String event) {
        if(this.conditions.containsKey(event))
            throw new IllegalArgumentException("The event '" + event + "', already exists");

        this.conditions.put(event, new NotificationPacket<>(this.rl.newCondition()));
    }

    @Override
    public void notifyEvent(String event) {
        try {
            this.rl.lock();
            this.conditions.get(event).condition.signal();
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
    }

    @Override
    public void notifyAllEvents(String event) {
        try {
            this.rl.lock();
            this.conditions.get(event).condition.signalAll();
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
    }

    @Override
    public void awaitEvent(String event) {
        try {
            this.rl.lock();
            this.conditions.get(event).condition.await();
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
    }

    @Override
    public void notifyAllEvents(String event, E payload) {
        try {
            this.rl.lock();
            NotificationPacket<E> packet = this.conditions.get(event);
            packet.updatePayload(payload);
            packet.condition.signalAll();
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
    }

    @Override
    public void notifyEvent(String event, E payload) {
        try {
            this.rl.lock();
            NotificationPacket<E> packet = this.conditions.get(event);
            packet.updatePayload(payload);
            packet.condition.signal();
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
        
    }

    @Override
    public E awaitEventWithReturn(String event) {
        E payload = null;
        try {
            this.rl.lock();
            NotificationPacket<E> packet = this.conditions.get(event);
            packet.condition.await();
            payload = packet.payload;
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
        return payload;
    }

    private class NotificationPacket<T> {

        protected final Condition condition;
        protected T payload;

        public NotificationPacket(Condition condition) {
            this.condition = condition;
        }

        public void updatePayload(T payload) {
            this.payload = payload;
        }
    }

}