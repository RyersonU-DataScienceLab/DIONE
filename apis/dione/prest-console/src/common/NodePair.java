package common;

public class NodePair {
    private String name;
    private Object value;

    /**
     * Create a NodePair with a name and value
     *
     * @param name The name
     * @param value The value
     */
    public NodePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Getter for name of the node
     *
     * @return Name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of the node
     *
     * @param name Name to set for the node
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for value of the node
     *
     * @return Value of the node
     */
    public Object getValue() {
        return value;
    }

    /**
     * Setter for value of the node
     *
     * @param value Value to set for the node
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
