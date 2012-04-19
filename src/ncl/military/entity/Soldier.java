package ncl.military.entity;

import java.sql.Date;

/**
 * @author gural
 * @version  1.0
 * Date: 13.04.12
 * Time: 13:23
 */

public class Soldier {
    /*
     * Fields (id, commander, unit) are id to identificate
     * entities within the dao.
     */
    private String id;
    private String commander;
    private String unit;
    
    private String name;
    private String rank;
    private Date birthDate;

    // TODO methods for getting collection of users from dao.

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getCommander() {
        return commander;
    }

    public void setCommander(String commander) {
        this.commander = commander;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Soldier(String id, String commander, String unit, String name, Date birthDate, String rank) {
        this.unit = unit;
        this.rank = rank;
        this.birthDate = birthDate;
        this.commander = commander;
        this.name = name;
        this.id = id;
    }

    public Soldier() {}
}