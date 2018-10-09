/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import com.mysql.jdbc.StringUtils;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Array;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.joda.time.DateTime;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Betervezo extends javax.swing.JFrame {

    /**
     */
    public String[][] pns;
    public static List<String> partn = new ArrayList<String>();
    public static List<String> workstations = new ArrayList<String>();
    public static Map<String, Tc_Besheet> Besheets = new TreeMap();
    public static List<String[][]> ciklusidok = new ArrayList<String[][]>();

    public static int slide1;
    public static int slide2;
    public static int slide3;
    public static int slide4;
    public static int slide5;
    public static int slide6;

    //oszlopallitas valtozoi
    public static boolean allitsuke = true;
    public static int slider1 = 130;
    public static int slider2 = 130;

    public Tc_Betervezo() throws SQLException, ClassNotFoundException {

        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        seticon();
        jButton1.requestFocus();
        //lekerjuk a szinezest
        planconnect pc = new planconnect();

        //szinbeallitasok lekerese
        String Query = "select tc_users.slides from tc_users where tc_users.username = '" + System.getProperty("user.name") + "'";

        pc.planconnect(Query);

        while (pc.rs.next()) {

            String szamok = pc.rs.getString(1);
            String[] szamtomb = szamok.split(",");
            slide1 = Integer.parseInt(szamtomb[0]);
            slide2 = Integer.parseInt(szamtomb[1]);
            slide3 = Integer.parseInt(szamtomb[2]);
            slide4 = Integer.parseInt(szamtomb[3]);
            slide5 = Integer.parseInt(szamtomb[4]);
            slide6 = Integer.parseInt(szamtomb[5]);

        }

        //letiltjuk a gombokat ha nem planner
        if (ablak.planner == false) {
            //this.jButton5.setEnabled(false);
            this.jButton2.setEnabled(false);
        }

        if (System.getProperty("user.name").equals("gabor_hanacsek")) {

            jButton2.setEnabled(true);
        }

        //lekerdezzuk a ciklusidoket
        String query = "select tc_becells.cellname , tc_bepns.partnumber , tc_bestations.workstation , tc_prodmatrix.ciklusido from tc_prodmatrix \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_prodmatrix.id_tc_becells \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_prodmatrix.id_tc_bepns\n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_prodmatrix.id_tc_bestations";
        pc = new planconnect();
        pc.planconnect(query);

        pc.rs.last();
        int utsosor = pc.rs.getRow();
        pc.rs.beforeFirst();

        String[][] ciklusidok = new String[utsosor][4];
        int i = 0;
        while (pc.rs.next()) {

            ciklusidok[i][0] = pc.rs.getString(1);
            ciklusidok[i][1] = pc.rs.getString(2);
            ciklusidok[i][2] = pc.rs.getString(3);
            ciklusidok[i][3] = pc.rs.getString(4);

            i++;
        }

        this.ciklusidok.add(ciklusidok);

        Tc_Cellavalaszto c = new Tc_Cellavalaszto(this);
        c.setVisible(true);

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
        jTabbedPane1 = new PlannTool.Tc_JTabbedPaneWithCloseIcons();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/cv1.png"))); // NOI18N
        jButton1.setToolTipText("Cella választó!");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/setup1.png"))); // NOI18N
        jButton2.setToolTipText("Setup");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Tól:");

        jLabel2.setText("Ig:");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/dall1.png"))); // NOI18N
        jButton3.setToolTipText("Terv lekérése az összes sheetre!");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
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

        jLabel3.setText("Műszakrend:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12 órás", "8 órás" }));

        jButton4.setText("+");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Terv/Tény sor +");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/version.png"))); // NOI18N
        jButton5.setToolTipText("Előző verzió visszaállítása!");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(3, 3, 3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addComponent(jLabel3))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(49, 49, 49)
                            .addComponent(jLabel2))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(jLabel1))
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
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

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("kepek/2.jpg")));

    }

    //globalis valtozoban letaroljuk a setupban levo partnumbereket hogy le tudjuk ellenorizni a rendererben a letezesuket

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            Tc_Cellavalaszto c = new Tc_Cellavalaszto(this);
            c.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        Tc_Besetup c = null;
        try {
            c = new Tc_Besetup();
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        Tc_ossztervlekerszal o = new Tc_ossztervlekerszal();
        o.start();


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //aktuális sheet
        int n = jTabbedPane1.getSelectedIndex();
        String neve = jTabbedPane1.getTitleAt(n);
        //hozzaadjuk a sorokat
        DefaultTableModel t2 = new DefaultTableModel();
        t2 = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
        int rownumber = t2.getRowCount();

        for (int i = 0; i < Integer.parseInt(jTextField1.getText()); i++) {

            t2.addRow(new Object[]{null, null, null, "Terv"});
            t2.addRow(new Object[]{null, null, null, "Tény"});

        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                Tc_Cellavalaszto c = new Tc_Cellavalaszto(this);
                c.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Besheets.clear();
            for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {

                String name = jTabbedPane1.getTitleAt(i);

                Besheets.put(name, (Tc_Besheet) jTabbedPane1.getComponentAt(i));

            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String first = "";
            String second = "";

            try {
                first = df.format(jDateChooser1.getDate());
                second = df.format(jDateChooser2.getDate());
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
            int n = jTabbedPane1.getSelectedIndex();
            String neve = jTabbedPane1.getTitleAt(n);

            //kitoroljuk az oszlopokat
            DefaultTableModel model = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();
            model.setColumnCount(4);

            //oszlopok neve a datumbol
            Calendar c = Calendar.getInstance();
            c.setTime(jDateChooser1.getDate());
            Date dt = new Date();
            dt = c.getTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTimeFormatter fmtnap = DateTimeFormat.forPattern("E");

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

            for (int i = 0; i < Besheets.get(neve).jTable2.getModel().getColumnCount(); i++) {

                if (i != 3) {
                    column = Besheets.get(neve).jTable2.getColumnModel().getColumn(i);
                    column.setPreferredWidth(130);
                }

            }

            //lekerdezzuk az adatbazis adatokat
            String Query = "select tc_terv.date , tc_bepns.partnumber , tc_terv.job , tc_bestations.workstation , tc_terv.qty , tc_terv.tt \n"
                    + "from tc_terv \n"
                    + "left join tc_bepns on tc_bepns.idtc_bepns = tc_terv.idtc_bepns \n"
                    + "left join tc_bestations on tc_bestations.idtc_bestations = tc_terv.idtc_bestations\n"
                    + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                    + "where tc_terv.date between '" + fmt.print(dtOrg) + " 06:00:00" + "' and '" + columneve + ":00" + "' and tc_terv.active = 2 and tc_becells.cellname = '" + neve + "'  \n"
                    + "order by tc_terv.date , tc_terv.wtf";

            //feldolgozzuk az eredmenyt
            planconnect pc = new planconnect();
            try {
                pc.planconnect(Query);
                model.setRowCount(0);
                int r = 0;
                String terv = "";

                //vegigporgetjuk a resultsetet
                while (pc.rs.next()) {

                    for (int i = 4; i < model.getColumnCount(); i++) {

                        if (pc.rs.getString(1).equals(model.getColumnName(i).substring(0, model.getColumnName(i).length() - 4) + ":00.0")) {

                            if (r % 2 == 0) {

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

        }
    }//GEN-LAST:event_jButton3KeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:

        planconnect pc = new planconnect();
        String Query = "update tc_users set tc_users.slides = '" + slide1 + "," + slide2 + "," + slide3 + "," + slide4 + "," + slide5 + "," + slide6 + "'" + "where tc_users.username = '" + System.getProperty("user.name") + "'";
        pc.feltolt(Query, false);
    }//GEN-LAST:event_formWindowClosing

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/cv.png")));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/cv1.png")));
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/setup.png")));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/setup1.png")));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/dall.png")));

    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/dall1.png")));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        // TODO add your handling code here:
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/version1.png")));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        // TODO add your handling code here:
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/version.png")));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        //meghatarozzuk a tol - ig et

        String tol = Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnName(4).substring(0, 10) + " 06:00:00";
        String ig = "";

        //tol ig intervallum a sheeten
        if (Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(11, 12).equals("2")) {

            ig += Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(0, 10) + " 22:00:00";

        } else {

            ig += Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(0, 10) + " 18:00:00";

        }
        //oszeallitjuk a queryt a lekerdezeshez
        String query = "select distinct tc_terv.active , tc_terv.timestamp from tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "where (tc_terv.active = '1' or tc_terv.active = '0') and tc_becells.cellname = '" + Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex()) + "' and tc_terv.date between '" + tol + "' and '" + ig + "'";

        //lekerdezzuk
        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultTableModel model = new DefaultTableModel();
        Tc_Visszaallito v = new Tc_Visszaallito(Tc_Betervezo.jTabbedPane1.getTitleAt(Tc_Betervezo.jTabbedPane1.getSelectedIndex()), tol , ig);
        model = (DefaultTableModel) v.jTable1.getModel();
        model.setRowCount(0);

        try {
            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        v.setVisible(true);


    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public static javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public static javax.swing.JComboBox<String> jComboBox1;
    public static com.toedter.calendar.JDateChooser jDateChooser1;
    public static com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
