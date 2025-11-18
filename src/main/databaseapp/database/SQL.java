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

    
    public static ResultSet rentingCheckouts(Connection conn, String userID) {
        ResultSet rs = null;
        try {
            String sql = "SELECT count(UserID) as 'Equipment Rented:' FROM UserRents WHERE UserID=?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userID);
            
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println(e);
        }

        return rs;
    }

    public static ResultSet mostCheckedOut(Connection conn) {
        ResultSet rs = null;
        try {
            String sql = "SELECT ID as User, max(count) as 'Times Rented' FROM (SELECT count(R.EquipmentSerialNumber) as count, R.UserID as ID FROM Rentals as R GROUP BY R.UserID);";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return rs;
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

    public static String delete(Connection conn, String table, String[] keys, String[] values){
        String out;
        try {
            String sql = "DELETE FROM "+ table + " WHERE ";
            int i = 0;
            for (; i < keys.length - 1; i++) {
                sql = sql + keys[i] + "=? AND ";
            }
            sql = sql + keys[i] + "=?;";

            ps = conn.prepareStatement(sql);
            for (i = 0; i < keys.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            int result = ps.executeUpdate();
            if (result > 0) {
                out = "Record deleted!";
            } else {
                out = "Record not found!";
            }
        } catch (SQLException e) {
            out = e.getMessage();
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

    public static String edit(Connection conn, String table, String[] keys, String[] values, String[] primaryKeys, String[] primaryValues){
        String out = null;
        try {
            String sql = "UPDATE " + table + " SET ";
            int i = 0;
            for (; i < keys.length - 1; i++) {
                sql = sql + keys[i] + "=?, ";
            }
            sql = sql + keys[i] + "=? WHERE ";
            
            for (i = 0; i < primaryKeys.length - 1; i++) {
                sql = sql + primaryKeys[i] + "=?, ";
            }
            sql = sql + primaryKeys[i] + "=?;";

            System.out.println(sql);

            ps = conn.prepareStatement(sql);
            for (i = 0; i < keys.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            for (int j = 0; j < primaryKeys.length; j++) {
                ps.setString(i + j + 1, primaryValues[j]);
            }
            int result = ps.executeUpdate();

            if (result > 0) {
                out = "Updated successfully!";
            } else {
                out = "Edit failed!";
            }
        } catch (SQLException e) {
            out = e.getMessage();
        }

        return out;
    }



}
