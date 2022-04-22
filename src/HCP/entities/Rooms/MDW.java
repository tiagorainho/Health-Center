package HCP.entities.Rooms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import HCP.Monitors.MSeatRow;
import HCP.entities.Patient;
import HCP.enums.AgeGroup;
import HCP.interfaces.IPlace;
import HCP.interfaces.IRoom;

public class MDW implements IRoom, IPlace {

    private final MSeatRow adultSeat;
    private final MSeatRow childSeat;

    public MDW(AgeGroup ageGroup, int x, int y) {
        this.adultSeat = new MSeatRow(1,AgeGroup.ADULT, x, y+75);
        this.childSeat = new MSeatRow(1,AgeGroup.CHILD, x, y);
    }

    public List<AgeGroup> AgesAvailable() {
        Set<AgeGroup> set = new HashSet<AgeGroup>();
        if(this.adultSeat.availableSeats() > 0)
            set.add(AgeGroup.ADULT);
        
        if(this.childSeat.availableSeats() > 0)
            set.add(AgeGroup.CHILD);

        List<AgeGroup> list = new LinkedList<AgeGroup>(set);
        return list;
    }

    private MSeatRow getRoomByAge(AgeGroup ageGroup) {
        switch(ageGroup) {
            case ADULT:
                return this.adultSeat;
            case CHILD:
                return this.childSeat;
            default:
                break;
        }
        return null;
    }

    @Override
    public void enters(Patient patient) {
        this.getRoomByAge(patient.getAgeGroup()).seat(patient, 0);
    }

    @Override
    public Patient leaves(Patient patient) {
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this.getRoomByAge(patient.getAgeGroup()).move();
    }

    @Override
    public void process(Patient patient) {
        // TODO Auto-generated method stub
        
    }
    
}
