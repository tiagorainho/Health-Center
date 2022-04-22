package CCP.interfaces;

public interface ISimulation {
    
    void startSimulation(int NoA, int NoC, int NoS, int maxEVT, int maxMDT, int maxPYT, int maxTtm);

    public void resumeSimulation();

    public void suspendSimulation();

    public void stopSimulation();

    public void endSimulation();

    public void manualSimulation();

    public void nextMove();

}
