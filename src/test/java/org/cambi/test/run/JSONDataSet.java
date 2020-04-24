/**
 *
 */
package org.cambi.test.run;

import org.dbunit.dataset.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author luca
 *
 */
public class JSONDataSet extends AbstractDataSet {

    private JSONITableParser tableParser = new JSONITableParser();

    private List<DefaultTable> tables;

    public JSONDataSet(File file) throws Exception {
        tables = tableParser.getTablesFromInput(file);
    }

    public JSONDataSet(InputStream is) throws IOException, DataSetException {
        tables = tableParser.getTablesFromInput(is);
    }

    @Override
    protected ITableIterator createIterator(boolean reverse) throws DataSetException {
        return new DefaultTableIterator(tables.toArray(new ITable[tables.size()]));
    }

}
