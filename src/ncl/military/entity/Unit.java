package ncl.military.entity;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:41
 */
public class Unit {

    /*
     * Fields (id, head, location) are id to identificate
     * entities within the dao.
     */

    private String id;
    private String head;
    private String location;

    private String name;

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

    public Unit(String id, String head, String location, String name) {
        this.id = id;
        this.head = head;
        this.location = location;
        this.name = name;
    }
}