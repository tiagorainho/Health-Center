package HCP.interfaces;

import HCP.entities.Patient;

public interface IPlace {

    void enters(Patient patient);

    Patient leaves(Patient patient);
    
}
