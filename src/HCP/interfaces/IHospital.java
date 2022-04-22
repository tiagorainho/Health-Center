package HCP.interfaces;

import HCP.Threads.TCallCenter;
import HCP.Threads.TPatient;
import HCP.entities.Patient;

public interface IHospital {
    
    TPatient processPatient(Patient patient);

    IHall[] getHalls();

    TCallCenter getCallCenter();

}
