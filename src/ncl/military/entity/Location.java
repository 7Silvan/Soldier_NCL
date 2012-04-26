package ncl.military.entity;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:48
 */
public class Location {

    // values to take
    // location_id
    // name
    // region
    // city

    public enum ALIAS {
        ID("location_id"),
        NAME("location_name"),
        REGION("location_region"),
        CITY("location_city");

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

    private String name;
    private String region;
    private String city;

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Location(String id, String name, String region, String city) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.city = city;
    }
}
