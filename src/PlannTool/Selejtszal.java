/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Selejtszal extends Thread{
    
    public void run(){
    
        selejt sele = null;
        try {
            sele = new selejt(new ablak());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Selejtszal.class.getName()).log(Level.SEVERE, null, ex);
        }
        sele.setDefaultCloseOperation(sele.DISPOSE_ON_CLOSE);
        sele.setVisible(true);
        
        
    
    }
    
}
