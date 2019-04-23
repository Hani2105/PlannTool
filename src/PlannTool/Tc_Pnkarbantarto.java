/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Pnkarbantarto extends javax.swing.JFrame {

    /**
     * Creates new form Tc_Pnkarbantarto
     */
    String pn = "";

    //ez egy változó a legördülőhöz
    List<String[]> legordulolist = new ArrayList<>();

    public Tc_Pnkarbantarto(String pn) {
        initComponents();
        this.pn = pn;
        String query = "";
        planconnect pc = new planconnect();
//ez lesz a selected item a projeknél
        String projekt = "";
        String customer = "";

        if (ablak.planner == false) {

            jButton1.setEnabled(false);

        } else {

            jButton1.setEnabled(true);

        }

//ha a pn nem "";
        if (!pn.equals("")) {

            jTextField1.setText(pn);
            query = "select * from pn_data where PartNumber = '" + pn + "'";

            try {
                pc.planconnect(query);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                while (pc.rs.next()) {

                    jTextField2.setText(pc.rs.getString(2));
                    jTextField3.setText(pc.rs.getString(4));
                    jTextField4.setText(pc.rs.getString(6));
                    jTextField5.setText(pc.rs.getString(9));
                    jTextField6.setText(pc.rs.getString(11));
                    jTextField7.setText(pc.rs.getString(12));

                    //beszetteljük az item type-ot
                    jComboBox3.setSelectedItem(pc.rs.getString(7));

                    //beszetteljük a harman be legördülőt
                    jComboBox5.setSelectedItem(pc.rs.getString(10));

                    //beszetteljük az MSL szintet
                    jComboBox4.setSelectedItem(pc.rs.getString(8));

                    try {
                        if (pc.rs.getString(3).length() > 0) {
                            projekt = pc.rs.getString(3);
                        }
                    } catch (Exception e) {
                    }

                    try {

                        if (pc.rs.getString(5).length() > 0) {
                            customer = pc.rs.getString(5);
                        }
                    } catch (Exception e) {
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

//mindenképpen kitöltjük a legördülőket
//lekérdezzük a projekteket és betesszük a legördülőbe
        query = "select * from project_names";

        try {
            pc.planconnect(query);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        try {
            while (pc.rs.next()) {
//bepakoljuk a listbe az adatokat projekt

                jComboBox1.addItem(pc.rs.getString(2));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        //ha van projekt adat beszetteljük
        if (!projekt.equals("")) {

            jComboBox1.setSelectedItem(projekt);

        } else {

            jComboBox1.setSelectedIndex(-1);
        }

//lekérdezzük a vevőket és betesszük a legördülőbe       
        query = "select * from customer_names";

        try {
            pc.planconnect(query);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        try {
            while (pc.rs.next()) {

                String[] adatok = new String[3];

                adatok[0] = pc.rs.getString(1);
                adatok[1] = pc.rs.getString(2);
                adatok[2] = pc.rs.getString(3);

                legordulolist.add(adatok);
                jComboBox2.addItem(pc.rs.getString(3));

            }
        } catch (SQLException ex) {

        }

        //ha van cusomer beállítjuk
        if (!customer.equals("")) {

            jComboBox2.setSelectedItem(customer);

        } else {

            jComboBox2.setSelectedIndex(-1);

        }

        if (pn.equals("")) {
            jTextField2.setText("");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PN Karbantartó");

        jLabel1.setText("PartNumber:");

        jTextField1.setText("");

        jLabel2.setText("Vevő:");

        jTextField2.setEditable(false);
        jTextField2.setText("");

        jLabel3.setText("Description:");

        jTextField3.setText("");

        jLabel4.setText("Planner komment:");

        jTextField4.setText("");

        jLabel5.setText("PCB PN:");

        jTextField5.setText("");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel6.setText("PN prefix nélkül:");

        jTextField6.setText("");

        jLabel7.setText("SMT-s alkártya ha van:");

        jTextField7.setText("");

        jLabel8.setText("Projekt:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));

        jLabel9.setText("Belső vevő:");

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jComboBox2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox2PropertyChange(evt);
            }
        });

        jLabel10.setText("Item type:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SA", "FG" }));

        jLabel11.setText("MSL szint:");

        jComboBox4.setMaximumRowCount(10);
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        jLabel12.setText("Harman BE:");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nem", "Igen" }));

        jButton1.setText("Mentés");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Keresés");
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
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(21, 21, 21)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField3)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox3, 0, 413, Short.MAX_VALUE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField2)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField7)
                                .addComponent(jTextField6)
                                .addComponent(jComboBox5, 0, 413, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(31, 31, 31)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(176, 176, 176))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(107, 107, 107))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // ez a mentés gomb

        //ha üresek a comboboxok kicseréljük "" re.
        String com1 = "";
        if (jComboBox1.getSelectedIndex() != -1) {

            com1 = jComboBox1.getSelectedItem().toString();

        }

        String com2 = "";
        if (jComboBox2.getSelectedIndex() != -1) {

            com2 = jComboBox2.getSelectedItem().toString();

        }

        String query = "insert ignore pn_data \n"
                + "(PartNumber,OutSideCustomer,Project,Description,InSideCustomer,\n"
                + "Comment,Item_Type,MSL_Level,PCB_PN,Harman_BE,noprefix,SMT,active,modosito) values ('" + jTextField1.getText() + "','" + jTextField2.getText() + "','" + com1 + "','" + jTextField3.getText() + "','" + com2 + "','" + jTextField4.getText() + "','" + jComboBox3.getSelectedItem().toString() + "','" + jComboBox4.getSelectedItem().toString() + "','" + jTextField5.getText() + "','" + jComboBox5.getSelectedItem().toString() + "','" + jTextField6.getText() + "','" + jTextField7.getText() + "','1','" + ablak.user + "')\n"
                + "on duplicate key update OutSideCustomer = values(OutSideCustomer) , Project = values(Project) , Description = values(Description) , InSideCustomer = values(InSideCustomer),\n"
                + "Comment = values(Comment) , Item_Type = values(Item_Type) , MSL_Level = values(MSL_Level) ,PCB_PN = values(PCB_PN) , Harman_BE = values(Harman_BE),\n"
                + "noprefix = values(noprefix) , SMT = values(SMT) , active = values(active) ,modosito = values(modosito)";

        planconnect pc = new planconnect();

        pc.feltolt(query, true);

        //frissitjük az adatokat
        //frissitjuk a partnumber  adatokat a sheeteken
        for (int n = 0; n < Tc_Betervezo.Tervezotabbed.getTabCount(); n++) {

            query = "select tc_bepns.partnumber ,pn_data.Project ,pn_data.Comment from tc_bepns \n"
                    + "left join tc_prodmatrix on tc_prodmatrix.id_tc_bepns = tc_bepns.idtc_bepns\n"
                    + "left join tc_becells on tc_prodmatrix.id_tc_becells = tc_becells.idtc_cells\n"
                    + "left join pn_data on pn_data.PartNumber = tc_bepns.partnumber\n"
                    + "where tc_becells.cellname = '" + Tc_Betervezo.Tervezotabbed.getTitleAt(n) + "'";

            //kiszedjuk a regieket
            Tc_Betervezo.Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(n)).partnumbers.clear();

            try {
                pc.planconnect(query);
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Besetup.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Besetup.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                while (pc.rs.next()) {

                    String[] pnadatok = new String[3];

                    pnadatok[0] = pc.rs.getString(1);
                    pnadatok[1] = pc.rs.getString(2);
                    pnadatok[2] = pc.rs.getString(3);

                    Tc_Betervezo.Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(n)).partnumbers.add(pnadatok);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Besetup.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        pc.kinyir();

        //frissitjuk a tablat
        try {
            Tc_Betervezo.Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.repaint();
        } catch (Exception e) {
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox2PropertyChange

    }//GEN-LAST:event_jComboBox2PropertyChange


    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:

        for (int i = 0; i < legordulolist.size(); i++) {
            try {
                if (jComboBox2.getSelectedItem().toString().equals(legordulolist.get(i)[2])) {

                    jTextField2.setText(legordulolist.get(i)[1]);

                }
            } catch (Exception e) {
            }

        }

        if (jComboBox2.getSelectedIndex() == -1) {

            jTextField2.setText("");

        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //kereső gomb

        String keresendo = "";

//alapra állítjuk a beviteli cuccokat
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");

        //beszetteljük az item type-ot
        jComboBox1.setSelectedIndex(-1);

        //beszetteljük a harman be legördülőt
        jComboBox2.setSelectedIndex(-1);

        

        try {
            keresendo = jTextField1.getText().trim();
        } catch (Exception e) {
        }

        String query = "select * from pn_data where PartNumber = '" + keresendo + "'";
        planconnect pc = new planconnect();
        String projekt = "";
        String customer = "";
        try {
            pc.planconnect(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            while (pc.rs.next()) {

                //jTextField1.setText(pn);
                jTextField2.setText(pc.rs.getString(2));
                jTextField3.setText(pc.rs.getString(4));
                jTextField4.setText(pc.rs.getString(6));
                jTextField5.setText(pc.rs.getString(9));
                jTextField6.setText(pc.rs.getString(11));
                jTextField7.setText(pc.rs.getString(12));

                //beszetteljük az item type-ot
                jComboBox3.setSelectedItem(pc.rs.getString(7));

                //beszetteljük a harman be legördülőt
                jComboBox5.setSelectedItem(pc.rs.getString(10));

                //beszetteljük az MSL szintet
                jComboBox4.setSelectedItem(pc.rs.getString(8));

                try {
                    if (pc.rs.getString(3).length() > 0) {
                        projekt = pc.rs.getString(3);
                    }
                } catch (Exception e) {
                }

                try {

                    if (pc.rs.getString(5).length() > 0) {
                        customer = pc.rs.getString(5);
                    }
                } catch (Exception e) {
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //mindenképpen kitöltjük a legördülőket
//lekérdezzük a projekteket és betesszük a legördülőbe
        query = "select * from project_names";

        try {
            pc.planconnect(query);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        try {
            while (pc.rs.next()) {
//bepakoljuk a listbe az adatokat projekt

                jComboBox1.addItem(pc.rs.getString(2));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        //ha van projekt adat beszetteljük
        if (!projekt.equals("")) {

            jComboBox1.setSelectedItem(projekt);

        } else {

            jComboBox1.setSelectedIndex(-1);
        }

//lekérdezzük a vevőket és betesszük a legördülőbe       
        query = "select * from customer_names";

        try {
            pc.planconnect(query);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        try {
            while (pc.rs.next()) {

                String[] adatok = new String[3];

                adatok[0] = pc.rs.getString(1);
                adatok[1] = pc.rs.getString(2);
                adatok[2] = pc.rs.getString(3);

                legordulolist.add(adatok);
                jComboBox2.addItem(pc.rs.getString(3));

            }
        } catch (SQLException ex) {

        }

        //ha van cusomer beállítjuk
        if (!customer.equals("")) {

            jComboBox2.setSelectedItem(customer);

        } else {

            jComboBox2.setSelectedIndex(-1);

        }

        if (keresendo.equals("")) {
            jTextField2.setText("");
        }

        pc.kinyir();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

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
            java.util.logging.Logger.getLogger(Tc_Pnkarbantarto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Pnkarbantarto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Pnkarbantarto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Pnkarbantarto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Tc_Pnkarbantarto().setVisible(true);
            }
        });
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
