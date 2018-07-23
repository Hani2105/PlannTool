/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

/**
 *
 * @author gabor_hanacsek
 */
public class Stat {

    public void beir(String Username, String Tabname) {

        
        String Query = "insert into planningdb.plannstat (username , tabname) values ('"+Username+"','"+Tabname+"')";
        planconnect conn = new planconnect();
        conn.feltolt(Query);
        
    }

}
