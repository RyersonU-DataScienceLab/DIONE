/**
 *
 */
package common.data;


public class DataField {

    /**
     * the value of the field to be casted to its type which is declared
     * to be either nominal or double in the corresponding DataHeader
     */
    private Object value;

    /**
     * corresponding DataHeader of the DataField
     */
    private DataHeader dataHeader;

    /**
     *
     */
    public DataField() {

    }

    /**
     * @param dataHeader
     */
    public DataField(DataHeader dataHeader) {
        this.dataHeader = dataHeader;
    }

    /**
     * @param fieldValue Loads the field value.
     *                   Uses the assumption that all fields other than nominal ones will be
     *                   of type double. Otherwise, i.e. if a field is set to be nominal, then
     *                   its value will be kept as string.
     *                   Casts the given string to double if the field type
     *                   is not set to be nominal
     */
    public void load(Object fieldValue) {

        if (dataHeader.isNominal())
            value = fieldValue;
        else
            value = Double.parseDouble(fieldValue == null? "" : fieldValue.toString());

    }

    /**
     * @return String value of the field
     */
    public String getStringValue() {
        return value.toString();
    }

    /**
     * @return
     */
    public DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * @param dataHeader
     */
    public void setDataHeader(DataHeader dataHeader) {
        this.dataHeader = dataHeader;
    }

    /**
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
