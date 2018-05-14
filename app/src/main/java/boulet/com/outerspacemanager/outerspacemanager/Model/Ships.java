package boulet.com.outerspacemanager.outerspacemanager.Model;

import boulet.com.outerspacemanager.outerspacemanager.Model.Ship;

/**
 * Created by bouleta on 26/02/2018.
 */
public class Ships {
    private String currentUserMinerals;
    private String currentUserGas;
    private String size;
    private String amount;
    private Ship[] ships;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setCurrentUserGas(String currentUserGas) {
        this.currentUserGas = currentUserGas;
    }

    public void setCurrentUserMinerals(String currentUserMinerals) {
        this.currentUserMinerals = currentUserMinerals;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    public String getSize() {
        return size;
    }

    public Ship[] getShips() {
        return ships;
    }

    public String getCurrentUserGas() {
        return currentUserGas;
    }

    public String getCurrentUserMinerals() {
        return currentUserMinerals;
    }

}