package HCP.entities.Rooms;

import java.util.List;

import HCP.Monitors.MSeatRow;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

public class ETR implements IRoom, IPlace {

    private MSeatRow seats;
    private int ID;

    public ETR(int capacity, int ID, AgeGroup ageGroup, int x, int y) {
        this.ID=ID;
        this.seats = new MSeatRow(capacity,ageGroup, x, y);
    }

    public Patient getHead() {
        return this.seats.getHead();
    }

    public boolean containsPatient(Patient patient) {
        List<Patient> patients = this.seats.getOrderedPatients();
        return patients.contains(patient);
    }

    @Override
    public void enters(Patient patient) {
        seats.seat(patient, patient.getETN());
        SingletonLogger.getInstance().logPatient(patient,"ETH",this.ID);
    }

    @Override
    public Patient leaves(Patient patient) {
        return seats.move();
    }

    @Override
    public void process(Patient patient) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String toString() {
        return "[ETR -> " + this.seats.toString() + "]";
    }
    
}
