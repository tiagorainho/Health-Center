package HCP.entities.Rooms;

import HCP.Monitors.MSeatRow;
import HCP.Threads.TDoctor;
import HCP.entities.Doctor;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

public class MDR implements IRoom, IPlace {

    private final MSeatRow seat;
    private final TDoctor doctor;
    private Patient currentPatient;
    private int ID;

    public MDR(int ID, AgeGroup ageGroup, int x, int y, int maxMDT) {
        this.seat = new MSeatRow(1,ageGroup,x,y);
        this.currentPatient = null;
        this.doctor = new TDoctor(new Doctor(),maxMDT);
        this.doctor.start();
        this.ID=ID;
        
    }

    public Patient getCurrentPatient() {
        return this.currentPatient;
    }

    public boolean isAvailable() {
        return this.currentPatient == null;
    }

    @Override
    public void enters(Patient patient) {
        this.currentPatient = patient;
        this.seat.seat(patient, 0);
        SingletonLogger.getInstance().logPatient(patient,"MDH",this.ID);
    }

    @Override
    public Patient leaves(Patient patient) {
        this.currentPatient = null;
        return patient;
    }

    @Override
    public void process(Patient patient) {
        this.doctor.treatPatient(patient);
        this.seat.move();
        
    }
    
}
