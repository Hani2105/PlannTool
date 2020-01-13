/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import static PlannTool.BACKEND.Tc_Adatkitolto.jTable1;
import static PlannTool.BACKEND.Tc_Adatkitolto.jTable2;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_DataLoaderDialog extends javax.swing.JDialog {

    /**
     * Creates new form Tc_DataLoaderDialog
     */
    Tc_Besheet b;

    public Tc_DataLoaderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void mindenjob() {

        //bejárjuk a b táblát
        String job = null;
        String pn = null;
        String ws = null;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            int darab = 0;
            try {
                job = b.jTable2.getValueAt(i, 1).toString();
            } catch (Exception e) {
            }

            try {
                pn = b.jTable2.getValueAt(i, 0).toString();
            } catch (Exception e) {
            }

            try {
                ws = b.jTable2.getValueAt(i, 2).toString();
            } catch (Exception e) {
            }
//ha nem null a job sem a pn és nem is üres és terv sorban vagyunk
            if (job != null && !job.equals("") && pn != null && b.jTable2.getValueAt(i, 3).equals("Terv") && ws != null) {
//bejarjuk a kistablat , hogy kell e vele foglalkozni
                boolean foglalkozni = true;

                for (int n = 0; n < jTable1.getRowCount(); n++) {

//ha megtaláljuk ugyan azt a job pn és ws kombot akkor false-ra állítjuk
                    if (jTable1.getValueAt(n, 0).equals(job) && jTable1.getValueAt(n, 1).equals(pn) && jTable1.getValueAt(n, 2).equals(ws)) {

                        foglalkozni = false;

                    }

                }
//ha kell vele foglalkozni akkor bejárjuk a nagy táblát és összeadjuk a darabokat kivéve a summa oszlopot
                if (foglalkozni) {
                    for (int r = 0; r < b.jTable2.getRowCount(); r++) {
//ha olyan sorban vagyunk ahol egyezik a job , pn és ws

                        try {
                            if (b.jTable2.getValueAt(r, 1).toString().equals(job) && b.jTable2.getValueAt(r, 0).toString().equals(pn) && b.jTable2.getValueAt(r, 2).toString().equals(ws) && b.jTable2.getValueAt(r, 3).toString().equals("Terv")) {
//vegigmegyunk az oszes oszlopon is
                                for (int o = 4; o < b.jTable2.getColumnCount(); o++) {
//ha van valami az adott cellában ráuszítjuk az érték kiszedőt és hozzáadjuk a databhoz
                                    if (b.jTable2.getValueAt(r, o) != null && !b.jTable2.getColumnName(o).equals("Sum: PN,JOB,WS")) {

                                        Tc_Stringbolint stb = new Tc_Stringbolint(b.jTable2.getValueAt(r, o).toString());

                                        darab += stb.db;

                                    }

                                }

                            }
                        } catch (Exception e) {
                        }

                    }
                    //hozzáadjuk a modelhez az adatokat

                    model.addRow(new Object[]{job, pn, ws, darab, false});

                }

            }

        }

//beállítjuk a táblát
        jTable1.setModel(model);

    }

    public void notreleasedjob() {

        //bejárjuk a b táblát
        String job = null;
        String pn = null;
        String ws = null;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            int darab = 0;
            try {
                job = b.jTable2.getValueAt(i, 1).toString();
            } catch (Exception e) {
            }

            try {
                pn = b.jTable2.getValueAt(i, 0).toString();
            } catch (Exception e) {
            }

            try {
                ws = b.jTable2.getValueAt(i, 2).toString();
            } catch (Exception e) {
            }
//ha nem null a job sem a pn és nem is üres és terv sorban vagyunk

//megnezzuk a jobadatok kozott az aktualis job ot , hogy mi a statusza
            boolean release = false;
            try {
                for (int n = 0; n < b.jobadat.get(0).length; n++) {

                    try {
                        if (b.jobadat.get(0)[n][0].equals(b.jTable2.getValueAt(i, 1).toString())) {

                            release = true;
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
            if (job != null && !job.equals("") && pn != null && b.jTable2.getValueAt(i, 3).equals("Terv") && ws != null && !release) {
//bejarjuk a kistablat , hogy kell e vele foglalkozni
                boolean foglalkozni = true;

                for (int n = 0; n < jTable1.getRowCount(); n++) {

//ha megtaláljuk ugyan azt a job pn és ws kombot akkor false-ra állítjuk
                    if (jTable1.getValueAt(n, 0).equals(job) && jTable1.getValueAt(n, 1).equals(pn) && jTable1.getValueAt(n, 2).equals(ws)) {

                        foglalkozni = false;

                    }

                }
//ha kell vele foglalkozni akkor bejárjuk a nagy táblát és összeadjuk a darabokat kivéve a summa oszlopot
                if (foglalkozni) {
                    for (int r = 0; r < b.jTable2.getRowCount(); r++) {
//ha olyan sorban vagyunk ahol egyezik a job , pn és ws

                        try {
                            if (b.jTable2.getValueAt(r, 1).toString().equals(job) && b.jTable2.getValueAt(r, 0).toString().equals(pn) && b.jTable2.getValueAt(r, 2).toString().equals(ws) && b.jTable2.getValueAt(r, 3).toString().equals("Terv")) {
//vegigmegyunk az oszes oszlopon is
                                for (int o = 4; o < b.jTable2.getColumnCount(); o++) {
//ha van valami az adott cellában ráuszítjuk az érték kiszedőt és hozzáadjuk a databhoz
                                    if (b.jTable2.getValueAt(r, o) != null && !b.jTable2.getColumnName(o).equals("Sum: PN,JOB,WS")) {

                                        Tc_Stringbolint stb = new Tc_Stringbolint(b.jTable2.getValueAt(r, o).toString());

                                        darab += stb.db;

                                    }

                                }

                            }
                        } catch (Exception e) {
                        }

                    }
                    //hozzáadjuk a modelhez az adatokat

                    model.addRow(new Object[]{job, pn, ws, darab, false});

                }

            }

        }

//beállítjuk a táblát
        jTable1.setModel(model);

    }

    @Override
    public void setVisible(boolean v) {
        super.setVisible(v);

        b = Tc_Betervezo.Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()));
        mindenjob();
                

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Loader"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JOB", "PartNumber", "Állomás", "Darabszám", "Betölt?"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(100);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setMinWidth(100);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
        }

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JOB", "TAB", "TAB", "PN", "TAB", "TAB", "QTY", "TAB", "RELEASE", "TAB", "DATETIME", "*DN"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jCheckBox1.setText("Nyitott JOB -ok elrejtése");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Összes kijelölése");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1178, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //vegigmegyünk a táblán és azzal foglalkozunk ami ki van jelölve de előtte kitakaritjuk tábla 1 et
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();

        model.setRowCount(0);
        //dateformat a megfelelő formátumért

        DateTime now = new org.joda.time.DateTime();
        String pattern = "dd-MMM-yyyy 00:00:00";
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String most = formatter.print(now);

        for (int i = 0; i < jTable1.getRowCount(); i++) {

            if ((boolean) (jTable1.getValueAt(i, 4))) {

                model.addRow(new Object[]{jTable1.getValueAt(i, 0).toString(), "TAB", "TAB", jTable1.getValueAt(i, 1).toString(), "TAB", "TAB", jTable1.getValueAt(i, 3).toString(), "TAB", "Released", "TAB", most, "*DN"});

            }

        }

        jTable2.setModel(model);


    }//GEN-LAST:event_jTable1MouseClicked

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        // összes kijelölése

        if (jCheckBox2.isSelected()) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                jTable1.setValueAt(true, i, 4);
            }

        }
        if (jCheckBox2.isSelected() == false) {
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                jTable1.setValueAt(false, i, 4);
            }

        }

        //vegigmegyünk a táblán és azzal foglalkozunk ami ki van jelölve de előtte kitakaritjuk tábla 1 et
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();

        model.setRowCount(0);
        //dateformat a megfelelő formátumért

        DateTime now = new org.joda.time.DateTime();
        String pattern = "dd-MMM-yyyy 00:00:00";
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String most = formatter.print(now);

        for (int i = 0; i < jTable1.getRowCount(); i++) {

            if ((boolean) (jTable1.getValueAt(i, 4))) {

                model.addRow(new Object[]{jTable1.getValueAt(i, 0).toString(), "TAB", "TAB", jTable1.getValueAt(i, 1).toString(), "TAB", "TAB", jTable1.getValueAt(i, 3).toString(), "TAB", "Released", "TAB", most, "*DN"});

            }

        }

        jTable2.setModel(model);

    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if (jCheckBox1.isSelected()) {

            notreleasedjob();

        } else {

            mindenjob();

        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Tc_DataLoaderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoaderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoaderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoaderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tc_DataLoaderDialog dialog = new Tc_DataLoaderDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
