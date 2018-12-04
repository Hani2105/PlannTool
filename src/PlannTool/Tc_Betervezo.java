/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import com.mysql.jdbc.StringUtils;
import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
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
    //nem engedjuk csukni amig megy a levelkuldes
    public static boolean level = false;
    public String[][] pns;
    public static List<String> partn = new ArrayList<String>();
    public static List<String> works = new ArrayList<String>();
    public static Map<String, Tc_Besheet> Besheets = new TreeMap();
    public static List<String[][]> ciklusidok = new ArrayList<String[][]>();

    //szinezes adatai
    public static int slide1;
    public static int slide2;
    public static int slide3;
    public static int slide4;
    public static int slide5;
    public static int slide6;
    public static int slide7;
    public static int slide8;
    public static int slide9;

    //oszlopallitas valtozoi
    public static int allitsuke = 0;
    public static int slider1 = 130;
    public static int slider2 = 130;
    public static List<Integer> szelessegek = new ArrayList<Integer>();

    //kalkulátor sor vagy ossz vagy oszlopig?
    public static int calc = 3;

    //ha csukodik ne fusson a kalkulator
    public static boolean csuk = false;

    //az ablak
    public static ablak a;

    public Tc_Betervezo(ablak a) throws SQLException, ClassNotFoundException {
        setExtendedState(MAXIMIZED_BOTH);

        initComponents();

        seticon();
        jButton1.requestFocus();
        //lekerjuk a szinezest
        planconnect pc = new planconnect();

        //szinek alapbeallitasa
        slide1 = 255;
        slide2 = 255;
        slide3 = 255;
        slide4 = 153;
        slide5 = 255;
        slide6 = 153;
        slide7 = 51;
        slide8 = 255;
        slide9 = 204;

        //szinbeallitasok lekerese
        String Query = "select tc_users.slides from tc_users where tc_users.username = '" + System.getProperty("user.name") + "'";

        pc.planconnect(Query);

        while (pc.rs.next()) {

            String szamok = pc.rs.getString(1);
            String[] szamtomb = szamok.split(",");
            try {
                slide1 = Integer.parseInt(szamtomb[0]);
                slide2 = Integer.parseInt(szamtomb[1]);
                slide3 = Integer.parseInt(szamtomb[2]);
                slide4 = Integer.parseInt(szamtomb[3]);
                slide5 = Integer.parseInt(szamtomb[4]);
                slide6 = Integer.parseInt(szamtomb[5]);
            } catch (Exception e) {

                slide1 = 255;
                slide2 = 255;
                slide3 = 255;
                slide4 = 153;
                slide5 = 255;
                slide6 = 153;

            }
            try {
                slide7 = Integer.parseInt(szamtomb[6]);
                slide8 = Integer.parseInt(szamtomb[7]);
                slide9 = Integer.parseInt(szamtomb[8]);
            } catch (Exception e) {

                slide7 = 51;
                slide8 = 255;
                slide9 = 204;

            }

        }

        pc.kinyir();

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

        pc.kinyir();

        this.ciklusidok.add(ciklusidok);

        Tc_Cellavalaszto c = new Tc_Cellavalaszto(this);
        c.setVisible(true);
        this.a = a;
//lekerjuk a pn-eket es ws eket hogy le tudjuk ellenorizni , hogy leteznek e
        pncheck();
        wscheck();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        Tervezotabbed = new PlannTool.Tc_JTabbedPaneWithCloseIcons();
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
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/login2.png"))); // NOI18N
        jButton8.setToolTipText("Login!");
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tc_Betervező");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setForeground(new java.awt.Color(0, 153, 153));

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

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/segedlet1.png"))); // NOI18N
        jButton6.setToolTipText("Segédlet!");
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/login2.png"))); // NOI18N
        jButton7.setToolTipText("Login!");
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/keszlet2.png"))); // NOI18N
        jButton9.setToolTipText("Készlet!");
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
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
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Tervezotabbed, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(Tervezotabbed)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("kepek/2.jpg")));

    }

    public static void pncheck() throws SQLException, ClassNotFoundException {

        //lekerdezzuk a letezo PN -eket , hogy meg tudjuk vizsgalni , hogy el fog e veszni feltoltes utan (piros)
        String query = "select tc_bepns.partnumber from tc_bepns";
        planconnect pc = new planconnect();
        pc.planconnect(query);
        partn.clear();

        while (pc.rs.next()) {

            partn.add(pc.rs.getString(1));

        }

        pc.kinyir();

    }

    public static void wscheck() throws SQLException, ClassNotFoundException {

        //lekerdezzuk a letezo PN -eket , hogy meg tudjuk vizsgalni , hogy el fog e veszni feltoltes utan (piros)
        String query = "select tc_bestations.workstation from tc_bestations";
        planconnect pc = new planconnect();
        pc.planconnect(query);
        works.clear();

        while (pc.rs.next()) {

            works.add(pc.rs.getString(1));

        }

        pc.kinyir();

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

    //gombok engedese , letiltasa
    static void gombenged() {

        //letiltjuk a gombokat ha nem planner
        if (ablak.planner == false) {
            //this.jButton5.setEnabled(false);
            Tc_Betervezo.jButton2.setEnabled(false);
            Tc_Betervezo.jButton5.setEnabled(false);

            for (int i = 0; i < Tervezotabbed.getTabCount(); i++) {

                Besheets.get(Tervezotabbed.getTitleAt(i)).jButton11.setEnabled(true);
                Besheets.get(Tervezotabbed.getTitleAt(i)).jButton10.setEnabled(false);
                Besheets.get(Tervezotabbed.getTitleAt(i)).DeleteRow.setEnabled(false);

            }

        } else if (ablak.planner == true) {

            Tc_Betervezo.jButton2.setEnabled(true);
            Tc_Betervezo.jButton5.setEnabled(true);

            for (int i = 0; i < Tervezotabbed.getTabCount(); i++) {

                Besheets.get(Tervezotabbed.getTitleAt(i)).jButton11.setEnabled(false);
                Besheets.get(Tervezotabbed.getTitleAt(i)).jButton10.setEnabled(true);
                Besheets.get(Tervezotabbed.getTitleAt(i)).DeleteRow.setEnabled(true);

            }

        }

    }


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

    static public void tablarajzolo(DefaultTableModel model, JTable table) {
        SwingUtilities.invokeLater(() -> {

            table.setModel(model);

        });
    }


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        int comboertek = Tc_Betervezo.jComboBox1.getSelectedIndex();
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

        //meghatarozzuk a napokat , mekkora intervallumra kell beallitani a sheeteket
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

        //oszlopok neve a datumbol
        Calendar c = Calendar.getInstance();
        c.setTime(Tc_Betervezo.jDateChooser1.getDate());
        Date dt = new Date();
        dt = c.getTime();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        org.joda.time.format.DateTimeFormatter fmtnap = DateTimeFormat.forPattern("E");

//letrehozunk egy megfelelo jtablet
//most indítjuk a nagy ciklust amiben végigpörgetjük a sheeteket
        for (int b = 0; b < Tc_Betervezo.Tervezotabbed.getTabCount(); b++) {

            Tc_Leker leker = new Tc_Leker(Tc_Betervezo.Tervezotabbed.getTitleAt(b));

        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //aktuális sheet
        int n = Tervezotabbed.getSelectedIndex();
        String neve = Tervezotabbed.getTitleAt(n);
        //hozzaadjuk a sorokat
        DefaultTableModel t2 = new DefaultTableModel();
        t2 = (DefaultTableModel) Besheets.get(neve).jTable2.getModel();

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
                Logger.getLogger(Tc_Betervezo.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Betervezo.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Besheets.clear();
            for (int i = 0; i < Tervezotabbed.getTabCount(); i++) {

                String name = Tervezotabbed.getTitleAt(i);

                Besheets.put(name, (Tc_Besheet) Tervezotabbed.getComponentAt(i));

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
                    Logger.getLogger(Tc_Betervezo.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    two = df.parse(second);

                } catch (ParseException ex) {
                    Logger.getLogger(Tc_Betervezo.class
                            .getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Tc_Betervezo.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tc_Betervezo.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            pc.kinyir();
            Besheets.get(neve).jTable2.setModel(model);
            Tc_Calculator calc = new Tc_Calculator(Besheets.get(neve), false, 0);

        }
    }//GEN-LAST:event_jButton3KeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:

      
        
        
        
        csuk = true;


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

        String tol = Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnName(4).substring(0, 10) + " 06:00:00";
        String ig = "";

        //tol ig intervallum a sheeten
        if (Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(11, 12).equals("2")) {

            ig += Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(0, 10) + " 22:00:00";

        } else {

            ig += Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnName(Besheets.get(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex())).jTable2.getColumnCount() - 2).substring(0, 10) + " 18:00:00";

        }
        //oszeallitjuk a queryt a lekerdezeshez
        String query = "select distinct tc_terv.active , tc_terv.timestamp from tc_terv \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_terv.idtc_becells\n"
                + "where (tc_terv.active = '1' or tc_terv.active = '0') and tc_becells.cellname = '" + Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()) + "' and tc_terv.date between '" + tol + "' and '" + ig + "' group by active order by active  ";

        //lekerdezzuk
        planconnect pc = new planconnect();
        try {
            pc.planconnect(query);

        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        DefaultTableModel model = new DefaultTableModel();
        Tc_Visszaallito v = new Tc_Visszaallito(Tc_Betervezo.Tervezotabbed.getTitleAt(Tc_Betervezo.Tervezotabbed.getSelectedIndex()), tol, ig);
        model = (DefaultTableModel) v.jTable1.getModel();
        model.setRowCount(0);

        try {
            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2)});

            }
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Betervezo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        v.setVisible(true);


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/segedlet.png")));

    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/segedlet1.png")));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Tc_Segedletablak s = new Tc_Segedletablak();
        s.setVisible(true);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/login1.png")));

    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/login2.png")));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        Tc_Bejelentkezes b = new Tc_Bejelentkezes(a);
        b.nyit = false;
        b.setVisible(true);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        // TODO add your handling code here:
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/keszlet1.png")));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        // TODO add your handling code here:
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/keszlet2.png")));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        Tc_Keszletfromterv k = new Tc_Keszletfromterv();
        k.setVisible(true);


    }//GEN-LAST:event_jButton9ActionPerformed

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
    public static javax.swing.JTabbedPane Tervezotabbed;
    public javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    public static javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    public static javax.swing.JComboBox<String> jComboBox1;
    public static com.toedter.calendar.JDateChooser jDateChooser1;
    public static com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
