package HCP.entities.Halls;

import HCP.Monitors.MWaiting;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.entities.Rooms.ETR;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHall;
import HCP.interfaces.IPlace;

// Entrance Hall
public class ETH implements IHall, IPlace {

    protected final MWaiting waitQueue;
    protected final ETR childRoom;
    protected final ETR adultRoom;
    protected int ETNCounter;
    

    public ETH(int numberOfSeats) {
        if(numberOfSeats % 2 != 0) throw new RuntimeException("Number of seats must be a pair number: " + numberOfSeats);
        
        this.waitQueue = new MWaiting(40,100);
        this.childRoom = new ETR((int)(numberOfSeats/2),1, AgeGroup.CHILD,150,100);
        this.adultRoom = new ETR((int)(numberOfSeats/2),2, AgeGroup.ADULT,340,100);

        this.ETNCounter = 1;
    }

    @Override
    public void enters(Patient patient) {
        patient.setETN(this.ETNCounter++);
        this.waitQueue.seat(patient,patient.getETN());
        SingletonLogger.getInstance().logPatient(patient,"ETH",0);
    }


    public ETR getPatientRoom(Patient patient) {
        switch(patient.getAgeGroup()) {
            case ADULT:
                return this.adultRoom;
            case CHILD:
                return this.childRoom;
            default:
                break;
        }
        return null;
    }


    @Override
    public void process(Patient patient) {
        this.getPatientRoom(patient).enters(patient);
        this.waitQueue.move(patient.getID());
    }

    @Override
    public Patient leaves(Patient patient) {
        int patientETN = patient.getETN();

        Patient children = this.childRoom.getHead();
        Patient adult = this.adultRoom.getHead();

        Patient p = null;
        if(children != null && patientETN == children.getETN())
            p = this.childRoom.leaves(patient);
        else if(adult != null && patientETN == adult.getETN())
            p = this.adultRoom.leaves(patient);
        return p;
    }

    @Override
    public String toString() {
        return "ETNCounter: " + this.ETNCounter + ", child Room: " + this.childRoom.toString() + ", adult Room: " + this.adultRoom.toString();
    }

}
