package HCP.entities.Rooms;

import java.util.HashMap;
import java.util.Map;

import HCP.Monitors.MSeatRow;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.enums.DoS;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

public class WTR implements IRoom, IPlace {

    private final MSeatRow seats;
    private final Map<DoS, Integer> priorities;
    private final int ID;

    public WTR(int numberOfSeats,int ID, AgeGroup ageGroup, int x, int y) {
        this.seats = new MSeatRow(numberOfSeats, false,ageGroup, x, y);
        this.priorities = new HashMap<>();
        this.priorities.put(DoS.BLUE, 1);
        this.priorities.put(DoS.YELLOW, 2);
        this.priorities.put(DoS.RED, 3);
        this.ID=ID;
    }

    public int availableSeats() {
        return this.seats.availableSeats();
    }

    public Patient getHead() {
        return this.seats.getHead();
    }

    private double computePatientPriority(Patient patient) {
        double priority = this.priorities.get(patient.getDoS()).doubleValue();
        priority += (double) 1/patient.getWTN();
        return priority;
    }

    @Override
    public void enters(Patient patient) {
        this.seats.seat(patient, computePatientPriority(patient));
        SingletonLogger.getInstance().logPatient(patient,"WTH",this.ID);
    }

    @Override
    public Patient leaves(Patient patient) {
        return this.seats.move();
    }

    @Override
    public void process(Patient patient) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String toString() {
        return "[WTR -> " + this.seats.toString() + "]";
    }
    
}
