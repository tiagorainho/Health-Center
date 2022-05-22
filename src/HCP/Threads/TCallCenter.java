package HCP.Threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.Monitors.MCallCenter;
import HCP.Monitors.MPriorityQueue;
import HCP.interfaces.INotification;

public class TCallCenter<E> extends Thread implements INotification<E> {
    
    private final ReentrantLock rl;
    private final Condition newNotification;
    private final MCallCenter<E> callCenter;
    private final MPriorityQueue<NotificationPacket<E>> priorityQueue;
    private String status;
    private final Condition CPPUpdateCondition;

    public TCallCenter() {
        this.callCenter = new MCallCenter<E>();
        this.rl = new ReentrantLock();
        this.newNotification = this.rl.newCondition();
        this.priorityQueue = new MPriorityQueue<>(Integer.MAX_VALUE);
        this.status = "RUN";
        this.CPPUpdateCondition = this.rl.newCondition();
    }

    public void updateStatus(String status) {
        this.status = status;
        try {
            this.rl.lock();
            this.CPPUpdateCondition.signal();
        }
        catch(IllegalMonitorStateException e) {}
        finally {
            this.rl.unlock();
        }
    }

    public void healthCheck() {
        switch(this.status) {
            case "SUSPEND":
                try {
                    this.rl.lock();
                    this.CPPUpdateCondition.await();
                }
                catch(Exception e) {}
                finally {
                    this.rl.unlock();
                }
                break;
            case "RUN":
                break;
        }
    }

    public void stateMaintenence() {
        switch(this.status) {
            case "MOVE_ONE":
                this.status = "SUSPEND";
                
        }
    }

    @Override
    public void run() {
        NotificationPacket<E> packet;
        while(true) {
            try {
                this.rl.lock();
                this.healthCheck();

                while(this.priorityQueue.isEmpty())
                    this.newNotification.await();
                
                packet = this.priorityQueue.pop();
                // System.out.println("\n/////////////////"+packet.event+"///////////////////////\n");

                if(packet.type == "signal")
                    if(packet.payload == null)
                        this.callCenter.notifyEvent(packet.event);
                    else
                        this.callCenter.notifyEvent(packet.event, packet.payload);
                else if(packet.type == "signalAll")
                    if(packet.payload == null)
                        this.callCenter.notifyAllEvents(packet.event);
                    else
                        this.callCenter.notifyAllEvents(packet.event, packet.payload);
                this.stateMaintenence();
            } catch ( InterruptedException ex ) {}
            finally {
                this.rl.unlock();
            }
        }
    }

    @Override
    public void notifyEvent(String event) {
        this.notifyEvent(event, null);
    }

    @Override
    public void notifyAllEvents(String event) {
        this.notifyAllEvents(event, null);
    }

    @Override
    public void awaitEvent(String event) {
        this.callCenter.awaitEvent(event);
    }

    private void prepareNotification(String event, String type, E payload) {
        try {
            this.rl.lock();
            NotificationPacket<E> notificationPacket = new NotificationPacket<E>(type, event, payload);
            this.priorityQueue.put(notificationPacket, 0);
            this.newNotification.signal();
        } catch(IllegalMonitorStateException ex) {}
        finally {
            this.rl.unlock();
        }
    }

    @Override
    public void notifyAllEvents(String event, E payload) {
        this.prepareNotification(event, "signalAll", payload);
    }

    @Override
    public void notifyEvent(String event, E payload) {
        this.prepareNotification(event, "signal", payload);
    }

    @Override
    public E awaitEventWithReturn(String event) {
        return this.callCenter.awaitEventWithReturn(event);
    }

    private class NotificationPacket<T> {

        protected final String type;
        protected final String event;
        protected final T payload;

        public NotificationPacket(String type, String event, T payload) {
            this.type = type;
            this.event = event;
            this.payload = payload;
        }

        public NotificationPacket(String type, String event) {
            this.type = type;
            this.event = event;
            this.payload = null;
        }
        
    }

}
