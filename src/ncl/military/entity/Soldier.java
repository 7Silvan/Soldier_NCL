package ncl.military.entity;

import java.sql.Date;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:23
 */

public class Soldier {

    // values to take
    // soldier_id
    // soldier_name
    // soldier_rank
    // soldier_commander
    // unit_name
    // soldier_birthdate

    // had an idea to use required aliases for all implementations of dao
    public enum ALIAS {
        ID("soldier_id"),
        NAME("soldier_name"),
        RANK("soldier_rank"),
        COMMANDER("soldier_commander"),
        UNIT("unit_name"),
        BIRTHDATE("soldier_birthdate");

        // with no inheritance for a while

        private String label;

        ALIAS(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }

        public String getLabelAsQueried() {
            return "queried_" + this.label;
        }

        public static ALIAS getAlias(String label) {
            if (label != null) {
                for (ALIAS a : ALIAS.values()) {
                    if (label.equals(a.label))
                        return a;
                }
                throw new IllegalArgumentException("Given label of " + label + " was not found.");
            } else
                throw new IllegalArgumentException("Given label must not be null.");
        }
    }

    private String id;
    private String commander;
    private String unit;

    private String name;
    private String rank;
    private Date birthDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    public Soldier(String id, String name, String rank, String unit, String commander, Date birthDate) {
        this.unit = unit;
        this.rank = rank;
        this.birthDate = birthDate;
        this.commander = commander;
        this.name = name;
        this.id = id;
    }

    public Soldier() {
    }
}
