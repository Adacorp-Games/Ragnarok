package outerhaven;

import java.util.ArrayList;

public class Case {
    int id;
    boolean status;
    ArrayList<Case> caseVoisines;

    public Case(int id, boolean status) {
        this.id = id;
        this.status = false;
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Case> getCaseVoisines() {
        return caseVoisines;
    }
}
