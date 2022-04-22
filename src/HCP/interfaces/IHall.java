package HCP.interfaces;

import HCP.entities.Patient;

public interface IHall {
    void enters(Patient patient);
    Patient leaves(Patient patient);
    void process(Patient patient);

}
