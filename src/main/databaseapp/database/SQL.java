package databaseapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQL {

    private static PreparedStatement ps;

    public static ArrayList<String> getColumns(Connection con, String table) {
        ArrayList<String> names = new ArrayList<String>();
        try {
            ps = con.prepareStatement("SELECT * FROM "+ table + " WHERE false;");
            // System.out.print(con.isValid(10));
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

    public static String add(Connection conn, String table, String[] newRecord){
        String out = " Added: ";

        try {
            String sql = "INSERT INTO "+ table + " VALUES (";
            for (int i = 0; i < newRecord.length; i++) {
                sql = sql + "?,";
            }
            sql = sql.substring(0, sql.length() -1);
            sql = sql + ");";
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

}
