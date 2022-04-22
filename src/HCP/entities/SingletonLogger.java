package HCP.entities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import HCP.enums.AgeGroup;
import HCP.enums.DoS;

public class SingletonLogger {
    private static class LoadSingleton{
        static final SingletonLogger INSTANCE = new SingletonLogger();
    }    

    public static SingletonLogger getInstance(){
        return LoadSingleton.INSTANCE;
    }

    private ReentrantLock rl;
    private String stockMessage;
    private HashMap<String,int[]> messagePosMap;
    private HashMap<AgeGroup,String> ageGroupMap;
    private HashMap<DoS,String> doSMap;
    private FileWriter log;

    private SingletonLogger(){
        this.rl = new ReentrantLock();
        this.stockMessage="    |             |                     |                |                          |     |    ";
        this.messagePosMap=new HashMap<String,int[]>();
        this.ageGroupMap=new HashMap<AgeGroup,String>();
        this.doSMap=new HashMap<DoS,String>();
        String logFileName = "tiago/src/log.txt";
        try {
            this.log = new FileWriter(logFileName);
          } catch (IOException e) {
              System.out.println("Log file Error: " + logFileName);
        }
        messagePosMap.put("ETH", new int[]{6,4});
        messagePosMap.put("EVH", new int[]{15,5});
        messagePosMap.put("WTH", new int[]{42,5});
        messagePosMap.put("MDH", new int[]{59,5});
        messagePosMap.put("PYH", new int[]{86,4});
        messagePosMap.put("OUT", new int[]{92,4});
        ageGroupMap.put(AgeGroup.CHILD, "C");
        ageGroupMap.put(AgeGroup.ADULT,"A");
        doSMap.put(DoS.BLUE, "B");
        doSMap.put(DoS.RED, "R");
        doSMap.put(DoS.YELLOW,"Y");
        doSMap.put(DoS.BLACK, "");
    }

    public void logInit(int NoA, int NoC, int NoS){
        String infoMessage=String.format("NoA:%02d, NoC:%02d, NoS:%02d", NoA,NoC,NoS);
        String header="STT | ETH ET1 ET2 | EVR1 EVR2 EVR3 EVR4 | WTH  WTR1 WTR2 | MDH  MDR1 MDR2 MDR3 MDR4 | PYH | OUT";
        commitMessage(infoMessage);
        commitMessage(header);
        logState("INI");
    }

    public void logPatient(Patient patient, String hall, int room){
        String s, message;
        if(messagePosMap.get(hall)[1]==4){
            s=ageGroupMap.get(patient.getAgeGroup())+String.format("%02d", patient.getETN());
        }else{
            s=ageGroupMap.get(patient.getAgeGroup())+String.format("%02d", patient.getETN())+doSMap.get(patient.getDoS());    
        }
        message=formMessage(s, messagePosMap.get(hall)[0]+room*messagePosMap.get(hall)[1]);
        commitMessage(message);
    }

    public void logState(String state){
        String stateMessage=formMessage(state, 0);
        commitMessage(stateMessage);
    }

    private String formMessage(String s, int start){
        String message = this.stockMessage.substring(0, start)+s+this.stockMessage.substring(start+s.length());
        return message;
    }

    public void closeLog() throws IOException{
        this.log.close();
    }

    private void commitMessage(String message){
        try {
            this.rl.lock();
            log.write(message+"\n");
        } catch ( Exception ex ) {}
        finally {
            this.rl.unlock();
        }    
    }
    
}
