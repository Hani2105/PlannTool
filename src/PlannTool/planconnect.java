/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

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
public class planconnect {

    public ResultSet rs;
    public Connection conn;

    public Object planconnect(String startQuery) throws SQLException, ClassNotFoundException {

        String driver = "com.mysql.jdbc.driver";
        String url = "jdbc:mysql://143.116.140.114/planningdb?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8";
        String username = "plan";
        String password = "plan500";
        Class.forName("com.mysql.jdbc.Driver");
        

        this.conn = (Connection) DriverManager.getConnection(url, username, password);
        Statement st = conn.createStatement();

        rs = st.executeQuery(startQuery);

        return rs;

    }

    public Object templekerdez(String startQuery) throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        Statement st = conn.createStatement();

        rs = st.executeQuery(startQuery);
        return rs;

    }

    public static TableModel resultSetToTableModel(ResultSet rs, DefaultTableModel tm, int colnum) {
        try {

            DefaultTableModel szerkeszt = tm;
            String value = "Nincs találat";

            if (rs.next()) {
                value = rs.getNString("Board_PN");
            }

            szerkeszt.setValueAt(value, 0, colnum);

            return szerkeszt;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void feltolt(String query, boolean infobox) {
        Connection conn = null;
        Statement stmt = null;
        try {

            String driver = "com.mysql.jdbc.driver";
            String url = "jdbc:mysql://143.116.140.114:3306/planningdb?characterEncoding=utf8";
            String username = "plan";
            String password = "plan500";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            if (infobox == true) {
                infobox info = new infobox();
                info.infoBox("Sikeres feltöltés!", "Mentés!");
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés! " + se.getMessage(), "Hiba!");

        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés!", "Hiba!");

        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public void createtemp(String query, boolean infobox) {

        Statement stmt = null;
        try {

            String driver = "com.mysql.jdbc.driver";
            String url = "jdbc:mysql://143.116.140.114:3306/planningdb?characterEncoding=utf8";
            String username = "plan";
            String password = "plan500";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            if (infobox == true) {
                infobox info = new infobox();
                info.infoBox("Sikeres feltöltés!", "Mentés!");
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés!", "Hiba!");

        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés!", "Hiba!");

        }

    }

    public void kinyir() {

        try {
            if (conn != null) {
                this.rs.close();
                this.conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(planconnect.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
    
        public void feltolt1(String query, boolean infobox) {
        Connection conn = null;
        Statement stmt = null;
        try {

            String driver = "com.mysql.jdbc.driver";
            String url = "jdbc:mysql://143.116.140.114:3306/planningdb?CharSet=utf8mb4";
            String username = "plan";
            String password = "plan500";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            if (infobox == true) {
                infobox info = new infobox();
                info.infoBox("Sikeres feltöltés!", "Mentés!");
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés! " + se.getMessage(), "Hiba!");

        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            infobox info = new infobox();
            info.infoBox("Sikertelen feltöltés!", "Hiba!");

        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

}
