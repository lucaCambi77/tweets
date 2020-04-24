package org.cambi.test.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.datatype.DataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class JSONITableParser {

    private ObjectMapper mapper = new ObjectMapper();

    public List<DefaultTable> getTablesFromInput(File jsonFile) throws Exception {
        return getTablesFromInput(new FileInputStream(jsonFile));
    }

    public List<DefaultTable> getTablesFromInput(InputStream jsonStream) throws IOException, DataSetException {
        List<DefaultTable> tables = new ArrayList<DefaultTable>();

        Map<String, Object> dataset = mapper.readValue(jsonStream, Map.class);

        for (Map.Entry<String, Object> entry : dataset.entrySet()) {
            DefaultTable table = createTable(entry.getKey(), (List<Map<String, Object>>) entry.getValue());
            tables.add(table);
        }

        return tables;
    }

    private DefaultTable createTable(String tableName,
                                     List<Map<String, Object>> rows) throws DataSetException {

        Column[] columns = getColumns(tableName, rows);
        DefaultTable table = new DefaultTable(
                new DefaultTableMetaData(tableName, columns));

        int rowIndex = 0;

        for (Map<String, Object> row : rows) {
            table.addRow();

            for (Map.Entry<String, Object> column : row.entrySet())
                table.setValue(rowIndex, column.getKey(), column.getValue());

            rowIndex++;

        }
        return table;

    }

    private Column[] getColumns(String tableName, List<Map<String, Object>> rows) {
        Set<String> columns = new LinkedHashSet<String>();

        for (Map<String, Object> row : rows) {
            for (Map.Entry<String, Object> column : row.entrySet()) {
                columns.add(column.getKey().toLowerCase());
            }
        }

        List<Column> list = columns.stream().map(s -> new Column(s, DataType.UNKNOWN)).collect(Collectors.toList());

        return list.toArray(new Column[list.size()]);
    }
}
