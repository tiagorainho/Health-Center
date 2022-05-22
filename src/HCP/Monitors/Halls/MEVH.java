package HCP.Monitors.Halls;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Patient;
import HCP.entities.Halls.EVH;
import HCP.entities.Rooms.EVR;
import HCP.interfaces.INotification;

public class MEVH extends EVH {

    private final ReentrantLock rl;
    private final INotification<Object> notification;
    private final Condition roomVacant;

    public MEVH(INotification<Object> notification, int maxEVT) {
        super(maxEVT);
        this.notification = notification;
        this.rl = new ReentrantLock();
        this.roomVacant = this.rl.newCondition();
        this.AnswearEVR();
    }

    private void AnswearEVR() {
        final ReentrantLock trl = new ReentrantLock();
        new Thread(() -> {
            while(true) {
                this.notification.awaitEvent("RequestEVR");
                new Thread(() -> {
                    try {
                        trl.lock();
                        while( this.getAvailableRoom() == null ) {
                            this.notification.awaitEvent("WTR");
                        }
                        this.notification.notifyEvent("EVR");
                    }
                    catch(Exception e) { }
                    finally {
                        trl.unlock();
                    }
                }).start();
            }
        }).start();
    }

    public EVR getAvailableRoom() {
        try {
            this.rl.lock();
            return super.getAvailableRoom();
        }
        catch(Exception e) { }
        finally {
            this.rl.unlock();
        }
        return null;
    }

    @Override
    public void enters(Patient patient) {
        System.out.println("Enters Mevh 1: " + patient);
        try {
            this.rl.lock();
            while( this.getAvailableRoom() == null )
                this.roomVacant.await();
        } catch ( InterruptedException ex ) {}
        finally {
            this.rl.unlock();
        }

        try {
            this.rl.lock();
            super.enters(patient);
        } catch ( IllegalMonitorStateException ex ) {}
        finally {
            this.rl.unlock();
        }
        System.out.println("Enters Mevh 2: " + patient);
    }

    @Override
    public void process(Patient patient) {
        super.process(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        Patient p = null;
        try {
            this.rl.lock();
            p = super.leaves(patient);
            this.roomVacant.signal();
            // this.notification.notifyEvent("WTR");
        } catch ( IllegalMonitorStateException ex ) {}
        finally {
            this.rl.unlock();
        }
        return p;
    }
    
    
}