/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

import PlannTool.CONNECTS.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Anyaghiany extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Anyaghiany
     */
    Tc_Besheet b;

    public Tc_Anyaghiany(Tc_Besheet b) throws SQLException, ClassNotFoundException {
        this.b = b;
        initComponents();
        lekerdez();
    }

    public void ment(String pktomig) {

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
        query = "insert into tc_anyaghiany (pktomig,pn,comment,felelos,tol,ig) values ('" + pktomig + "','" + jTextField1.getText().trim() + "','" + jTextArea1.getText() + "','" + String.valueOf(jComboBox1.getSelectedItem()) + "','" + String.valueOf(jComboBox2.getSelectedItem()) + String.valueOf(jComboBox3.getSelectedItem()) + "','" + String.valueOf(jComboBox5.getSelectedItem()) + String.valueOf(jComboBox4.getSelectedItem()) + "') on duplicate key update pn = values(pn) , comment = values(comment) , felelos = values(felelos),tol = values(tol) , ig = values(ig)";
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

    public void lekerdez() throws SQLException, ClassNotFoundException {

        //ki kell szedni a pktomoget a cellklasszbol es lekerdezni az adatbazisbol a hozza tartozo infokat
        int infsor = 0;

        for (int i = 0; i < b.jTable2.getRowCount(); i++) {

            if (b.jTable2.getValueAt(i, 3).toString().equals("Infó")) {

                infsor++;

            }

        }
        String pktomig = b.tablaadat[b.jTable2.getSelectedRow() - infsor][b.jTable2.getSelectedColumn()].pktomig;
        if (pktomig.equals("")) {

            pktomig = b.tablaadat[b.jTable2.getSelectedRow() - infsor - 1][b.jTable2.getSelectedColumn()].pktomig;

        }

        String query = "select * from tc_anyaghiany where pktomig = '" + pktomig + "'";
        planconnect pc = new planconnect();
        pc.lekerdez(query);

        while (pc.rs.next()) {

            jTextField1.setText(pc.rs.getString(3));
            jTextArea1.setText(pc.rs.getString(4));
            //ki kell szedni az időpontokat is
            jComboBox2.setSelectedItem(pc.rs.getString(6).substring(0, 2));
            jComboBox3.setSelectedItem(pc.rs.getString(6).substring(2, 4));
            jComboBox5.setSelectedItem(pc.rs.getString(7).substring(0, 2));
            jComboBox4.setSelectedItem(pc.rs.getString(7).substring(2, 4));
            //beallitjuk a felelost is
            jComboBox1.setSelectedItem(pc.rs.getString(5));

        }

        pc.kinyir();

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
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        jButton1.setText("Ment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Töröl");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
            .addComponent(jScrollPane1)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox2, jComboBox3, jComboBox4, jComboBox5});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // mentés
        Tc_AdatInterface a = new Tc_AdatInterface(b);
        ment(a.pktomig());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // töröl
        Tc_AdatInterface a = new Tc_AdatInterface(b);
        torol(a.pktomig());
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Tc_Anyaghiany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Anyaghiany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Anyaghiany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Anyaghiany.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}