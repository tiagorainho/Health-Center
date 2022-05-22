package HCP.Monitors;

import static HCP.entities.HCP_GUI.jPanel1;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import HCP.entities.GuiBox;
import HCP.entities.Patient;
import HCP.enums.AgeGroup;

public class MWaiting extends MPriorityQueue<Patient>{
    private ReentrantLock rl,GUIrl;
    public GuiBox[] boxesGUI;
    final int maxNumBoxes=8;
    
    public MWaiting(int x, int y) {
        super(Integer.MAX_VALUE);
        this.boxesGUI = new GuiBox[maxNumBoxes];
        this.rl=new ReentrantLock();
        this.GUIrl=new ReentrantLock();
        for(int i=0; i<maxNumBoxes; i++){
            boxesGUI[i]=new GuiBox(AgeGroup.ANY, x,y+22*i, false);
            boxesGUI[i].setVisible(false);
            jPanel1.add(boxesGUI[i]);
        }
    }

    public void seat(Patient patient, int priority) {
        this.put(patient, priority);
        updateGUI();
    }

    public Patient move(int id) {
        try {
            this.rl.lock();
            List<Patient> patients = this.getOrderedPatients();
            for(int i=0;i<patients.size();i++){
                if(patients.get(i).getID()==id){
                    this.remove(i);
                }
            }
        } catch ( Exception ex ) {}
        finally {
            this.rl.unlock();
        }  
        updateGUI();
        return null;
    }

    private void updateGUI() {
        try {
            this.GUIrl.lock();
            List<Patient> patients = this.getOrderedPatients();
            int i=0;
            int maxIt=Math.min(patients.size(),maxNumBoxes);
            while(i<maxIt){
                Patient p = patients.get(i);
                boxesGUI[i].updateBox(p.getID(), p.getDoS(), p.getAgeGroup());
                boxesGUI[i].setVisible(true);
                i++;
            }
            while(i<boxesGUI.length){
                boxesGUI[i].setVisible(false);;
                i++;
            }
            // Thread.sleep(50);
        } catch ( Exception ex ) {}
        finally {
            this.GUIrl.unlock();
        }        
    }

    public List<Patient> getOrderedPatients() {
        return this.getPriorityQueue();
    }

    @Override
    public String toString() {
        return "[Monitor Waiting Room -> " + this.getPriorityQueue() + "]";
    }
}
