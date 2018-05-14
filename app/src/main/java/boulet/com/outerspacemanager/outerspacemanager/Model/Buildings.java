package boulet.com.outerspacemanager.outerspacemanager.Model;

import boulet.com.outerspacemanager.outerspacemanager.Model.Building;

/**
 * Created by atison on 26/02/2018.
 */

public class Buildings {
    private String size;
    private Building[] buildings;

    public void setSize(String size) {
        this.size = size;
    }

    public void setBuildings(Building[] buildings) {
        this.buildings = buildings;
    }

    public String getSize() {
        return size;
    }

    public Building[] getBuildings() {
        return buildings;
    }
}