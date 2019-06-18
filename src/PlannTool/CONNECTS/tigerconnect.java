/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CONNECTS;

import PlannTool.ablak;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class tigerconnect {

    public ResultSet rs;
    public Connection conn;

    public Object lekerdez(String startQuery) throws SQLException, ClassNotFoundException {

        String driver = "com.mysql.jdbc.driver";
        String url = "jdbc:mysql://172.31.133.111/Query?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8";
        String username = "dbguser";
        String password = "Sanmina1";
        Class.forName("com.mysql.jdbc.Driver");

        this.conn = (Connection) DriverManager.getConnection(url, username, password);
        Statement st = conn.createStatement();

        rs = st.executeQuery(startQuery);

        return rs;

    }

    public void storedSN(String SN) throws ClassNotFoundException {

        String driver = "com.mysql.jdbc.driver";
        String url = "jdbc:mysql://172.31.133.111/Query?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8";
        String username = "dbguser";
        String password = "Sanmina1";
        Class.forName("com.mysql.jdbc.Driver");
        String sql = "{call Query.WMR(?)}";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                CallableStatement stmt = conn.prepareCall(sql);) {

            //Set IN parameter
            stmt.setString(1, SN);

            //Execute stored procedure
            stmt.execute();
            rs = stmt.getResultSet();
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) ablak.jTable22.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                model.addRow(new Object[]{rs.getString(1), rs.getString(2)});

            }

            ablak.jTable22.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        kinyir();

    }

    public void storedUBTTesters(String tol, String ig) throws ClassNotFoundException {

        String driver = "com.mysql.jdbc.driver";
        String url = "jdbc:mysql://172.31.133.111/Query?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8";
        String username = "dbguser";
        String password = "Sanmina1";
        Class.forName("com.mysql.jdbc.Driver");
        String sql = "{call Query.UBT_Elek_Z(?,?)}";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                CallableStatement stmt = conn.prepareCall(sql);) {

            //Set IN parameter
            stmt.setString(1, tol);
            stmt.setString(2, ig);

            //Execute stored procedure
            stmt.execute();
            rs = stmt.getResultSet();
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) ablak.jTable23.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});

            }

            ablak.jTable23.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        kinyir();

    }

    public void storedMPTTesters(String tol, String ig) throws ClassNotFoundException {

        String driver = "com.mysql.jdbc.driver";
        String url = "jdbc:mysql://172.31.133.111/Query?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8";
        String username = "dbguser";
        String password = "Sanmina1";
        Class.forName("com.mysql.jdbc.Driver");
        String sql = "{call Query.MPT_test_data(?,?)}";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                CallableStatement stmt = conn.prepareCall(sql);) {

            //Set IN parameter
            stmt.setString(1, tol);
            stmt.setString(2, ig);

            //Execute stored procedure
            stmt.execute();
            rs = stmt.getResultSet();
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) ablak.jTable23.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});

            }

            ablak.jTable23.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        kinyir();

    }

    
    public void kinyir() {

        try {
            if (conn != null) {
                this.rs.close();
                this.conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(tigerconnect.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
