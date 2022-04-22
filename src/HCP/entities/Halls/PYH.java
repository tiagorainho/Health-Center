package HCP.entities.Halls;

import HCP.Monitors.MSeatRow;
import HCP.Monitors.MWaiting;
import HCP.Threads.TCashier;
import HCP.entities.Cashier;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHall;
import HCP.interfaces.IPlace;

// Payment Hall
public class PYH implements IHall, IPlace {

    private MSeatRow cashierBox;
    private final TCashier cashier;
    private final MWaiting queue;
    private int count;
    protected int nextToPay;

    public PYH(int maxPYT) {
        this.count = 0;
        this.nextToPay = 0;
        this.queue = new MWaiting(750,360);
        this.cashier = new TCashier(new Cashier(maxPYT));
        this.cashier.start();
        this.cashierBox = new MSeatRow(1,AgeGroup.ANY,925,360);
    }

    @Override
    public void enters(Patient patient) {
        patient.setPYN(this.count++);
        this.queue.seat(patient, patient.getPYN());
        SingletonLogger.getInstance().logPatient(patient,"PYH",0);
    }

    @Override
    public void process(Patient patient) {
        cashierBox.seat(patient, patient.getPYN());
        Patient p = this.queue.move(patient.getID());
        cashier.work(p);
        this.nextToPay++;
    }

    protected boolean cashierIsAvailable() {
        return this.cashier.isAvailable();
    }

    @Override
    public Patient leaves(Patient patient) {
        // TODO Auto-generated method stub
        //cashierBox.empty(AgeGroup.ANY
        cashierBox.move();
        SingletonLogger.getInstance().logPatient(patient,"OUT",0);
        return null;
        
    }
}
