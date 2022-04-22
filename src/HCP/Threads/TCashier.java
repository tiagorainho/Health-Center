package HCP.Threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Cashier;
import HCP.entities.Patient;

public class TCashier extends Thread {
    
    private final Cashier cashier;
    private final ReentrantLock rl;
    private final Condition newPatient;
    private final Condition patientPayed;
    private Patient currentPatient;

    public TCashier(Cashier cashier) {
        this.cashier = cashier;
        this.rl = new ReentrantLock();
        this.newPatient = this.rl.newCondition();
        this.patientPayed = this.rl.newCondition();
    }

    public boolean isAvailable() {
        return this.cashier.isAvailable();
    }

    public void work(Patient patient) {
        try {
            this.rl.lock();
            this.newPatient.signal();
            this.patientPayed.await();
        } catch ( InterruptedException ex ) {}
        finally {
            this.rl.unlock();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.rl.lock();
                this.newPatient.await();
                this.cashier.providePaymentService(this.currentPatient);
                this.patientPayed.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
            }
        }
    }
    
}
