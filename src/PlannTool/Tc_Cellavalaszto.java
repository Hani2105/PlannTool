/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Cellavalaszto extends javax.swing.JFrame {

    Tc_Betervezo bt;
    public static String neve;

    public Tc_Cellavalaszto(Tc_Betervezo b) throws SQLException, ClassNotFoundException {
        initComponents();

        bt = b;
        //lekerdezzuk az adatbazisbol a cellakat
        planconnect pc = new planconnect();
        String query = "SELECT * FROM planningdb.tc_becells;";
        ResultSet rs = (ResultSet) pc.planconnect(query);
        DefaultListModel lm1 = new DefaultListModel();
        DefaultListModel lm2 = new DefaultListModel();
        lm1.removeAllElements();
        lm2.removeAllElements();
        jList1.setModel(lm1);
        jList2.setModel(lm2);
        jComboBox1.requestFocus();

        while (rs.next()) {

            lm1.addElement(rs.getString(2));

        }

        jList1.setModel(lm1);

        // lekerdezzuk a felhasznalokat es betesszuk a comboboxba
        query = "select * from tc_users order by username asc";
        pc.planconnect(query);

        while (pc.rs.next()) {

            jComboBox1.addItem(pc.rs.getString(2));

        }

        jComboBox1.setSelectedIndex(-1);

//        Besheet sheet = new Besheet();
//        bt.jTabbedPane1.add("valami", sheet);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cella választó");
        setAlwaysOnTop(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setOpaque(false);

        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 170, 310));

        jScrollPane2.setOpaque(false);

        jList2.setToolTipText("");
        jScrollPane2.setViewportView(jList2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, 170, 310));

        jButton1.setText("-->");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        jButton2.setText("<--");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, -1, -1));

        jButton3.setText("Cellák megnyitása");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton3KeyPressed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 130, -1));

        jComboBox1.setOpaque(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 190, 30));

        jLabel1.setText("Felhasználó:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jButton4.setText("Mentés");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 70, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/cella.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 200, 300));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        String querybe = "";
        String querysorrend = "";
        DefaultListModel lm2 = new DefaultListModel();
        lm2 = (DefaultListModel) jList2.getModel();
        //osszerakjuk hogy milyen allomasok vannak a jlistben
        for (int i = 0; i < lm2.size(); i++) {

            querybe += "'" + lm2.getElementAt(i) + "',";
            querysorrend += lm2.getElementAt(i) + ",";

        }
        querybe = querybe.substring(0, querybe.length() - 1);
        querysorrend = querysorrend.substring(0, querysorrend.length() - 1);
        //lekerdezzuk az idjukat
        String query = "select tc_becells.idtc_cells from tc_becells where tc_becells.cellname in (" + querybe + ") order by find_in_set(tc_becells.cellname,'" + querysorrend + "')";

        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);
            //felhasznalato formava alakitjuk az update queryhez
            querybe = "";
            while (pc.rs.next()) {

                querybe += pc.rs.getString(1) + ",";

            }

            querybe = querybe.substring(0, querybe.length() - 1);

            //updatelunk
            query = "update tc_users set tc_users.cellaids = '" + querybe + "' where tc_users.username = '" + jComboBox1.getSelectedItem().toString() + "'";
            try {
                pc.feltolt(query, false);

                //infobox inf = new infobox();
                //inf.infoBox("Sikeres mentés!", "Mentés");
            } catch (Exception e) {
                //infobox inf = new infobox();
                //inf.infoBox("Sikertelen mentés!", "Mentés");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.dispose();


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        DefaultListModel lm2 = new DefaultListModel();
        if (jComboBox1.getSelectedIndex() != -1) {
            String query = "select tc_users.cellaids from tc_users where tc_users.username = '" + jComboBox1.getSelectedItem().toString() + "'";
            //System.out.println(query);

            String cellak = "";
            planconnect pc = new planconnect();
            try {
                pc.planconnect(query);

                while (pc.rs.next()) {

                    cellak = pc.rs.getString(1);
                    //System.out.println(cellak);

                }

            } catch (SQLException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            }

            //feldaraboljuk az rs eredmenyet
            String[] cellaktomb = cellak.split(",");

            //atalakitjuk hasznalhato formava a queryhez
            String querybe = "";
            String querysorrend = "";

            for (int i = 0; i < cellaktomb.length; i++) {

                querybe += "'" + cellaktomb[i].toString() + "',";
                querysorrend += cellaktomb[i].toString() + ",";

            }
            querybe = querybe.substring(0, querybe.length() - 1);
            querysorrend = querysorrend.substring(0, querysorrend.length() - 1);

            //osszeqallitjuk a queryt
            query = "SELECT tc_becells.cellname from tc_becells where tc_becells.idtc_cells in (" + querybe + ") order by find_in_set(tc_becells.idtc_cells,'" + querysorrend + "')";

            try {
                //lekerdezzuk

                pc.planconnect(query);
                while (pc.rs.next()) {
                    lm2.addElement(pc.rs.getString(1));
                }

                jList2.setModel(lm2);

            } catch (SQLException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            lm2.removeAllElements();
            jList2.setModel(lm2);
        }


    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
        // TODO add your handling code here:

        // TODO add your handling code here:
        //lekérjük a sheeteket
        Tc_Betervezo.Besheets.clear();
        for (int i = 0; i < jList2.getModel().getSize(); i++) {

            Tc_Besheet sheet = null;
            try {
                sheet = new Tc_Besheet(bt);
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            }
            bt.Tervezotabbed.addTab(jList2.getModel().getElementAt(i), sheet);
            neve = jList2.getModel().getElementAt(i);
            Tc_Betervezo.Besheets.put(jList2.getModel().getElementAt(i), sheet);
            try {
                sheet.parts();
                sheet.workstations();
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        bt.jButton1.setFocusable(false);
        bt.jButton3.requestFocus();

        this.setVisible(false);

    }//GEN-LAST:event_jButton3KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        Tc_CellavalsztoSzal c = new Tc_CellavalsztoSzal(this);
        c.start();

        animation a = new animation();
        a.start();


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        DefaultListModel lm2 = new DefaultListModel();
        lm2 = (DefaultListModel) jList2.getModel();

        if (this.jList2.getSelectedIndices().length > 0) {
            int[] selectedIndices = jList2.getSelectedIndices();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                lm2.removeElementAt(selectedIndices[i]);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DefaultListModel lm2 = new DefaultListModel();
        lm2 = (DefaultListModel) jList2.getModel();
        for (int i = 0; i < jList1.getModel().getSize(); i++) {

            if (jList1.isSelectedIndex(i)) {

                lm2.addElement(jList1.getModel().getElementAt(i));

            }

        }

        jList2.setModel(lm2);
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
            java.util.logging.Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Cellavalaszto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new cellavalaszto().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new cellavalaszto().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new cellavalaszto().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new cellavalaszto().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    public static javax.swing.JList<String> jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
