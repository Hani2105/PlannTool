/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static javax.print.attribute.standard.MediaPrintableArea.MM;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import org.joda.time.DateTime;
import static org.joda.time.DateTime.now;
import org.joda.time.format.DateTimeFormat;
import static org.joda.time.format.ISODateTimeFormat.date;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_DataLoader extends javax.swing.JFrame {

    /**
     * Creates new form Tc_DataLoader
     */
    Tc_Besheet b;

    public Tc_DataLoader(Tc_Besheet b) {
        this.b = b;
        initComponents();
        mindenjob();
    }

    public void mindenjob() {

        //bejárjuk a b táblát
        String job = null;
        String pn = null;
        String ws = null;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();
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

                for (int n = 0; n < jTable2.getRowCount(); n++) {

//ha megtaláljuk ugyan azt a job pn és ws kombot akkor false-ra állítjuk
                    if (jTable2.getValueAt(n, 0).equals(job) && jTable2.getValueAt(n, 1).equals(pn) && jTable2.getValueAt(n, 2).equals(ws)) {

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
        jTable2.setModel(model);

    }

    public void notreleasedjob() {

        //bejárjuk a b táblát
        String job = null;
        String pn = null;
        String ws = null;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();
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

                for (int n = 0; n < jTable2.getRowCount(); n++) {

//ha megtaláljuk ugyan azt a job pn és ws kombot akkor false-ra állítjuk
                    if (jTable2.getValueAt(n, 0).equals(job) && jTable2.getValueAt(n, 1).equals(pn) && jTable2.getValueAt(n, 2).equals(ws)) {

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
        jTable2.setModel(model);

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
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DataLoader");
        setAlwaysOnTop(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JOB", "TAB", "TAB", "PN", "TAB", "TAB", "QTY", "TAB", "RELEASED", "TAB", "DATETIME", "*DN"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JOB", "PN", "Állomás", "Darabszám", "Betölt?"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(5);
        }

        jCheckBox1.setText("Összes kijelölése");
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Nyitott JOB-ok elrejtése");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jCheckBox2)
                        .addGap(153, 153, 153)
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 866, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

        //vegigmegyünk a táblán és azzal foglalkozunk ami ki van jelölve de előtte kitakaritjuk tábla 1 et
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);
        //dateformat a megfelelő formátumért

        DateTime now = new org.joda.time.DateTime();
        String pattern = "dd-MMM-yyyy 00:00:00";
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String most = formatter.print(now);

        for (int i = 0; i < jTable2.getRowCount(); i++) {

            if ((boolean) (jTable2.getValueAt(i, 4))) {

                model.addRow(new Object[]{jTable2.getValueAt(i, 0).toString(), "TAB", "TAB", jTable2.getValueAt(i, 1).toString(), "TAB", "TAB", jTable2.getValueAt(i, 3).toString(), "TAB", "Released", "TAB", most, "*DN"});

            }

        }

        jTable1.setModel(model);

    }//GEN-LAST:event_jTable2MouseClicked

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:

        if (jCheckBox2.isSelected()) {

            notreleasedjob();

        } else {

            mindenjob();

        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // összes kijelölése

        if (jCheckBox1.isSelected()) {
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                jTable2.setValueAt(true, i, 4);
            }

        }
        if (jCheckBox1.isSelected() == false) {
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                jTable2.setValueAt(false, i, 4);
            }

        }

        //vegigmegyünk a táblán és azzal foglalkozunk ami ki van jelölve de előtte kitakaritjuk tábla 1 et
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();

        model.setRowCount(0);
        //dateformat a megfelelő formátumért

        DateTime now = new org.joda.time.DateTime();
        String pattern = "dd-MMM-yyyy 00:00:00";
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String most = formatter.print(now);

        for (int i = 0; i < jTable2.getRowCount(); i++) {

            if ((boolean) (jTable2.getValueAt(i, 4))) {

                model.addRow(new Object[]{jTable2.getValueAt(i, 0).toString(), "TAB", "TAB", jTable2.getValueAt(i, 1).toString(), "TAB", "TAB", jTable2.getValueAt(i, 3).toString(), "TAB", "Released", "TAB", most, "*DN"});

            }

        }

        jTable1.setModel(model);


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
            java.util.logging.Logger.getLogger(Tc_DataLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_DataLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
