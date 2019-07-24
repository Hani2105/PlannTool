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

    public static JFileChooser getFileChooserRiport() {
        if (!CTB.riportpath.equals("")) {
            JFileChooser fc = new JFileChooser(CTB.riportpath);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }
     public static JFileChooser getFileChooserScen() {
        if (!CTB.scenpath.equals("")) {
            JFileChooser fc = new JFileChooser(CTB.scenpath);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }

    public static void setLastDirScen(File file) {
        lastDir = file.getParent();
        CTB.scenpath = file.getAbsolutePath() + ".scen";
    }

    public static void setLastDirRiports(File file) {
        lastDir = file.getParent();
        CTB.riportpath = file.getParent();
    }

}
