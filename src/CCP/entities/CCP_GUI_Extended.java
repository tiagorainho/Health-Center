package CCP.entities;

import CCP.Comunications.CCPServer;

public class CCP_GUI_Extended extends CCP_GUI {

    private final CCPServer server;

    public CCP_GUI_Extended(CCPServer server) {
        super();
        this.setVisible(true);
        this.server = server;
    }

    @Override
    protected void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        super.startButtonActionPerformed(evt);        
        server.startSimulation(super.getAdultPatients(),super.getChildPatients(),super.getNumSeats(),super.getMaxEVT(),super.getMaxMDT(),super.getMaxPYT(),super.getMaxTtm());
    }

    @Override
    protected void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        super.stopButtonActionPerformed(evt);
        server.stopSimulation();
    }

    @Override
    protected void resumeButtonActionPerformed(java.awt.event.ActionEvent evt){
        super.resumeButtonActionPerformed(evt);
        server.resumeSimulation();
    }

    @Override
    protected void suspendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        super.suspendButtonActionPerformed(evt);
        server.suspendSimulation();
    }

    @Override
    protected void endButtonActionPerformed(java.awt.event.ActionEvent evt) {
        server.endSimulation();
        super.endButtonActionPerformed(evt);
    }

    @Override
    protected void autoRadioActionPerformed(java.awt.event.ActionEvent evt) {
        super.autoRadioActionPerformed(evt);   
        server.resumeSimulation();    
    }

    @Override
    protected void manualRadioActionPerformed(java.awt.event.ActionEvent evt) {
        super.manualRadioActionPerformed(evt);
        server.manualSimulation();
    }
    
    @Override
    protected void authorizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        super.authorizeButtonActionPerformed(evt);
        server.nextMove();
    }
}
