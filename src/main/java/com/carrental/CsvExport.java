package com.carrental;
import com.carrental.SingletonConnection;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

public class CsvExport {
    private BufferedWriter fileWriter;

    public void export() {
        String table = "Revenue";
        String csvFileName = getFileName(table.concat("_Export"));

        try {
            Connection connection = SingletonConnection.getConnection();
            String sql = "SELECT DATE_FORMAT(DATE_FORMAT(res.startDate, '%Y-%m-01'), '%Y-%m') AS month, SUM(DATEDIFF(LEAST(DATE_FORMAT(res.endDate, '%Y-%m-01'), LAST_DAY(res.startDate)), res.startDate) * veh.price) AS total_revenue FROM Reservations res JOIN Vehicles veh ON res.idV = veh.idV WHERE res.status = 1 GROUP BY month ORDER BY month;";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            fileWriter = new BufferedWriter(new FileWriter(csvFileName));

            int columnCount = writeHeaderLine(result);

            while (result.next()) {
                String line = "";

                for (int i = 1; i <= columnCount; i++) {
                    Object valueObject = result.getObject(i);
                    String valueString = "";

                    if (valueObject != null) valueString = valueObject.toString();

                    if (valueObject instanceof String) {
                        valueString = "\"" + escapeDoubleQuotes(valueString) + "\"";
                    }

                    line = line.concat(valueString);

                    if (i != columnCount) {
                        line = line.concat(",");
                    }
                }

                fileWriter.newLine();
                fileWriter.write(line);
            }

            statement.close();
            fileWriter.close();

        } catch (SQLException e) {
            System.out.println("Datababse error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }

    }

    public String getFileName(String baseName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTimeInfo = dateFormat.format(new Date());
        return baseName.concat(String.format("_%s.csv", dateTimeInfo));
    }

    public int writeHeaderLine(ResultSet result) throws SQLException, IOException {
        // write header line containing column names
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        String headerLine = "";

        // exclude the first column which is the ID field
        for (int i = 1; i <= numberOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            headerLine = headerLine.concat(columnName).concat(",");
        }

        fileWriter.write(headerLine.substring(0, headerLine.length() - 1));

        return numberOfColumns;
    }

    public String escapeDoubleQuotes(String value) {
        return value.replaceAll("\"", "\"\"");
    }

}
