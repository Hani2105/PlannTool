/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.CTB_CALC.CTB_Filechooser;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author gabor_hanacsek
 */
public class ImgUp extends javax.swing.JFrame {

    /**
     * Creates new form ImgUp
     */
    File f;
    private String path = "";

    public ImgUp() {
    }

    public ImgUp(File f) {
        this.f = f;
        initComponents();
        path = f.getAbsolutePath();
        Icon i = new javax.swing.ImageIcon(path);

        jLabel1.setSize(i.getIconWidth(), i.getIconHeight());
        jLabel1.setIcon(i);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        pack();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kép tallózása");
        getContentPane().setLayout(new java.awt.GridLayout());

        jButton1.setText("Ment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Másik választása");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(698, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //ini file mentése ha nincs
        //beolvassuk az ini file-t
        String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
        File file = new File(filepath + "PlannTool.ini");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {

        }
        String line = null;
        ArrayList<String> sorok = new ArrayList<>();
//olvassuk a filet
        try {
            while ((line = br.readLine()) != null) {

                sorok.add(line);

            }
        } catch (Exception ex) {
        }
        try {
            br.close();
        } catch (Exception ex) {
        }

//kicseréljuk az animationnal kezdodo sort 
        for (int i = 0; i < sorok.size(); i++) {

            if (sorok.get(i).contains("animation")) {

                sorok.remove(i);

            }

        }
        sorok.add("animation: " + path);

//kiirjuk a filet
        file = new File(filepath + "PlannTool.ini");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ImgUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException ex) {
            Logger.getLogger(ImgUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = new BufferedWriter(writer);

        for (int i = 0; i < sorok.size(); i++) {

            try {
                bw.write(sorok.get(i));
                bw.newLine();
            } catch (IOException ex) {
                Logger.getLogger(ImgUp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ImgUp.class.getName()).log(Level.SEVERE, null, ex);
        }

        CreateDefaultIcon();
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // másik kép választása

        JFileChooser chooser = CTB_Filechooser.getFileChooserRiport();
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        chooser.setFileFilter(imageFilter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            path = file.getAbsolutePath();
            Icon i = new javax.swing.ImageIcon(path);

            jLabel1.setSize(i.getIconWidth(), i.getIconHeight());
            jLabel1.setIcon(i);
            jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
            pack();

//            ImgUp u = new ImgUp(file);
//            u.setVisible(true);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    public void CreateDefaultIcon() {

        //megprobaljuk beolvasni az ini filet , és beállítani a default icont
        String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
        File file = new File(filepath + "PlannTool.ini");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {

            ablak.defaulticon = new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/planning.gif"));

        }
        String line = null;
        ArrayList<String> sorok = new ArrayList<>();
//olvassuk a filet
        try {
            while ((line = br.readLine()) != null) {

                sorok.add(line);

            }
        } catch (Exception ex) {
        }
        try {
            br.close();
        } catch (Exception ex) {
        }

        //megkeressük azt a sort amiben az icon elérési utja van
        for (int i = 0; i < sorok.size(); i++) {

            if (sorok.get(i).contains("animation")) {
                try {
                    ablak.defaulticon = new javax.swing.ImageIcon(sorok.get(i).substring(11, sorok.get(i).length()));
                } catch (Exception e) {

                    ablak.defaulticon = new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/planning.gif"));
                }

            }

        }

        if (ablak.defaulticon == null || ablak.defaulticon.getIconHeight() < 1) {

            ablak.defaulticon = new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/planning.gif"));

        }

    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ImgUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ImgUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ImgUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ImgUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
//////                new ImgUp().setVisible(true);
////            }
////        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
