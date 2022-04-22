package HCP.Monitors.Halls;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Patient;
import HCP.entities.Halls.WTH;
import HCP.enums.AgeGroup;
import HCP.interfaces.INotification;

public class MWTH extends WTH {

    private final ReentrantLock rl;
    private final Condition newUpdate;
    private final INotification<Object> notification;
    private final Condition requestCall;
    private List<AgeGroup> currentWaitingHallAges;

    public MWTH(int numberOfSeats, INotification<Object> notification) {
        super(numberOfSeats);
        this.notification = notification;
        this.rl = new ReentrantLock();
        this.newUpdate = this.rl.newCondition();
        this.requestCall = this.rl.newCondition();
        this.currentWaitingHallAges = new LinkedList<>();
        updateWaitingListStatus();
    }

    public void updateWaitingListStatus() {
        new Thread(() -> {
            while(true) {
                this.currentWaitingHallAges = (List<AgeGroup>) this.notification.awaitEventWithReturn("MDW");
                try {
                    this.rl.lock();
                    this.newUpdate.signalAll();
                    System.out.println("MWTH recebeu " + this.currentWaitingHallAges.toString());
                } catch ( Exception ex ) {}
                finally {
                    this.rl.unlock();
                }
            }
        }).start();
    }

    @Override
    public void enters(Patient patient) {
        super.enters(patient);
    }

    @Override
    public void process(Patient patient) {
        super.process(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        Patient p = null;
        boolean waitingHallAvailable = false;
        do {
            try {
                this.rl.lock();
                waitingHallAvailable = this.currentWaitingHallAges.contains(patient.getAgeGroup());
                if(waitingHallAvailable) {
                    p = super.leaves(patient);
                }
                else {
                    this.newUpdate.await();
                }
            } catch ( Exception ex ) {}
            finally {
                this.rl.unlock();
            }
        } while(p == null);
        this.notification.notifyAllEvents("WTR");
        return p;
    }

    @Override
    public String toString() {
        return "Waiting Hall Monitor: " + super.toString();
    }

    

}
