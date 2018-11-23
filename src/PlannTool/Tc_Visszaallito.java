/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;
import static PlannTool.Tc_Betervezo.jComboBox1;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import static PlannTool.Tc_Betervezo.Tervezotabbed;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Visszaallito extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Visszaallito
     */
    String tol = "";
    String ig = "";
    String sheetname = "";

    public Tc_Visszaallito(String sheetname, String tol, String ig) {
        initComponents();
        this.tol = tol;
        this.ig = ig;
        this.sheetname = sheetname;
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Active", "Mentés dátuma"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        jButton1.setText("Visszaállít");
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(0, 19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //visszaallitjuk a kivalsztott idore
        // ami jelenleg kettes az hármas lesz
        String query = "update tc_terv left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells set active = '3' where tc_terv.active = '2' and tc_becells.cellname = '" + sheetname + "' and tc_terv.date between '" + tol + "' and '" + ig + "'";
        //vegrehajtjuk
        planconnect pc = new planconnect();
        pc.feltolt(query, false);

        //a kivalasztott kettes lesz
        String updatequery = "update tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "set tc_terv.active = '2' \n"
                + "where tc_terv.active = '" + jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "'  and tc_becells.cellname = '" + sheetname + "'";
        pc.feltolt(updatequery, false);

        //a nullas torlodik
        String deletequery = "delete from tc_terv where tc_terv.active = 0 and tc_terv.idtc_becells = '" + sheetname + "'";
        pc.feltolt(deletequery, false);

        //az egyes nullas lesz
        updatequery = "update tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "set tc_terv.active = '0' \n"
                + "where tc_terv.active = 1 and tc_terv.date between '" + tol + "' and '" + ig + "' and tc_becells.cellname = '" + sheetname + "'";
        pc.feltolt(updatequery, false);

        //a 3 as 1 es lesz
        updatequery = "update tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "set tc_terv.active = '1' \n"
                + "where tc_terv.active = 3 and tc_terv.date between '" + tol + "' and '" + ig + "' and tc_becells.cellname = '" + sheetname + "'";
        pc.feltolt(updatequery, false);
        
        
        //lekerjuk az oldal adatai ujbol
        
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

// terv lekérése
        Besheets.clear();
        for (int i = 0; i < Tervezotabbed.getTabCount(); i++) {

            String name = Tervezotabbed.getTitleAt(i);

            Besheets.put(name, (Tc_Besheet) Tervezotabbed.getComponentAt(i));

        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String first = "";
        String second = "";

        try {
            first = df.format(Tc_Betervezo.jDateChooser1.getDate());
            second = df.format(Tc_Betervezo.jDateChooser2.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date one = null;
        Date two = null;
        int napok = 0;
        if (!first.equals("") && !second.equals("")) {
            try {
                one = df.parse(first);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                two = df.parse(second);
            } catch (ParseException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Tc_Napszamolo nap = new Tc_Napszamolo();
        if (!first.equals("") && !second.equals("")) {
            napok = nap.daysBetweenUsingJoda(one, two);
        }

        //System.out.println(napok);
        //hozzaadjuk a napok es a muszakhossznak megfelelo oszlopok szamat a tablahoz
        int n = Tervezotabbed.getSelectedIndex();
        String neve = Tervezotabbed.getTitleAt(n);

        //kitoroljuk az oszlopokat
        DefaultTableModel model = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
        model.setColumnCount(4);

        //oszlopok neve a datumbol
        Calendar c = Calendar.getInstance();
        c.setTime(Tc_Betervezo.jDateChooser1.getDate());
        Date dt = new Date();
        dt = c.getTime();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        org.joda.time.format.DateTimeFormatter fmtnap = DateTimeFormat.forPattern("E");

        DateTime dtOrg = new DateTime(dt);
        String columneve = "";
        String szak = "";
        String napneve = "";
        TableColumn column = null;

        //napok szamaszor futtatjuk
        for (int i = 0; i < napok; i++) {

            //ha 12 órás a műszakrend 2 szer
            if (jComboBox1.getSelectedIndex() == 0) {
                for (int k = 0; k < 2; k++) {

                    szak = (k == 0) ? " 06:00" : " 18:00";
                    columneve = fmt.print(dtOrg.plusDays(i)) + szak;

                    napneve = fmtnap.print(dtOrg.plusDays(i));

                    model.addColumn(columneve + " " + napneve);

                }
            }

            //ha 8 órás 3 szor
            if (jComboBox1.getSelectedIndex() == 1) {
                for (int k = 0; k < 3; k++) {

                    if (k == 0) {

                        szak = " 06:00";

                    } else if (k == 1) {

                        szak = " 14:00";
                    } else {

                        szak = " 22:00";

                    }
                    columneve = fmt.print(dtOrg.plusDays(i)) + szak;
                    napneve = fmtnap.print(dtOrg.plusDays(i));
                    model.addColumn(columneve + " " + napneve);

                }
            }

        }
        //col szelesseg allitas

//        for (int i = 0; i < Besheets.get(neve).jTable2.getModel().getColumnCount(); i++) {
//
//            if (i != 3) {
//                column = Besheets.get(neve).jTable2.getColumnModel().getColumn(i);
//                column.setPreferredWidth(130);
//            }
//
//        }
        //lekerdezzuk az adatbazis adatokat
        String Query = "select tc_terv.date , tc_bepns.partnumber , tc_terv.job , tc_bestations.workstation , tc_terv.qty , tc_terv.tt \n"
                + "from tc_terv \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns \n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_terv.idtc_bestations\n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "where tc_terv.date between '" + fmt.print(dtOrg) + " 06:00:00" + "' and '" + columneve + ":00" + "' and tc_terv.active = 2 and tc_becells.cellname = '" + neve + "'  \n"
                + "order by tc_terv.date , tc_terv.wtf";

        //feldolgozzuk az eredmenyt
       pc = new planconnect();
        try {
            pc.planconnect(Query);
            model.setRowCount(0);
            int r = 0;
            String terv = "";

            //vegigporgetjuk a resultsetet
            while (pc.rs.next()) {

                //porgetjuk az oszlopokat
                for (int i = 4; i < model.getColumnCount(); i++) {

                    //h egyezik a query datuma az oszlop datumaval akkor 
                    if (pc.rs.getString(1).equals(model.getColumnName(i).substring(0, model.getColumnName(i).length() - 4) + ":00.0")) {

                        // hozzaadunk egy terv vagy teny sort
                        if (pc.rs.getString(6).equals("0")) {

                            terv = "Terv";
                        } else {
                            terv = "Tény";
                        }
                        model.addRow(new Object[]{pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), terv});
                        model.setValueAt(pc.rs.getString(5), r, i);
                        r++;

                    }

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        Besheets.get(neve).jTable2.setModel(model);
        Tc_Calculator calc = new Tc_Calculator(Besheets.get(neve));
        this.setVisible(false);


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
            java.util.logging.Logger.getLogger(Tc_Visszaallito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Visszaallito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Visszaallito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Visszaallito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Tc_Visszaallito().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
