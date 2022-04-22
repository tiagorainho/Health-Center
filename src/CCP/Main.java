package CCP;

import java.io.IOException;
import java.net.Socket;

import CCP.Comunications.CCPServer;
import CCP.entities.CCP_GUI_Extended;

public class Main {
    
    public static void main(String args[]) {

        String hostName = "localhost";
        int portNumber = 9876;
        Socket socket = null;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                 
        CCPServer server = new CCPServer(socket);
        server.start();

        CCP_GUI_Extended gui = new CCP_GUI_Extended(server);

    }
}
