package HCP.Monitors.Halls;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.Monitors.MPriorityQueue;
import HCP.entities.Patient;
import HCP.entities.Halls.ETH;
import HCP.interfaces.INotification;

public class METH extends ETH {

    private final ReentrantLock rl;
    private final Condition newCall;
    private final Condition requestCall;
    private int headETN;
    private final INotification<Object> notification;
    private final MPriorityQueue<Integer> requestPriority;

    public METH(int numberOfSeats, INotification<Object> notification) {
        super(numberOfSeats);
        this.rl = new ReentrantLock();
        this.newCall = this.rl.newCondition();
        this.requestCall = this.rl.newCondition();
        this.notification = notification;
        this.requestPriority = new MPriorityQueue<Integer>(Integer.MAX_VALUE);
        this.checkIfCanContinue();
    }

    private void checkIfCanContinue() {
        new Thread(() -> {
            while(true) {
                try {
                    this.rl.lock();
                    this.requestCall.await();                    
                } catch ( Exception ex ) {}
                finally {
                    this.rl.unlock();
                }
                this.notification.notifyEvent("RequestEVR");
                this.notification.awaitEvent("EVR");
                this.call();
            }
        }).start();
        
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
            this.requestPriority.put(patient.getETN(), patient.getETN());
            while(this.headETN != patient.getETN()) {
                this.requestCall.signal();
                this.newCall.await();
            }
            p = super.leaves(patient);
        } catch( InterruptedException ex ) {}
        finally {
            this.rl.unlock();
        }
        return p;
    }

    public void call() {
        try {
            this.rl.lock();
            Patient children = this.childRoom.getHead();
            Patient adult = this.adultRoom.getHead();
            
            this.headETN = Integer.min((children == null)? Integer.MAX_VALUE:children.getETN(), (adult==null)? Integer.MAX_VALUE: adult.getETN());
            
            this.newCall.signalAll();
        } catch ( Exception ex ) {}
        finally {
            this.rl.unlock();
        }
    }


    @Override
    public String toString() {
        return "Entrace Hall Monitor: " + super.toString();
    }
    
}