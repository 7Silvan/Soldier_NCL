package ncl.military.controller.handle.searchtool;

/**
 * @author gural
 * @version 1.0
 *          Date: 24.04.12
 *          Time: 13:05
 */
public class Filter {
    private String attribute;
    private String matchValue;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(String matchValue) {
        this.matchValue = matchValue;
    }
}
