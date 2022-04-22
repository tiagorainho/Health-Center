
package CCP.Comunications;

import java.io.*;
import java.net.*;

import CCP.interfaces.ISimulation;

    
public class CCPServer extends Thread implements ISimulation {
    private String hostName = "localhost";
    private int portNumber = 999;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    

    public CCPServer( Socket socket){
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            this.stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {

        } catch (IOException e) {

        }
        System.out.println("CCP Comunications has started on " + hostName + ":" + portNumber);
    }

    public void startSimulation(int NoA, int NoC, int NoS, int maxEVT, int maxMDT, int maxPYT, int maxTtm) {
        String message= String.format("START:%d,%d,%d,%d,%d,%d,%d", NoA,NoC,NoS, maxEVT, maxMDT, maxPYT, maxTtm);
        this.out.println(message);
        this.out.flush();        
    }

    public void resumeSimulation(){
        this.out.println("RESUME");
        this.out.flush();    
    }

    public void suspendSimulation(){
        this.out.println("SUSPEND");
        this.out.flush();  
    }

    public void stopSimulation() {
        this.out.println("STOP");
        this.out.flush();        
    }

    public void endSimulation(){
        this.out.println("END");
        this.out.flush(); 
    }

    public void manualSimulation(){
        this.out.println("MANUAL");
        this.out.flush();  
    }

    public void nextMove(){
        this.out.println("MOVE");
        this.out.flush();
    }

    /**
     * Implements the state machine for Server Listening on Client Side
     */
    @Override
    public void run() {
        try {
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);

            }
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Could not send message to server");
        }
    }   
}