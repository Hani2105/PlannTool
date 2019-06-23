/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_Filechooser {

    private static String lastDir = null;

    public static JFileChooser getFileChooser() {
        if (lastDir != null) {
            JFileChooser fc = new JFileChooser(lastDir);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }

    public static void setLastDir(File file) {
        lastDir = file.getParent();
    }

}
