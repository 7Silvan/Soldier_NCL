package ncl.military.dao;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 16:39
 */
public class Filter {
    private String attribute;
    private String valueToCompare;
    private FilterType typeOfComparison;

    public String getAttribute() {
        return attribute;
    }

    public String getValueToCompare() {
        return valueToCompare;
    }

    public FilterType getTypeOfComparison() {
        return typeOfComparison;
    }

    public Filter(String entityName, String attribute, String valueToCompare, FilterType typeOfComparison) {
        this.attribute = attribute;
        this.valueToCompare = valueToCompare;
        this.typeOfComparison = typeOfComparison;
    }
}
