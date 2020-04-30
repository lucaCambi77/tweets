/**
 *
 */
package org.cambi.test.config.dbunit;

import org.dbunit.dataset.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author luca
 *
 */
public class JsonDataSet extends AbstractDataSet {

    private JsonTableParser tableParser = new JsonTableParser();

    private List<DefaultTable> tables;

    public JsonDataSet(File file) throws Exception {
        tables = tableParser.getTablesFromInput(file);
    }

    public JsonDataSet(InputStream is) throws IOException, DataSetException {
        tables = tableParser.getTablesFromInput(is);
    }

    @Override
    protected ITableIterator createIterator(boolean reverse) throws DataSetException {
        return new DefaultTableIterator(tables.toArray(new ITable[tables.size()]));
    }

}
