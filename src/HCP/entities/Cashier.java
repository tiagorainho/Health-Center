package HCP.entities;

import java.util.Random;

public class Cashier {

    private boolean isAvailable;
    private final int maxPYT;
    
    public Cashier(int maxPYT) {
        this.maxPYT=maxPYT;
        isAvailable = true;
    }

    public void providePaymentService(Patient patient) {
        this.isAvailable = false;
        try {
            Thread.sleep(new Random().nextInt(this.maxPYT));
        } catch (InterruptedException e) {}
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

}
