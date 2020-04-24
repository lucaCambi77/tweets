/**
 *
 */
package org.cambi.test.run;

import java.io.*;
import java.util.List;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;

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
