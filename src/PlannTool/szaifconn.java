/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author gabor_hanacsek
 */
public class szaifconn {

    String driver;
    String url;
    String username;
    String password;
    ResultSet rs;

    szaifconn(String driver, String url, String username, String password) throws ClassNotFoundException {

        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        Class.forName("com.mysql.jdbc.Driver");

    }

    public Object lekerdez(String query) throws SQLException, ClassNotFoundException, Exception {

        Connection conn = (Connection) DriverManager.getConnection(url, username, password);
        Statement st = conn.createStatement();
        rs = st.executeQuery(query);

        return rs;

    }

}
