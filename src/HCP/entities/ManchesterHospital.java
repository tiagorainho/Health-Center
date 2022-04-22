package HCP.entities;

import static HCP.entities.HCP_GUI.jPanel1;

import java.io.IOException;
import java.net.ServerSocket;

import HCP.Comunications.HCPServer;
import HCP.Monitors.Halls.METH;
import HCP.Monitors.Halls.MEVH;
import HCP.Monitors.Halls.MMDH;
import HCP.Monitors.Halls.MPYH;
import HCP.Monitors.Halls.MWTH;
import HCP.Threads.TCallCenter;
import HCP.Threads.TPatient;
import HCP.interfaces.IHall;
import HCP.interfaces.IHospital;
import HCP.interfaces.INotification;
import HCP.interfaces.IPlace;

public class ManchesterHospital implements IHospital, IPlace {

    private IHall[] halls = new IHall[5];
    private final int numberOfSeats;
    private int count;
    public static HCP_GUI hcpGUI = new HCP_GUI();
    private final TCallCenter<Object> CCH;

    public ManchesterHospital(int numberOfSeats, int maxEVT, int maxMDT, int maxPYT) {
        this.count = 0;
        this.numberOfSeats = numberOfSeats;

        hcpGUI.setVisible(true);
        hcpGUI.setTitle("Health Center");
        
        // add call center
        this.CCH = new TCallCenter<Object>();

        // add halls to the hospital
        this.halls[0] = new METH(numberOfSeats, (INotification<Object>) this.CCH);
        this.halls[1] = new MEVH((INotification<Object>) CCH, maxEVT);
        this.halls[2] = new MWTH(numberOfSeats, (INotification<Object>)  this.CCH);
        this.halls[3] = new MMDH((INotification<Object>) this.CCH, maxMDT);
        this.halls[4] = new MPYH(maxPYT);
        jPanel1.revalidate();
        jPanel1.repaint();
        
        // run callcenter
        CCH.start();
    }

    public TCallCenter getCallCenter() {
        return this.CCH;
    }

    @Override
    public TPatient processPatient(Patient patient) {

        TPatient patientThread = new TPatient(count++, patient, this);

        this.enters(patient);
        patientThread.start();
        this.leaves(patient);
        return patientThread;
    }

    public IHall[] getHalls() { return this.halls; };

    public int getNumberOfSeats() { return this.numberOfSeats; }

    @Override
    public void enters(Patient patient) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Patient leaves(Patient patient) {
        // TODO Auto-generated method stub
        return null;
        
    }

    
}
