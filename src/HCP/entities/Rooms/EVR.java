package HCP.entities.Rooms;

import java.util.List;

import HCP.Monitors.MSeatRow;
import HCP.Threads.TNurse;
import HCP.entities.Nurse;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

public class EVR implements IRoom, IPlace {

    private final TNurse nurse;
    private final MSeatRow seat;
    private boolean inUse;
    private int ID;

    public EVR(int ID, AgeGroup ageGroup, int x, int y, int maxEVT) {
        this.nurse = new TNurse(new Nurse(),maxEVT);
        this.nurse.start();
        this.seat = new MSeatRow(1,ageGroup,x,y);
        this.ID=ID;
        this.inUse = false;
    }

    @Override
    public void enters(Patient patient) {
        this.inUse = true;
        this.seat.seat(patient, 0);
        SingletonLogger.getInstance().logPatient(patient, "EVH", this.ID);
    }

    @Override
    public Patient leaves(Patient patient) {
        Patient p = this.seat.move();
        this.inUse = false;
        return p;
    }

    public boolean containsPatient(Patient patient) {
        List<Patient> patients = this.seat.getOrderedPatients();
        return patients.contains(patient);
    }

    public boolean isAvailable() {
        return !this.inUse;
    }

    @Override
    public void process(Patient patient) {
        this.nurse.evaluate(patient);
        this.seat.updateGUI();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SingletonLogger.getInstance().logPatient(patient,"EVH",this.ID);
    }
    
}
