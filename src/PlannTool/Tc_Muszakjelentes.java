/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.ablak.jTabbedPane1;
import static PlannTool.ablak.stat;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Muszakjelentes extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Muszakjelentes
     */
    Tc_Besheet b;
    String ellenorzoadat = "";
    String Email = "";
    String subject = "";

    public Tc_Muszakjelentes(Tc_Besheet b) throws SQLException, ClassNotFoundException {
        initComponents();
        this.b = b;
        //behuzzuk a cimlistat

        String query = "SELECT Muszakjelentes.Cím FROM planningdb.Muszakjelentes";
        planconnect pc = new planconnect();

        pc.planconnect(query);

        //cimlista string
        String cimlista = "";

        while (pc.rs.next()) {

            cimlista += pc.rs.getString(1) + ",\n";
        }
        
        pc.kinyir();

        //levagjuk az utolso biszbaszt
        cimlista = cimlista.substring(0, cimlista.length() - 1);
        //beallitjuk a műszakjelentés szövegeként
        jTextArea1.setText(cimlista);

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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Műszakjelentés");

        jButton1.setText("Adatok összegyűjtése");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/sendmail.gif"))); // NOI18N
        jButton2.setText("Levél elküldése");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Címlista:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel3.setText("Műszakjelentés");

        jLabel4.setText("Levél tartalma:");

        jScrollPane1.setHorizontalScrollBar(null);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel7.setText("Műszakvezetői komment:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 26)); // NOI18N
        jLabel1.setText("<html>Kattints a megfelelő oszlopba, </br>majd válaszd az adatok összegyűjtése</br> lehetőséget! </html>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel7)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //email kuldese

        //osszeallitjuk a html szoveget
        //osszeszedjuk a cella adatait
        String cellname = Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex());

        //osszeszedjuk az info sorokbol az adatokat
        String hatekonysag = "";

        //osszeszedjuk a ws infokat ws enkent
        String ws = "";
        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            try {
                if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                    ws += "<div style=\"color:green\"> Az állomásra  tervezett terv és tény idő: " + "<span style =\"color:black\">" + b.jTable2.getValueAt(i, 2).toString() + "  " + b.jTable2.getValueAt(i, b.jTable2.getSelectedColumn()) + "</span></div>";

                }
            } catch (Exception e) {

                infobox info = new infobox();
                info.infoBox("Nem választottál ki szakot!", "Hiba!");
                this.dispose();
                return;
            }

        }

        //osszeszedjuk a tervezett es vegrehajtott darabokat
        String terv = "";

        //lekerdezzuk adatbazisbol az adatokat
        String query = "select  tc_bestations.workstation, sum(tc_terv.qty) as sum , sum(tc_terv.qty_teny) as sumteny \n"
                + "from tc_terv \n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_terv.idtc_bestations \n"
                + "where tc_terv.date = '" + b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).substring(0, 16) + "' and tc_terv.idtc_becells = \n"
                + "(select tc_becells.idtc_cells from tc_becells where tc_becells.cellname = '" + cellname + "') and tc_terv.active = 2\n"
                + "group by tc_terv.tt , tc_terv.idtc_bestations order by workstation , tt desc";

        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(Level.SEVERE, null, ex);
        }

        //az osszesitett adatok formazasa , osszeallitasa
        String osszesitett1 = "<tr align=\"center\"><td align=\"center\">Állomás</td><td>Terv</td><td>Tény</td></tr>";
        String osszesitett = "";

        try {
            while (pc.rs.next()) {

                osszesitett += "<tr align=\"center\">" + pc.rs.getString(1) + "</td><td align=\"center\">" + pc.rs.getString(2) + "</td><td align=\"center\">" + pc.rs.getString(3) + "</td></tr>";

            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(Level.SEVERE, null, ex);
        }

        //lekerezzuk a reszletes adatokat
        query = "select tc_bepns.partnumber ,tc_terv.job,  tc_bestations.workstation , tc_terv.qty  , tc_terv.qty_teny \n"
                + "from tc_terv \n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_terv.idtc_bestations \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns\n"
                + "where tc_terv.date = '" + b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).substring(0, 16) + "' and tc_terv.idtc_becells = \n"
                + "(select tc_becells.idtc_cells from tc_becells where tc_becells.cellname = '" + cellname + "') and tc_terv.active = 2\n"
                + "order by tc_terv.wtf, tc_bepns.partnumber , workstation ,   tt desc";

        String reszletes1 = "<tr><td align=\"center\">Partnumber</td><td>JOB</td><td align=\"center\">Állomás</td><td align=\"center\">Terv/Komment</td><td align=\"center\">Tény/Komment</td></tr>";
        String reszletes = "";

        String ellenorzo = "";

        try {
            //osszeallitjuk a stringet az adatokból

            pc.planconnect(query);

            while (pc.rs.next()) {

                ellenorzo += pc.rs.getString(1) + "  " + pc.rs.getString(2) + "  " + pc.rs.getString(3) + " Terv: " + pc.rs.getString(4) + " Tény: " + pc.rs.getString(5) + "\n";
                reszletes += "<tr><td align=\"center\">" + pc.rs.getString(1) + "</td><td align=\"center\">" + pc.rs.getString(2) + "</td><td align=\"center\">" + pc.rs.getString(3) + "</td><td align=\"center\">" + pc.rs.getString(4) + "</td><td align=\"center\">" + pc.rs.getString(5) + "</td></tr>";

            }

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pc.kinyir();

        ellenorzoadat = "Ezek az adatok kerülnek elküldésre! \nMódosításhoz változtasd meg az elmentett adatokat! \n \n" + ellenorzo + "\n\n" + "Műszakvezetői komment:\n";

        jTextArea3.setText(ellenorzoadat);

        Email = "<html>\n"
                + "    <head>\n"
                + "        <title>TODO supply a title</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                //cella neve
                + "<div style=\"color:red;font-size:200%\">" + cellname + " Cella hatékonysága:<br></div>\n"
                //állomások kihasználtsága idő
                + ws
                + "<br>"
                + "<br>"
                + "<div style=\"font-size:200%\">A cella összesítése:</div>"
                + "<table border = \"5\">"
                //ide jön az összesitett queryadat
                + osszesitett1
                + osszesitett
                + "</table>"
                + "<br>"
                + "<div style=\"font-size:200%\">A cella részletei:</div>"
                + "<table border = \"5\">"
                //ide jön a részletes adat stringje
                + reszletes1
                + reszletes
                + "</table>"
                //műszakvezetői komment
                + "<br><br>"
                + "<div style=\"font-size:200%\">Műszakvezetői komment:</div>"
                + "    </body>\n"
                + "</html>";
//osszeallitjuk a targyat
        subject = "Muszakjelentes: " + cellname + " " + b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).substring(0, 16);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//kuldok egy levelet magamnak hogy mirol akartak jelentest kuldeni

        //peldanyositunk egy levelkuldot
        try {
            stat.beir(System.getProperty("user.name"), Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()), "Mjelenteskartlenni", "gabor.hanacsek@sanmina.com");
            Email += "<html><div>" + jTextArea2.getText().replace("\n", "<br>") + "</div></html>";
            Tc_Levelkuldes l = new Tc_Levelkuldes(subject, Email,  jTextArea1.getText() ,"Muszakjelentes@sanmina.com");
            l.start();
            DateFormat df = new SimpleDateFormat("HH:mm");
            Date dateobj = new Date();
            b.jLabel1.setText("Elküldve: " + df.format(dateobj));
            this.dispose();
        } catch (Exception e) {

            stat.beir(System.getProperty("user.name"), Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()), "Elhasaltunk a peldanyositasnal" + e, "gabor.hanacsek@sanmina.com");

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyPressed

        String ellenorzobe = jTextArea2.getText();
        jTextArea3.setText(ellenorzoadat + ellenorzobe);


    }//GEN-LAST:event_jTextArea2KeyPressed

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
            java.util.logging.Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Muszakjelentes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Tc_Muszakjelentes().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    // End of variables declaration//GEN-END:variables
}
