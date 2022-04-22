package HCP;
import java.io.IOException;
import java.net.ServerSocket;

import HCP.Comunications.HCPServer;



public class Main {



    public static void main(String[] args) {
        new Thread(() -> {
            int portNumber = 9876;
            try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
                while (true) {
                    new HCPServer(serverSocket.accept()).start();
                }
            } catch (IOException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Could not listen on port " + portNumber);
                System.exit(-1);
            }
        }).start();

    }

}