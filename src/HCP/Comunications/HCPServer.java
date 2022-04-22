
package HCP.Comunications;


import java.net.*;

import HCP.Threads.TPatient;
import HCP.entities.ManchesterHospital;
import HCP.entities.Patient;
import HCP.entities.SingletonLogger;
import HCP.enums.AgeGroup;
import HCP.interfaces.IHospital;

import java.io.*;
 
public class HCPServer extends Thread {
    private Socket socket = null;
    IHospital hospital = null;

    /**
     * Constructor to instantiate a TMultiServer object
     * @param socket Client Server socket
     * @param control SimulationControl interface
     */
    public HCPServer(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }
     
    /**
     * Implements the state machine for server entity
     */
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                String command = inputLine.split(":")[0].toUpperCase();
                switch(command) {
                    case "OPERATION":
                        String args[] = inputLine.split(":");
                        break;
                    case "START":
                        String inputArgs[] = inputLine.split(":")[1].split(",");
                        int NoA = Integer.valueOf(inputArgs[0]);
                        int NoC = Integer.valueOf(inputArgs[1]);
                        int NoS = Integer.valueOf(inputArgs[2]);

                        int maxEVT = Integer.valueOf(inputArgs[3]);
                        int maxMDT = Integer.valueOf(inputArgs[4]);
                        int maxPYT = Integer.valueOf(inputArgs[5]);
                        int maxTtm = Integer.valueOf(inputArgs[6]);

                        SingletonLogger.getInstance().logInit(NoA, NoC, NoS);

                        // create patients
                        Patient[] patients = new Patient[NoA+NoC];
                        for(int i=0;i<NoC;i++){
                            patients[i] = new Patient(i, AgeGroup.CHILD);
                            patients[i].setTtm(maxTtm);
                        }
                            
                        for(int i=NoC;i<NoC+NoA;i++){
                            patients[i] = new Patient(i, AgeGroup.ADULT);
                            patients[i].setTtm(maxTtm);
                        }

                        // create hospital
                        this.hospital = new ManchesterHospital(NoS, maxEVT, maxMDT, maxPYT);

                        this.processHospital(hospital,patients);                        
                        break;
                    case "RESUME":
                        SingletonLogger.getInstance().logState("RUN");
                        hospital.getCallCenter().updateStatus("RUN");
                        break;
                    case "SUSPEND":
                        SingletonLogger.getInstance().logState("SUS");
                        hospital.getCallCenter().updateStatus("SUSPEND");
                        break;
                    case "STOP":
                        SingletonLogger.getInstance().logState("STO");
                        hospital.getCallCenter().updateStatus("SUSPEND");
                        hospital=null;
                        break;
                    case "MANUAL":
                        SingletonLogger.getInstance().logState("MAN");
                        hospital.getCallCenter().updateStatus("SUSPEND");
                        break;
                    case "MOVE":
                        hospital.getCallCenter().updateStatus("MOVE_ONE");
                        break;
                    case "END":
                        SingletonLogger.getInstance().logState("END");
                        SingletonLogger.getInstance().closeLog();
                        System.exit(0);
                        break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processHospital(IHospital hospital, Patient[] patients) {
        new Thread(() -> {
            
            // process patients
            TPatient[] patientsThreads = new TPatient[patients.length];
            for(int i=0;i<patients.length;i++)
                patientsThreads[i] = hospital.processPatient(patients[i]);

            for(TPatient patientThread: patientsThreads) {
                try {
                    patientThread.join();
                } catch (InterruptedException e) {}
            }

            try{
                SingletonLogger.getInstance().closeLog();
            }catch(IOException io){}
            
            System.out.println("Simulation Ended !");
            this.hospital = null;
        }).start();
    }
}