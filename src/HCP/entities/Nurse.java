package HCP.entities;

import java.util.Random;

import HCP.enums.DoS;

public class Nurse {

    private final Random randomGenerator;

    public Nurse() {
        this.randomGenerator = new Random();
    }

    public DoS evaluate(Patient patient) {
        switch(this.randomGenerator.nextInt(3)) {
            case 0:
                return DoS.BLUE;
            case 1:
                return DoS.RED;
            default:
                return DoS.YELLOW;
        }
    }

}
