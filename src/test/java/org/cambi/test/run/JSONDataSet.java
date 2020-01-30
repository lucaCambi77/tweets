/**
 * 
 */
package org.cambi.test.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.DefaultTableIterator;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.datatype.DataType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author luca
 *
 */
public class JSONDataSet extends AbstractDataSet
{
    // The parser for the dataset JSON file
    private JSONITableParser tableParser = new JSONITableParser();

    // The tables after parsing
    private List<DefaultTable> tables;

    /**
     * Creates a JSON dataset based on a file
     * 
     * @param file
     *            A JSON dataset file
     */
    public JSONDataSet(File file)
    {
        tables = tableParser.getTables(file);
    }

    /**
     * Creates a JSON dataset based on an inputstream
     * 
     * @param is
     *            An inputstream pointing to a JSON dataset
     */
    public JSONDataSet(InputStream is)
    {
        tables = tableParser.getTables(is);
    }

    @Override
    protected ITableIterator createIterator(boolean reverse) throws DataSetException
    {
        return new DefaultTableIterator(tables.toArray(new ITable[tables.size()]));
    }

    private class JSONITableParser
    {

        private ObjectMapper mapper = new ObjectMapper();

        /**
         * Parses a JSON dataset file and returns the list of DBUnit tables contained in that file
         * 
         * @param jsonFile
         *            A JSON dataset file
         * @return A list of DBUnit tables
         */
        public List<DefaultTable> getTables(File jsonFile)
        {
            try
            {
                return getTables(new FileInputStream(jsonFile));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        /**
         * Parses a JSON dataset input stream and returns the list of DBUnit tables contained in that input stream
         * 
         * @param jsonStream
         *            A JSON dataset input stream
         * @return A list of DBUnit tables
         */
        @SuppressWarnings("unchecked")
        public List<DefaultTable> getTables(InputStream jsonStream)
        {
            List<DefaultTable> tables = new ArrayList<DefaultTable>();
            try
            {
                // get the base object tree from the JSON stream
                Map<String, Object> dataset = mapper.readValue(jsonStream, Map.class);
                // iterate over the tables in the object tree
                for (Map.Entry<String, Object> entry : dataset.entrySet())
                {
                    // get the rows for the table
                    List<Map<String, Object>> rows = (List<Map<String, Object>>) entry.getValue();
                    ITableMetaData meta = getMetaData(entry.getKey(), rows);
                    // create a table based on the metadata
                    DefaultTable table = new DefaultTable(meta);
                    int rowIndex = 0;
                    // iterate through the rows and fill the table
                    for (Map<String, Object> row : rows)
                    {
                        fillRow(table, row, rowIndex++);
                    }
                    // add the table to the list of DBUnit tables
                    tables.add(table);
                }

            }
            catch (IOException e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
            return tables;
        }

        /**
         * Gets the table meta data based on the rows for a table
         * 
         * @param tableName
         *            The name of the table
         * @param rows
         *            The rows of the table
         * @return The table metadata for the table
         */
        private ITableMetaData getMetaData(String tableName, List<Map<String, Object>> rows)
        {
            Set<String> columns = new LinkedHashSet<String>();
            // iterate through the dataset and add the column names to a set
            for (Map<String, Object> row : rows)
            {
                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    columns.add(column.getKey());
                }
            }

            List<Column> list = new ArrayList<Column>(columns.size());
            // create a list of DBUnit columns based on the column name set
            for (String s : columns)
            {
                list.add(new Column(s, DataType.UNKNOWN));
            }
            return new DefaultTableMetaData(tableName, list.toArray(new Column[list.size()]));
        }

        /**
         * Fill a table row
         * 
         * @param table
         *            The table to be filled
         * @param row
         *            A map containing the column values
         * @param rowIndex
         *            The index of the row to te filled
         */
        private void fillRow(DefaultTable table, Map<String, Object> row, int rowIndex)
        {
            try
            {
                table.addRow();
                // set the column values for the current row
                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    table.setValue(rowIndex, column.getKey(), column.getValue());

                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
