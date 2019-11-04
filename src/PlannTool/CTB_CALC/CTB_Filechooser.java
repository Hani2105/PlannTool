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
            fc.setFont(new java.awt.Font("Segoe Script", 1, 12));
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setFont(new java.awt.Font("Segoe Script", 1, 12));
            return fc;
        }
    }
     public static JFileChooser getFileChooserScen() {
        if (!CTB.scenpath.equals("")) {
            JFileChooser fc = new JFileChooser(CTB.scenpath);
            fc.setFont(new java.awt.Font("Segoe Script", 1, 12));
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setFont(new java.awt.Font("Segoe Script", 1, 12));
            return fc;
        }
    }

    public static void setLastDirScen(File file) {
        lastDir = file.getParent();
        CTB.scenpath = file.getParent();
    }

    public static void setLastDirRiports(File file) {
        lastDir = file.getParent();
        CTB.riportpath = file.getParent();
    }

}
