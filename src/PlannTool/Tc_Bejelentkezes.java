/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Bejelentkezes extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Bejelentkezes
     *
     *
     */
    private ablak az;

    public Tc_Bejelentkezes(ablak a) {
        initComponents();
        this.az = a;
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
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Felhasználó név:");

        jLabel2.setText("Jelszó:");

        jButton1.setText("Bejelentkezés plannerként");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Kérlek jelentkezz be! Universal login!");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
        });

        jButton2.setText("Nem vagyok Planner!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPasswordField1)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(47, Short.MAX_VALUE))
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
        planconnect pc = new planconnect();
        char[] password = jPasswordField1.getPassword();

        //proba universal login
        URL oracle = null;
        try {
            oracle = new URL("http://143.116.140.120/api/auth/authlib.php?username=" + jTextField1.getText() + "&password=" + String.valueOf(password) + "");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
        } catch (IOException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }

        String inputLine = "";
        String truee = "";
        try {
            while ((inputLine = in.readLine()) != null) {
                
                truee = inputLine;
            }
        } catch (IOException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }

        //eredeti kód
        String query = "SELECT job_positions_id as poz , pass FROM planningdb.perm where perm.email like '%" + jTextField1.getText().replace(".", "_") + "%'";

        ResultSet rs = null;
        try {
            rs = (ResultSet) pc.planconnect(query);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (rs.next()) {

                String poz = "";
                poz = rs.getString(1);


                if (truee.equals("true") && (Integer.parseInt(poz) == 1 || Integer.parseInt(poz) == 2 || Integer.parseInt(poz) == 4)) {
//sikeres bejelentkezes
                    ablak.planner = true;
                    Tc_Betervezo b = new Tc_Betervezo();
                    b.setVisible(true);
                    this.dispose();

                } else {
//sikertelen bejelentkezes
                    ablak.planner = false;
//Tc_Betervezo b = new Tc_Betervezo();
//b.setVisible(true);
//this.dispose();
                    infobox info = new infobox();
                    info.infoBox("Nem jó jelszó vagy felh. név!", "Hiba!");

                    this.setVisible(true);

                }

            } else {
//sikertelen bejelentkezes
                ablak.planner = false;
//Tc_Betervezo b = new Tc_Betervezo();
//b.setVisible(true);
//this.dispose();
                infobox info = new infobox();
                info.infoBox("Nem jó jelszó vagy felh. név!", "Hiba!");
                this.setVisible(true);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            planconnect pc = new planconnect();
            char[] password = jPasswordField1.getPassword();

            //proba universal login
            URL oracle = null;
            try {
                oracle = new URL("http://143.116.140.120/api/auth/authlib.php?username=" + jTextField1.getText() + "&password=" + String.valueOf(password) + "");
            } catch (MalformedURLException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedReader in = null;

            try {
                in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));
            } catch (IOException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }

            String inputLine = "";
            String truee = "";
            try {
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    truee = inputLine;
                }
            } catch (IOException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }

            //eredeti kód
            String query = "SELECT job_positions_id as poz , pass FROM planningdb.perm where perm.email like '%" + jTextField1.getText().replace(".", "_") + "%'";

            ResultSet rs = null;
            try {
                rs = (ResultSet) pc.planconnect(query);
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (rs.next()) {

                    String poz = "";
                    poz = rs.getString(1);
//            String pass = rs.getString(2);
//            char[] correctpass = pass.toCharArray();

                    if (truee.equals("true") && (Integer.parseInt(poz) == 1 || Integer.parseInt(poz) == 2 || Integer.parseInt(poz) == 4)) {
//sikeres bejelentkezes
                        ablak.planner = true;
                        Tc_Betervezo b = new Tc_Betervezo();
                        b.setVisible(true);
                        this.dispose();

                    } else {
//sikertelen bejelentkezes
                        ablak.planner = false;
//Tc_Betervezo b = new Tc_Betervezo();
//b.setVisible(true);
//this.dispose();
                        infobox info = new infobox();
                        info.infoBox("Nem jó jelszó vagy felh. név!", "Hiba!");

                        this.setVisible(true);

                    }

                } else {
//sikertelen bejelentkezes
                    ablak.planner = false;
//Tc_Betervezo b = new Tc_Betervezo();
//b.setVisible(true);
//this.dispose();
                    infobox info = new infobox();
                    info.infoBox("Nem jó jelszó vagy felh. név!", "Hiba!");

                    this.setVisible(true);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jPasswordField1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        //a nem vagyok planner gomb
        ablak.planner = false;
        Tc_Betervezo b = null;
        try {
            b = new Tc_Betervezo();
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(Level.SEVERE, null, ex);
        }
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:

        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Bejelentkezes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Tc_Bejelentkezes().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}