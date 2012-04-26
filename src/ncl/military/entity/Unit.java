package ncl.military.entity;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:41
 */
public class Unit {

    // values to take
    // unit_id
    // unit_name
    // soldier_name
    // location_name

    public enum ALIAS {
        ID("unit_id"),
        NAME("unit_name"),
        HEAD("soldier_name"),
        LOCATION("location_name"),
        HEADID("soldier_id");

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
    private String head;
    private String location;
    private String name;

    private String headId;

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit(String id, String head, String location, String name, String headId) {
        this.id = id;
        this.head = head;
        this.location = location;
        this.name = name;
        this.headId = headId;
    }
}
