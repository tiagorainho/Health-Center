package HCP.Monitors.Halls;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Patient;
import HCP.entities.Halls.PYH;

public class MPYH extends PYH {
    
    private final ReentrantLock rl;
    private final Condition cashierReady;
    

    public MPYH(int maxPYT) {
        super(maxPYT);
        this.rl = new ReentrantLock();
        this.cashierReady = this.rl.newCondition();
    }

    @Override
    public void enters(Patient patient) {
        try {
            this.rl.lock();
            super.enters(patient);
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
    }

    @Override
    public void process(Patient patient) {
        try {
            this.rl.lock();
            while(patient.getPYN() != super.nextToPay)
                this.cashierReady.await();

            super.process(patient);
            this.cashierReady.signalAll();
        } catch ( InterruptedException ex ) {}
        finally {    
            this.rl.unlock();
        }
        
    }


}
