package metrics;

/**
 * A base metric encapsulates a String (the label or "name" of the metric) and boolean value (of whether or not
 * the metric is a nominal value).
 */
public class BaseMetric {
    private String label;
    private boolean isNominal;

    /**
     * Defines a metric type and gives it a name, this constructor will assume it is non-nominal.
     * @param label The label or "name" of the metric.
     */
    public BaseMetric(String label) {
        this.label = label;
    }

    /**
     * Defines a metric type and gives it a name and nominality.
     * @param label The label or "name" of the metric.
     * @param isNominal Whether or not this metric is
     */
    public BaseMetric(String label, boolean isNominal) {
        this.label = label;
        this.isNominal = isNominal;
    }

    /**
     * Getter for label
     * @return The value of the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for label
     * @param label The value to set the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for nominal boolean
     * @return The value of the boolean
     */
    public boolean isNominal() {
        return isNominal;
    }

    /**
     * Setter for nominal boolean
     * @param isNominal The value to set the boolean
     */
    public void setNominal(boolean isNominal) {
        this.isNominal = isNominal;
    }

}
