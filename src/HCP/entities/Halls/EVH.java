package HCP.entities.Halls;

import java.util.HashMap;
import java.util.Map;

import HCP.entities.Patient;
import HCP.entities.Rooms.EVR;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHall;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

// Evaluation Hall
public class EVH implements IHall, IPlace {

    protected EVR[] rooms;
    private final int numberOfRooms = 4;
    private final Map<EVR, Patient> roomToPatient;
    

    public EVH(int maxEVT) {
        this.roomToPatient = new HashMap<>();
        this.rooms = new EVR[numberOfRooms];
        for(int i=0;i<numberOfRooms;i++) {
            this.rooms[i] = new EVR(i+1,AgeGroup.ANY,500+140*(i%2),100+100*((int)i/2), maxEVT);
            this.roomToPatient.put(this.rooms[i], null);
        }
    }

    public IRoom[] getRooms() {
        return this.rooms;
    }

    public EVR getAvailableRoom() {
        for(EVR room: this.rooms) {
            if(room.isAvailable())
                return room;
        }
        return null;
    }

    protected EVR getPatientRoom(Patient patient) {
        for(EVR room: this.roomToPatient.keySet())
            if(patient == this.roomToPatient.get(room))
                return room;
        return null;
    }

    @Override
    public void enters(Patient patient) {
        EVR room = this.getAvailableRoom();
        this.roomToPatient.put(room, patient);
        room.enters(patient);
    }

    @Override
    public void process(Patient patient) {
        EVR room = this.getPatientRoom(patient);
        room.process(patient);
    }

    @Override
    public Patient leaves(Patient patient) {
        EVR room = this.getPatientRoom(patient);
        Patient p = room.leaves(patient);
        this.roomToPatient.remove(room);
        return p;
    }

    
}
