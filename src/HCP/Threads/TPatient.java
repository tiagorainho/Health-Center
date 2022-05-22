package HCP.Threads;

import HCP.Monitors.Halls.METH;
import HCP.Monitors.Halls.MEVH;
import HCP.Monitors.Halls.MMDH;
import HCP.Monitors.Halls.MPYH;
import HCP.Monitors.Halls.MWTH;
import HCP.entities.Patient;
import HCP.interfaces.IHall;
import HCP.interfaces.IHospital;
import HCP.interfaces.IPatient;

public class TPatient extends Thread implements IPatient {
    private int id;
    private Patient patient;
    private IHospital hospital;

    public TPatient(int id, Patient patient, IHospital hospital) {
        this.id = id;
        this.patient = patient;
        this.hospital = hospital;
    }


    @Override
    public void run() {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_YELLOW = "\u001B[33m";

        IHall[] halls = this.hospital.getHalls();

        String color = "";
        for(IHall hall: halls) {
            if(hall.getClass() == METH.class)
                color = ANSI_RED;
            else if(hall.getClass() == MEVH.class)
                color = ANSI_GREEN;
            else if(hall.getClass() == MWTH.class)
                color = ANSI_BLUE;
            else if(hall.getClass() == MMDH.class)
                color = ANSI_PURPLE;
            else if(hall.getClass() == MPYH.class)
                color = ANSI_YELLOW;

            //System.out.println(color + "Paciente " + this.patient.getID() + " ENTROU no hall " + hall.getClass().toString() + ANSI_RESET);
            // System.out.println("Entrou: " + this.patient.getID());
            hall.enters(this.patient);
            hall.process(this.patient);
            hall.leaves(this.patient);
            // System.out.println("Saiu: " + this.patient.getID());
            //System.out.println(color + "Paciente " + this.patient.getID() + " SAIU do hall " + hall.getClass().toString() + ANSI_RESET);
        }
        
    }

    public int getThreadPatientId() { return this.id; }
    public IPatient getPatient() { return this.patient; }

    
}
