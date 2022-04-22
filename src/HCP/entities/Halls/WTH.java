package HCP.entities.Halls;

import HCP.Monitors.MWaiting;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.entities.Rooms.WTR;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHall;
import HCP.interfaces.IPlace;

// Waiting Hall
public class WTH implements IHall, IPlace {

    protected final MWaiting waitQueue;
    protected final WTR childRoom;
    protected final WTR adultRoom;
    protected int WTNCounter;

    public WTH(int numberOfSeats) {
        this.waitQueue = new MWaiting(760, 100);
        this.childRoom = new WTR(numberOfSeats/2,1,AgeGroup.CHILD,870,100);
        this.adultRoom = new WTR(numberOfSeats/2,2,AgeGroup.ADULT,1000,100);
        this.WTNCounter = 1;
    }

    protected WTR getPatientRoom(Patient patient) {
        if(patient.getAgeGroup() == AgeGroup.ADULT)
            return this.adultRoom;
        else if(patient.getAgeGroup() == AgeGroup.CHILD)
            return this.childRoom;
        return null;
    }

    @Override
    public void enters(Patient patient) {
        patient.setWTN(this.WTNCounter++);
        this.waitQueue.seat(patient, patient.getWTN());
        SingletonLogger.getInstance().logPatient(patient,"WTH",0);
    }

    @Override
    public void process(Patient patient) {
        this.getPatientRoom(patient).enters(patient);
        this.waitQueue.move(patient.getID());        
    }

    @Override
    public Patient leaves(Patient patient) {
        WTR room = this.getPatientRoom(patient);
        if(room.getHead() == patient)
            return room.leaves(patient);
        return null;
    }

    @Override
    public String toString() {
        return "WTNCounter: " + this.WTNCounter + ", child Room: " + this.childRoom.toString() + ", adult Room: " + this.adultRoom.toString();
    }

    
}
