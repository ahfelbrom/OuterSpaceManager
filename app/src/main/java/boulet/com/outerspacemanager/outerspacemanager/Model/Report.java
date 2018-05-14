package boulet.com.outerspacemanager.outerspacemanager.Model;

/**
 * Created by aboulet on 09/04/2018.
 */

public class Report {
    private ShipReport[] attackerFleet;
    private ShipReport attackerFleetAfterBattle;
    private String date;
    private String dateInv;
    private ShipReport[] defenderFleet;
    private ShipReport defenderFleetAfterBattle;
    private String from;
    private String gasWon;
    private String mineralsWon;
    private String to;
    private String type;

    public ShipReport[] getAttackerFleet() {
        return attackerFleet;
    }

    public void setAttackerFleet(ShipReport[] attackerFleet) {
        this.attackerFleet = attackerFleet;
    }

    public ShipReport getAttackerFleetAfterBattle() {
        return attackerFleetAfterBattle;
    }

    public void setAttackerFleetAfterBattle(ShipReport attackerFleetAfterBattle) {
        this.attackerFleetAfterBattle = attackerFleetAfterBattle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateInv() {
        return dateInv;
    }

    public void setDateInv(String dateInv) {
        this.dateInv = dateInv;
    }

    public ShipReport[] getDefenderFleet() {
        return defenderFleet;
    }

    public void setDefenderFleet(ShipReport[] defenderFleet) {
        this.defenderFleet = defenderFleet;
    }

    public ShipReport getDefenderFleetAfterBattle() {
        return defenderFleetAfterBattle;
    }

    public void setDefenderFleetAfterBattle(ShipReport defenderFleetAfterBattle) {
        this.defenderFleetAfterBattle = defenderFleetAfterBattle;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGasWon() {
        return gasWon;
    }

    public void setGasWon(String gasWon) {
        this.gasWon = gasWon;
    }

    public String getMineralsWon() {
        return mineralsWon;
    }

    public void setMineralsWon(String mineralsWon) {
        this.mineralsWon = mineralsWon;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if(this.getType().equals("attacker")) {
            String def_fleet = "";
            if(this.getDefenderFleet() != null) {
                for (ShipReport ship : this.getDefenderFleet()) {
                    def_fleet = def_fleet + "\n\t - " + ship.toString();
                }
            }
            return "You have attacked " + this.getTo()
                    + " !\n\tGas won : " + Math.round(Double.parseDouble(this.getGasWon()))
                    + "\n\tMinerals won : " + Math.round(Double.parseDouble(this.getMineralsWon()))
                    + "\n\tDefender fleet : " + def_fleet
                    + "\n\tAfter battle : " + this.defenderFleetAfterBattle.getSurvivingShips()
                    + " ships survived";
        }else {
            String att_fleet = "";
            if(this.getAttackerFleet() != null) {
                for (ShipReport ship : this.getAttackerFleet()) {
                    att_fleet = att_fleet + "\n\t - " + ship.toString();
                }
            }
            return "You have been attacked by " + this.getFrom()
                    + " !\n\tGas won : " + Math.round(Double.parseDouble(this.getGasWon()))
                    + "\n\tMinerals won : " + Math.round(Double.parseDouble(this.getMineralsWon()))
                    + "\n\tAttacker fleet : " + att_fleet
                    + "\n\tAfter battle : " + this.attackerFleetAfterBattle.getSurvivingShips()
                    + " ships survived";
        }
    }
}
