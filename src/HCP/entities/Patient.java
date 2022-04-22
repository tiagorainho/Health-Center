package HCP.entities;

import HCP.enums.AgeGroup;
import HCP.enums.DoS;
import HCP.interfaces.IPatient;
import HCP.interfaces.IPlace;

public class Patient implements IPatient {
    
    private final int id;
    private final AgeGroup ageGroup;
    private Integer ETN;
    private DoS degreeOfSeverity;
    private Integer WTN;
    private Integer PYN;
    private int ttm=0;

    public Patient(int id, AgeGroup ageGroup) {
        this.id = id;
        this.ageGroup = ageGroup;
        this.degreeOfSeverity=DoS.BLACK;
    }

    public void move(IPlace place) {
        System.out.println(this.toString() + " entrou em " + place);
        try {
            Thread.sleep(this.ttm);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getID() { return this.id; }
    public AgeGroup getAgeGroup() { return this.ageGroup; }
    public Integer getETN() { return this.ETN; }
    public Integer getWTN() { return this.WTN; }
    public Integer getPYN() { return this.PYN; }
    public DoS getDoS() { return this.degreeOfSeverity; }

    public void setETN(int ETN) { this.ETN = ETN; }
    public void setWTN(int WTN) { this.WTN = WTN; }
    public void setPYN(int PYN) { this.PYN = PYN; }
    public void setDoS(DoS dos) { this.degreeOfSeverity = dos; }
    public void setTtm(int maxTtm) {this.ttm=maxTtm;}
    
    @Override
    public String toString() {
        String extraInfo = "";
        if(this.ETN != null)
            extraInfo += ", ETN: " + this.ETN;
        if(this.WTN != null)
            extraInfo += ", WTN: " + this.WTN;
        if(this.degreeOfSeverity != null)
            extraInfo += ", DoS: " + this.degreeOfSeverity;
        return "[Person -> ID: " + this.id + ", age: " + this.ageGroup + extraInfo + "]";
    }
}
