package HCP.Monitors.Halls;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.Patient;
import HCP.entities.Halls.MDH;
import HCP.enums.AgeGroup;
import HCP.interfaces.INotification;

public class MMDH extends MDH {

    private final ReentrantLock rl;
    private final Condition childVacant;
    private final Condition adultVacant;
    private final INotification<Object> notification;
    private final int maxMDT;

    
    public MMDH(INotification<Object> notification, int maxMDT) {
        super(maxMDT);
        this.rl = new ReentrantLock();
        this.maxMDT=maxMDT;
        this.childVacant = this.rl.newCondition();
        this.adultVacant = this.rl.newCondition();
        this.notification = notification;

        this.MDWguarantee();
    }

    private void MDWguarantee() {
        new Thread(() -> {
            while(true) {
                try {
                    this.rl.lock();
                    List<AgeGroup> agesAvailables = (List<AgeGroup>) super.waitingRoom.AgesAvailable();
                    if(agesAvailables.size() == 0)
                        this.notification.notifyEvent("MDW", new LinkedList<>());
                    else
                        this.notification.notifyEvent("MDW", agesAvailables);
                }
                catch(Exception e) {}
                finally {
                    this.rl.unlock();
                };
                this.notification.awaitEvent("MDR");
            }
        }).start();
    }


    @Override
    public void process(Patient patient) {
        try {
            this.rl.lock();

            while(super.getAvailableMedicalRoom(patient.getAgeGroup()) == null) {
                if(patient.getAgeGroup() == AgeGroup.CHILD)
                    this.childVacant.await();
                else if((patient.getAgeGroup() == AgeGroup.ADULT))
                    this.adultVacant.await();
            }
            super.process(patient);
            this.notification.notifyEvent("MDR");
        } catch ( Exception ex ) {}
        finally {    
            this.rl.unlock();
        }
        super.waitForProcess(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        Patient p = null;
        try {
            this.rl.lock();
            p = super.leaves(patient);
            if(patient.getAgeGroup() == AgeGroup.CHILD)
                this.childVacant.signal();
            else if((patient.getAgeGroup() == AgeGroup.ADULT))
                this.adultVacant.signal();
            
            this.notification.notifyEvent("MDR");
        } catch ( IllegalMonitorStateException ex ) {}
        finally {
            this.rl.unlock();
        }
        try {
            Thread.sleep(new Random().nextInt(maxMDT));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }

}
