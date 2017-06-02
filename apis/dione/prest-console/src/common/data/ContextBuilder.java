package common.data;

public interface ContextBuilder {

    /**
     * @return TODO
     */
    public DataContext store() throws Exception;

    /**
     * @param dataContext
     * @throws UnsupportedDataContextException
     */
    void load(DataContext dataContext) throws Exception;

}
