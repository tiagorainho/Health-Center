package HCP.Threads;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Doctor;
import HCP.entities.Patient;

public class TDoctor extends Thread {
    
    private final Doctor doctor;
    private final ReentrantLock rl;
    private final Condition newPatient;
    private final Condition patientTreated;
    private Patient currentPatient;
    private final int maxMDT;


    public TDoctor(Doctor doctor, int maxMDT) {
        this.doctor = doctor;
        this.maxMDT=maxMDT;
        this.rl = new ReentrantLock();
        this.newPatient = this.rl.newCondition();
        this.patientTreated = this.rl.newCondition();
    }

    public void treatPatient(Patient patient) {
        try {
            this.rl.lock();
            this.currentPatient = patient;
            this.newPatient.signal();
            this.patientTreated.await();
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
                Thread.sleep(new Random().nextInt(maxMDT));
                doctor.treat(this.currentPatient);
                this.patientTreated.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
            }
        }
    }
}
