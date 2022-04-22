package HCP.Threads;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Nurse;
import HCP.entities.Patient;

public class TNurse extends Thread {

    private final Nurse nurse;
    private final ReentrantLock rl;
    private final Condition newPatient;
    private final Condition patientEvaluated;
    private Patient currentPatient;
    private final int maxEVT;
    
    public TNurse(Nurse nurse, int maxEVT) {
        this.nurse = nurse;
        this.maxEVT=maxEVT;
        this.rl = new ReentrantLock();
        this.newPatient = this.rl.newCondition();
        this.patientEvaluated = this.rl.newCondition();
    }

    public void evaluate(Patient patient) {
        try {
            this.rl.lock();
            this.currentPatient = patient;
            this.newPatient.signal();

            this.patientEvaluated.await();
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
                
                Thread.sleep(new Random().nextInt(maxEVT));
                this.currentPatient.setDoS(this.nurse.evaluate(this.currentPatient));

                this.patientEvaluated.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
            }
        }

    }

}
