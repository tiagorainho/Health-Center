package HCP.interfaces;

import java.util.List;

import HCP.entities.Patient;

public interface ISeatRow {

    void seat(Patient patient, double priority);

    Patient move();

    List<Patient> getOrderedPatients();
    
}
