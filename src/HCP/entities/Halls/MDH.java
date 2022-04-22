package HCP.entities.Halls;

import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.entities.Rooms.MDR;
import HCP.entities.Rooms.MDW;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHall;
import HCP.interfaces.IPlace;

// Medical Hall
public class MDH implements IHall, IPlace {

    protected final MDW waitingRoom;
    protected final MDR[] childMedicalRooms;
    protected final MDR[] adultMedicalRooms;

    public MDH(int maxMDT) {
        this.waitingRoom = new MDW(AgeGroup.ANY,180,360);

        this.childMedicalRooms = new MDR[2];
        this.adultMedicalRooms = new MDR[2];

        for(int i=0;i<this.childMedicalRooms.length;i++)
            this.childMedicalRooms[i] = new MDR(i+1,AgeGroup.CHILD,360+140*i,360, maxMDT);
        
        for(int i=0;i<this.adultMedicalRooms.length;i++)
            this.adultMedicalRooms[i] = new MDR(this.childMedicalRooms.length+i+1,AgeGroup.ADULT,360+140*i,440,maxMDT);
    }

    protected MDR getAvailableMedicalRoom(AgeGroup ageGroup) {

        // chose where to bring him
        MDR[] medicalRooms = this.childMedicalRooms;
        if(ageGroup == AgeGroup.ADULT)
            medicalRooms = this.adultMedicalRooms;

        for(MDR room: medicalRooms)
            if(room.isAvailable())
                return room;

        return null;
    }

    @Override
    public void enters(Patient patient) {
        this.waitingRoom.enters(patient);
        SingletonLogger.getInstance().logPatient(patient,"MDH",0);
    }

    @Override
    public void process(Patient patient) {
        // call patient from the waiting room
        this.waitingRoom.leaves(patient);
        
        MDR medicalRoom = this.getAvailableMedicalRoom(patient.getAgeGroup());
        medicalRoom.enters(patient);
    }

    protected void waitForProcess(Patient patient) {
        MDR[] medicalRooms = this.childMedicalRooms;
        if(patient.getAgeGroup() == AgeGroup.ADULT)
            medicalRooms = this.adultMedicalRooms;

        MDR medicalRoom = null;
        for(int i=0;i<medicalRooms.length;i++) {
            if(medicalRooms[i].getCurrentPatient() == patient) {
                medicalRoom = medicalRooms[i];
            }
        }
        medicalRoom.process(patient);
        medicalRoom.leaves(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        return patient;
    }


}
