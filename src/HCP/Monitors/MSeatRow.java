package HCP.Monitors;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static HCP.entities.HCP_GUI.jPanel1;

import HCP.entities.GuiBox;
import HCP.entities.Patient;
import HCP.enums.AgeGroup;
import HCP.interfaces.ISeatRow;

public class MSeatRow extends MPriorityQueue<Patient> implements ISeatRow {

    public GuiBox[] boxesGUI;
    public AgeGroup ageGroup;
    public ReentrantLock GUIrl;

    public MSeatRow(int numberOfSeats, boolean descendent, AgeGroup ageGroup, int x, int y) {
        super(numberOfSeats, descendent);
        this.boxesGUI = new GuiBox[numberOfSeats];
        this.ageGroup=ageGroup;
        this.GUIrl=new ReentrantLock();
        for(int i=0; i<numberOfSeats; i++){
            boxesGUI[i]=new GuiBox(ageGroup, x+(i%2)*50,y+50*(int)(i/2), true);
            jPanel1.add(boxesGUI[i]);
        }
    }

    public MSeatRow(int numberOfSeats, AgeGroup ageGroup, int x, int y) {
        super(numberOfSeats);
        this.boxesGUI = new GuiBox[numberOfSeats];
        this.ageGroup=ageGroup;
        this.GUIrl=new ReentrantLock();
        for(int i=0; i<numberOfSeats; i++){
            boxesGUI[i]=new GuiBox(ageGroup, x+(i%2)*50,y+50*(int)(i/2), true);
            boxesGUI[i].setVisible(true);
            jPanel1.add(boxesGUI[i]);
        }
    }

    public int availableSeats() {
        return super.maxSize - super.queue.size();
    }

    public void seat(Patient patient, double priority) {
        this.put(patient, priority);
        updateGUI();
    }
    
    @Override
    public Patient move() {
        Patient patient = this.pop();
       
        updateGUI();
        return patient;
    }

    @Override
    public List<Patient> getOrderedPatients() {
        return this.getPriorityQueue();
    }

    public void updateGUI(){
        try {
            GUIrl.lock();
            List<Patient> patients = this.getOrderedPatients();
            int i=0;
            for(Patient p:patients){
                boxesGUI[i].updateBox(p.getID(), p.getDoS(), p.getAgeGroup());
                i++;
            }
            while(i<boxesGUI.length){
                boxesGUI[i].empty(this.ageGroup);
                i++;
            }
        } catch ( Exception ex ) {}
        finally {
            GUIrl.unlock();
        }
    }

    @Override
    public String toString() {
        return "[Monitor Seat Row -> " + super.toString() + "]";
    }
}
