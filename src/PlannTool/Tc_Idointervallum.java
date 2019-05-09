/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.c;
import static PlannTool.Tc_Betervezo.first;
import static PlannTool.Tc_Betervezo.one;
import static PlannTool.Tc_Betervezo.second;
import static PlannTool.Tc_Betervezo.two;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Idointervallum extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Időintervallum
     */
    public Tc_Idointervallum() {
        initComponents();
        
        jDateChooser1.setDate(one);
        jDateChooser2.setDate(two);
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
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Idő beállítások");
        setAlwaysOnTop(true);

        jDateChooser1.setMinimumSize(new java.awt.Dimension(56, 10));
        jDateChooser1.setPreferredSize(new java.awt.Dimension(119, 12));

        jDateChooser2.setMinimumSize(new java.awt.Dimension(56, 10));
        jDateChooser2.setPreferredSize(new java.awt.Dimension(119, 12));

        jLabel1.setText("Ezen időközön:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12 órás", "8 órás" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Műszakrend:");

        jButton1.setText("Lekér / Ment");
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
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // az időintervallum mentése
        Tc_Betervezo.comboertek = jComboBox1.getSelectedIndex();
        //meghatarozzuk a napokat , mekkora intervallumra kell beallitani a sheeteket
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Tc_Betervezo.first = df.format(jDateChooser1.getDate());
            Tc_Betervezo.second = df.format(jDateChooser2.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!first.equals("") && !second.equals("")) {
            try {
                Tc_Betervezo.one = df.parse(first);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Tc_Betervezo.two = df.parse(second);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Tc_Betervezo.c.setTime(jDateChooser1.getDate());

        //lefuttatjuk az össztervlekért
        //eltesszuk az adatokat az ellenorzeshez , hogy valtozott e a terv
        Tc_Tervvaltozasellenor.tervellenor.clear();
        Tc_Tervvaltozasellenor t = new Tc_Tervvaltozasellenor();
        try {
            t.leker();
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Besheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Besheet.class.getName()).log(Level.SEVERE, null, ex);
        }

        //ciklust indítunk és végrehajtjuk a lekért cellákon a dátum beállítást
        //bepakoljuk a maptree be a sheeteket ujból
        Tc_Betervezo.Besheets.clear();
        for (int i = 0; i < Tc_Betervezo.Tervezotabbed.getTabCount(); i++) {

            String name = Tc_Betervezo.Tervezotabbed.getTitleAt(i);

            Tc_Betervezo.Besheets.put(name, (Tc_Besheet) Tc_Betervezo.Tervezotabbed.getComponentAt(i));

        }

        int napok = 0;

        Tc_Napszamolo nap = new Tc_Napszamolo();
        if (!first.equals("") && !second.equals("")) {
            napok = nap.daysBetweenUsingJoda(one, two);
        }

        //oszlopok neve a datumbol
        Date dt = new Date();
        dt = c.getTime();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        org.joda.time.format.DateTimeFormatter fmtnap = DateTimeFormat.forPattern("E");

//letrehozunk egy megfelelo jtablet
//most indítjuk a nagy ciklust amiben végigpörgetjük a sheeteket
        for (int b = 0; b < Tc_Betervezo.Tervezotabbed.getTabCount(); b++) {

            Tc_Leker leker = new Tc_Leker(Tc_Betervezo.Tervezotabbed.getTitleAt(b), "groupleker");

        }

        Tc_Calculator calc = new Tc_Calculator(Tc_Betervezo.Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())), false, 0);
        calc.run();

        this.dispose();

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
            java.util.logging.Logger.getLogger(Tc_Idointervallum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Idointervallum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Idointervallum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Idointervallum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tc_Idointervallum().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public static javax.swing.JComboBox<String> jComboBox1;
    public static com.toedter.calendar.JDateChooser jDateChooser1;
    public static com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
