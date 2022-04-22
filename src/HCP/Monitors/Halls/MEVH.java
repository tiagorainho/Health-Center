package HCP.Monitors.Halls;

import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Patient;
import HCP.entities.Halls.EVH;
import HCP.interfaces.INotification;

public class MEVH extends EVH {

    private final ReentrantLock rl;
    private final INotification<Object> notification;

    public MEVH(INotification<Object> notification, int maxEVT) {
        super(maxEVT);
        this.notification = notification;
        this.rl = new ReentrantLock();
        this.AnswearEVR();
    }

    private void AnswearEVR() {
        new Thread(() -> {
            while(true) {
                this.notification.awaitEvent("RequestEVR");
                while( super.getAvailableRoom() == null )
                    this.notification.awaitEvent("WTR");
                this.notification.notifyEvent("EVR");
            }
        }).start();
    }

    @Override
    public void enters(Patient patient) {
        while( super.getAvailableRoom() == null )
            this.notification.awaitEvent("WTR");
        super.enters(patient);
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
            this.notification.notifyEvent("WTR");
        } catch ( IllegalMonitorStateException ex ) {}
        finally {
            this.rl.unlock();
        }
        return p;
    }
    
    
}