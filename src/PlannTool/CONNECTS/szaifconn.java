/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CONNECTS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class szaifconn {

    String driver;
    String url;
    String username;
    String password;
    public ResultSet rs;
    Connection conn;

    public szaifconn(String driver, String url, String username, String password) throws ClassNotFoundException {

        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        Class.forName("com.mysql.jdbc.Driver");

    }

    public Object lekerdez(String query) throws SQLException, ClassNotFoundException, Exception {

        conn = (Connection) DriverManager.getConnection(url, username, password);
        Statement st = conn.createStatement();
        rs = st.executeQuery(query);

        return rs;

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

}
