/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.jTable12;
import PlannTool.infobox;
import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.table.DefaultTableModel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_EditLostQuantity extends javax.swing.JDialog {

    /**
     * Creates new form CTB_NEW_ExportShorty
     */
    private static int xx = 0;
    private static int yy = 0;
    CTB c;

    public CTB_NEW_EditLostQuantity(java.awt.Frame parent, boolean modal, CTB c) {
        super(parent, modal);
        this.c = c;
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        initComponents();
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
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setOpacity(0.8F);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Edit Lost Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe Script", 1, 12))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("X");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jLabel2.setText("PartNumber:");

        jTextField2.setBackground(new java.awt.Color(204, 204, 204));
        jTextField2.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jTextField2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel3.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jLabel3.setText("Mennyiség:");

        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jTextArea1.setBackground(new java.awt.Color(204, 204, 204));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane1.setViewportView(jTextArea1);

        jLabel5.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jLabel5.setText("Komment:");

        jButton1.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N
        jButton1.setText("Ment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(90, 90, 90)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jTextField1))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        xx = evt.getX();
        yy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        //egermozog
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - yy);
    }//GEN-LAST:event_formMouseDragged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // mentés gomb

        //megszuntetjuk a tabla szerkeszteset
        if (CTB.jTable9.isEditing()) {

            CTB.jTable9.getCellEditor().stopCellEditing();
        }
        //be kell szettelni az adatokat a lost táblába
        DefaultTableModel lostmodel = new DefaultTableModel();
        lostmodel = (DefaultTableModel) CTB.jTable12.getModel();
        boolean letezik = false;

        //bejarjuk és megkeressuk a pn-t
        for (int i = 0; i < lostmodel.getRowCount(); i++) {

            if (jTextField2.getText().equals(lostmodel.getValueAt(i, 0).toString())) {
                //ha megvan akkor beállítjuk az adott sorba az uj értékeket
                try {
                    lostmodel.setValueAt(Integer.parseInt(jTextField1.getText()), i, 1);
                    lostmodel.setValueAt(jTextArea1.getText(), i, 2);
                    CTB.jTable12.setModel(lostmodel);
                    letezik = true;

                    //kiugrunk
                    break;

                } catch (Exception e) {

                    c.warning.SetMessage("Nem számot adtál meg mennyiségnek!");
                    jTextField1.requestFocus();
                    jTextField1.setText("");

                }

            }

        }

        //ha false maradt a létezik akkor kell egy uj sort hozzáadni a modellhez
        if (!letezik) {

            String comment = "";
            try {
                comment = jTextArea1.getText();
            } catch (Exception e) {
            }

            lostmodel.addRow(new Object[]{jTextField2.getText(), jTextField1.getText(), comment});
            jTable12.setModel(lostmodel);

        }

        //elmentjük az egész cuccost----------------------------------------------------------------------------------------------------------------------
        try {

            //bejárjuk a tábla adatait és kiirjuk a fileba
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable12.getModel();

            //megprobáljuk számmá convertálni a qty -t ha nem sikerül kiirjuk és kiállunk
            for (int i = 0; i < model.getRowCount(); i++) {

                if (!model.getValueAt(i, 0).toString().equals("")) {

                    try {
                        Integer.parseInt(model.getValueAt(i, 1).toString());
                    } catch (Exception e) {

                        c.warning.SetMessage("Nem jó számot adtál meg a " + String.valueOf(i + 1) + ". sorban!");
                        CTB_LostRead r = new CTB_LostRead();
                        r.olvas();
                        return;
                    }

                }
            }

            // lost file mentése
            String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
            File file = new File(filepath + "CTB_Lost.ini");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);

            for (int i = 0; i < model.getRowCount(); i++) {
                //ha üres a pn nem irjuk ki azt a sort
                if (model.getValueAt(i, 0).equals("")) {

                    continue;

                }

                bw.write(model.getValueAt(i, 0) + ";" + model.getValueAt(i, 1) + ";" + model.getValueAt(i, 2));
                bw.newLine();

            }

            bw.close();
            //visszaolvassuk
            CTB_LostRead r = new CTB_LostRead();
            r.olvas();

        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }

        CTB_NEW_FullCalc calc = new CTB_NEW_FullCalc(true, CTB_NEW_FullCalc.calculations.CHANGELOST);
        calc.start();

        jTextArea1.setText("");
        jTextField1.setText("");
        jTextField2.setText("");

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CTB_NEW_EditLostQuantity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CTB_NEW_EditLostQuantity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CTB_NEW_EditLostQuantity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CTB_NEW_EditLostQuantity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                CTB_NEW_EditLostQuantity dialog = new CTB_NEW_EditLostQuantity(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}