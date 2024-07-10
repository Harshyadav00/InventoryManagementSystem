package main.java.com.inventory.dao;

import java.io.*;
import java.sql.* ;

public class CSVExporter {

    public void exportToCSV(String filePath, ResultSet resultSet) throws IOException, SQLException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write headers
            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Write rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(resultSet.getString(i));
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        }
    }

}
