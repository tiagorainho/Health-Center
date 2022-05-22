package HCP.Monitors.Halls;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.Monitors.MPriorityQueue;
import HCP.entities.Patient;
import HCP.entities.Halls.ETH;
import HCP.enums.AgeGroup;
import HCP.interfaces.INotification;

public class METH extends ETH {

    private final ReentrantLock rl;
    private final INotification<Object> notification;
    private final MPriorityQueue<Condition> requestPriority;

    public METH(int numberOfSeats, INotification<Object> notification) {
        super(numberOfSeats);
        this.rl = new ReentrantLock();
        this.notification = notification;
        this.requestPriority = new MPriorityQueue<Condition>(Integer.MAX_VALUE);
        this.checkIfCanContinue();
    }

    private void checkIfCanContinue() {
        new Thread(() -> {
            while(true) {
                Condition cond = this.requestPriority.pop();
                this.notification.notifyEvent("RequestEVR");
                this.notification.awaitEvent("EVR");
                try {
                    this.rl.lock();
                    cond.signal();
                } catch ( Exception ex ) {}
                finally {
                    this.rl.unlock();
                }
            }
        }).start();
        
    }

    @Override
    public void process(Patient patient) {
        super.process(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        System.out.println("Leaves Meth 1: " + patient);
        Patient p = null;
        Condition cond = this.rl.newCondition();
        try {
            while(p == null) {
                this.rl.lock();
                this.requestPriority.put(cond, patient.getETN());
                cond.await();
                p = super.leaves(patient);
            }
            
        } catch( InterruptedException ex ) {}
        finally {
            this.rl.unlock();
        }
        System.out.println("Leaves Meth 2: " + patient);
        return p;
    }


    @Override
    public String toString() {
        return "Entrace Hall Monitor: " + super.toString();
    }
    
}