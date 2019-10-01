/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.CONNECTS.*;
import java.awt.Toolkit;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_AnyaghianyRogzito extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Anyaghiany
     */
    Tc_Besheet b;

    public Tc_AnyaghianyRogzito(Tc_Besheet b) throws SQLException, ClassNotFoundException {
        this.b = b;
        initComponents();
        seticon();
        jLabel7.setText(b.jTable2.getColumnName(b.jTable2.getSelectedColumn()));
    }

    public void ment(String pktomig) {

        //legyen pktomig
        if (pktomig.equals("")) {

            JOptionPane.showMessageDialog(this,
                    "Ebbe a mezőbe nem vehetsz fel anyaghiányt!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

//legyen valami kiválasztva
        if (jComboBox1.getSelectedIndex() == -1) {

            //custom title, error icon
            JOptionPane.showMessageDialog(this,
                    "Nem választottál ki okot!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

//legyen pn is megadva
        if (jTextField1.getText().equals("")) {

            JOptionPane.showMessageDialog(this,
                    "Nem adtál meg PN-t!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

// el kell menteni az allasidős táblába is az adatokat
//kell a dátum a megfelelő formátumban , tól ig
        String tol = this.jLabel7.getText().substring(0, 10) + " " + jSpinner1.getValue().toString() + ":" + jSpinner2.getValue().toString();
        String ig = this.jLabel7.getText().substring(0, 10) + " " + jSpinner3.getValue().toString() + ":" + jSpinner4.getValue().toString();

        //ha az óra 0-5 között van akkor hozzá kell adni egy napot mert akkor az már a másik nap
        org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        if (jSpinner1.getValue().equals("00") || jSpinner1.getValue().equals("01") || jSpinner1.getValue().equals("02") || jSpinner1.getValue().equals("03") || jSpinner1.getValue().equals("04") || jSpinner1.getValue().equals("05")) {

            DateTime dt = formatter.parseDateTime(this.jLabel7.getText().substring(0, 10));

            String tolnap = dt.plusDays(1).toString();
            tolnap = tolnap.substring(0, 10) + " ";

            tol = tolnap + jSpinner1.getValue().toString() + ":" + jSpinner2.getValue().toString();

        }
        //ha az óra 0-5 között van akkor hozzá kell adni egy napot mert akkor az már a másik nap
        if (jSpinner3.getValue().equals("00") || jSpinner3.getValue().equals("01") || jSpinner3.getValue().equals("02") || jSpinner3.getValue().equals("03") || jSpinner3.getValue().equals("04") || jSpinner3.getValue().equals("05")) {

            DateTime dt = formatter.parseDateTime(this.jLabel7.getText().substring(0, 10));

            String ignap = dt.plusDays(1).toString();
            ignap = ignap.substring(0, 10) + " ";

            ig = ignap + jSpinner3.getValue().toString() + ":" + jSpinner4.getValue().toString();

        }

        //leellenőrizzük , hogy nem fordul e minuszba a kettő különbsége
        formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime toldatetime = formatter.parseDateTime(tol);
        DateTime igdatetime = formatter.parseDateTime(ig);

        if (toldatetime.isAfter(igdatetime)) {

            JOptionPane.showMessageDialog(this,
                    "Nem jók a megadott időpontok!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

        //az adatbazisba be kell irni az anyaghianyt   (updateljuk a pktomig alapjan a tervet)
        String query = "update tc_terv set ah = '1' where pktomig = '" + pktomig + "'";
        planconnect pc = new planconnect();
        pc.feltolt(query, false);

        //aztan beallitjuk a cellcalssban is
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                infsor++;

            }

        }
        if (b.jTable2.getValueAt(b.jTable2.getSelectedRow(), 3).toString().equals("Terv")) {
            b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].ah = jTextField1.getText().trim();
        } else {

            b.tablaadat[b.jTable2.getSelectedRow() - infsor - 1][b.jTable2.getSelectedColumn()].ah = jTextField1.getText().trim();

        }

        //az anyaghianyos tablaba is fel kell vinni de ide mar minden adatot
        query = "insert into tc_anyaghiany (pktomig,pn,comment,felelos,tol,ig,cella) values ('" + pktomig + "','" + jTextField1.getText().trim() + "','" + jTextArea1.getText() + "','" + String.valueOf(jComboBox1.getSelectedItem()) + "','" + tol + "','" + ig + "','" + Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()) + "') on duplicate key update pn = values(pn) , comment = values(comment) , felelos = values(felelos),tol = values(tol) , ig = values(ig)";
        pc.feltolt(query, false);
        JOptionPane.showMessageDialog(this,
                "Sikeres mentés!");
        this.dispose();
        b.jTable2.repaint();
    }

    public void torol(String pktomig) {
//az adatbazisba updateljük üresre
        String query = "update tc_terv set ah = '' where pktomig = '" + pktomig + "'";
        planconnect pc = new planconnect();
        pc.feltolt(query, false);
//aztan beallitjuk a cellcalssban is
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                infsor++;

            }

        }

        if (b.jTable2.getValueAt(b.jTable2.getSelectedRow(), 3).toString().equals("Terv")) {
            b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].ah = "";
        } else {

            b.tablaadat[b.jTable2.getSelectedRow() - infsor - 1][b.jTable2.getSelectedColumn()].ah = "";
        }
        b.jTable2.repaint();
        JOptionPane.showMessageDialog(this,
                "Sikeres törlés!");
        this.dispose();

    }

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlannTool/PICTURES/ah.jpg")));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Anyaghiány");

        jLabel1.setText("PartNumber:");

        jLabel2.setText("Anyaghiány oka:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nincs készleten", "Nem találjuk", "Nem ért ki időben", "Quality probléma", "Nincs előkészítve" }));
        jComboBox1.setSelectedIndex(-1);

        jLabel3.setText("Komment:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setText("Tól:");

        jLabel5.setText("Ig:");

        jButton1.setText("Ment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        if(b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).contains("06:")){
            jSpinner1.setModel(new javax.swing.SpinnerListModel(new String[] {"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"}));
        }
        else{

            jSpinner1.setModel(new javax.swing.SpinnerListModel(new String[] {"18", "19", "20", "21", "22", "23", "00", "01", "02", "03", "04", "05"}));

        }

        jSpinner2.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        if(b.jTable2.getColumnName(b.jTable2.getSelectedColumn()).contains("06:")){
            jSpinner3.setModel(new javax.swing.SpinnerListModel(new String[] {"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"}));
        }
        else{

            jSpinner3.setModel(new javax.swing.SpinnerListModel(new String[] {"18", "19", "20", "21", "22", "23", "00", "01", "02", "03", "04", "05"}));

        }

        jSpinner4.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        jLabel6.setText("Dátum , szak:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // mentés
        Tc_AdatInterface a = new Tc_AdatInterface(b);

        ment(a.pktomig());
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
            java.util.logging.Logger.getLogger(Tc_AnyaghianyRogzito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_AnyaghianyRogzito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_AnyaghianyRogzito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_AnyaghianyRogzito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Tc_Anyaghiany().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Tc_Anyaghiany().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
