package databaseapp.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQL {

    private static PreparedStatement ps;

    public static ArrayList<String> getColumns(Connection conn, String table) {
        ArrayList<String> names = new ArrayList<String>();
        try {
            ps = conn.prepareStatement("SELECT * FROM "+ table + " WHERE false;");
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                names.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return names;
    }

    public static String[] getPrimaryKeys(Connection conn, String table) {
        ArrayList<String> names = new ArrayList<String>();
        String[] out = null;
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getPrimaryKeys(null, null, table);
            while (rs.next()) {
                names.add(rs.getString("COLUMN_NAME"));
            }
            out = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                out[i] = names.get(i);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return out;
    }

    private static String generateValueList(int size) {
        String valueList = "(";
        for (int i = 0; i < size; i++) {
            valueList = valueList + "?,";
        }
        valueList = valueList.substring(0, valueList.length() -1);
        return valueList;
    }

    public static String add(Connection conn, String table, String[] newRecord){
        String out = " Added: ";
        try {
            String sql = "INSERT INTO "+ table + " VALUES ";
            
            sql = generateValueList(newRecord.length) + ";";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < newRecord.length; i++) {
                ps.setString(i + 1, newRecord[i]);
                out = out + " "+ newRecord[i];
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            out = "Adding failed!";
        }

        return out;
    }

    public static ResultSet search(Connection conn, String table, String[] keys, String[] values){
        ResultSet out = null;
        try {
            String sql = "SELECT * FROM "+ table + " WHERE ";
            int i = 0;
            for (; i < keys.length - 1; i++) {
                sql = sql + keys[i] + "=? AND ";
            }
            sql = sql + keys[i] + "=?;";

            ps = conn.prepareStatement(sql);
            for (i = 0; i < keys.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            out = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println(e);
        }

        return out;
    }



}
