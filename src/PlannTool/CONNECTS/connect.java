/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CONNECTS;

import com.mysql.jdbc.RowData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class connect {

    public ResultSet rs;
    Connection conn;

    public connect(String startQuery) {

        try {
            String driver = "com.mysql.jdbc.driver";
            String url = "jdbc:mysql://143.116.140.120/trax_mon";
            String username = "pluser";
            String password = "plpass";
            Class.forName("com.mysql.jdbc.Driver");

            conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            String query = startQuery;

            rs = st.executeQuery(query);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
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

    connect() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Statement createStatement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
