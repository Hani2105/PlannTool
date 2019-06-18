/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool_TABS;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author gabor_hanacsek
 */
public class jlistfilterfromtestbox {
    
    public DefaultListModel<String> filterModel(DefaultListModel<String> model, String filter, ArrayList<String> defaultadat) {
    for (String s : defaultadat) {
        if (!s.startsWith(filter)) {
            if (model.contains(s)) {
                model.removeElement(s);
            }
        } else {
            if (!model.contains(s)) {
                model.addElement(s);
            }
        }
    }
    
    return model;
}
    
    
    
    
    
}
