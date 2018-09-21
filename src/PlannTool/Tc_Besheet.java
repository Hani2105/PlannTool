/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import static PlannTool.Tc_Betervezo.Besheets;
import static PlannTool.Tc_SfdcData.ig;
import static PlannTool.Tc_SfdcData.tol;
import static PlannTool.ablak.jTable1;
import static PlannTool.ablak.jTable2;
import static PlannTool.ablak.jTextField1;
import static PlannTool.ablak.model;
import static PlannTool.ablak.model1;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Besheet extends javax.swing.JPanel {

    public List<String> partnumbers = new ArrayList<String>();
    public List<String> workstations = new ArrayList<String>();
    public List<String[][]> ciklusidok = new ArrayList<String[][]>();

    Tc_Betervezo bt;

    public Tc_Besheet(Tc_Betervezo b) throws SQLException, ClassNotFoundException {
        initComponents();

        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        new ExcelAdapter(jTable2);
        jTable2.getTableHeader().setDefaultRenderer(new Tc_TervTablaRenderer());
        jTable2.setDefaultRenderer(Object.class, new Tc_TervTooltipRenderer(this));
        bt = b;

        //lekerdezzuk a ciklusidoket
        String query = "select tc_becells.cellname , tc_bepns.partnumber , tc_bestations.workstation , tc_prodmatrix.ciklusido from tc_prodmatrix \n"
                + "left join tc_becells on tc_becells.idtc_cells = tc_prodmatrix.id_tc_becells \n"
                + "left join tc_bepns on tc_bepns.idtc_bepns = tc_prodmatrix.id_tc_bepns\n"
                + "left join tc_bestations on tc_bestations.idtc_bestations = tc_prodmatrix.id_tc_bestations";
        planconnect pc = new planconnect();
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPopupMenu1 = new javax.swing.JPopupMenu();
        CellaAdatok = new javax.swing.JMenuItem();
        DeleteArea = new javax.swing.JMenuItem();
        MuveletekSorokkal = new javax.swing.JMenu();
        InsertRow = new javax.swing.JMenuItem();
        DeleteRow = new javax.swing.JMenuItem();
        SFDClekeres = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        CellaAdatok.setText("Elérhető PN / WS");
        CellaAdatok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CellaAdatokActionPerformed(evt);
            }
        });
        JPopupMenu1.add(CellaAdatok);

        DeleteArea.setText("Terület törlése");
        DeleteArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAreaActionPerformed(evt);
            }
        });
        JPopupMenu1.add(DeleteArea);

        MuveletekSorokkal.setText("Sor +/-");
        MuveletekSorokkal.setActionCommand("Sor + / -");
        MuveletekSorokkal.setAutoscrolls(true);

        InsertRow.setText("Sor beszúrása fölé");
        InsertRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InsertRowActionPerformed(evt);
            }
        });
        MuveletekSorokkal.add(InsertRow);

        DeleteRow.setText("Sor törlése");
        DeleteRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteRowActionPerformed(evt);
            }
        });
        MuveletekSorokkal.add(DeleteRow);

        JPopupMenu1.add(MuveletekSorokkal);

        SFDClekeres.setText("SFDC lekérés");
        SFDClekeres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SFDClekeresActionPerformed(evt);
            }
        });
        JPopupMenu1.add(SFDClekeres);

        setComponentPopupMenu(JPopupMenu1);
        setPreferredSize(new java.awt.Dimension(1800, 700));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/paint1.png"))); // NOI18N
        jButton1.setToolTipText("Ter/Tény színezése");
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

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Job", "WorkStation", "Terv/Tény"
            }
        ));
        jTable2.setCellSelectionEnabled(true);
        jTable2.setComponentPopupMenu(JPopupMenu1);
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(130);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/pull1.png"))); // NOI18N
        jButton2.setToolTipText("Behúzós");
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/sfdc2.png"))); // NOI18N
        jButton3.setToolTipText("SFDC adatok lekérése");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/search1.png"))); // NOI18N
        jButton4.setToolTipText("Kereső");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTextField1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTextField1MouseExited(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/look1.png"))); // NOI18N
        jButton6.setToolTipText("Nézetváltás (PN / JOB szerint)");
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

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/envelop1.png"))); // NOI18N
        jButton7.setToolTipText("Műszakjelentés küldése");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(11, 11, 11)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(1, 1, 1))
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void parts() throws SQLException, ClassNotFoundException {

        planconnect pc = new planconnect();
        String query = "SELECT tc_bepns.partnumber  from tc_bepns where tc_bepns.idtc_bepns in \n"
                + "(select distinct tc_prodmatrix.id_tc_bepns from tc_prodmatrix where tc_prodmatrix.id_tc_becells  = \n"
                + "(SELECT tc_becells.idtc_cells FROM planningdb.tc_becells where tc_becells.cellname = '" + Tc_Cellavalaszto.neve + "'))";

        pc.planconnect(query);

        while (pc.rs.next()) {

            partnumbers.add(pc.rs.getString(1));

        }

    }

    public void workstations() throws SQLException, ClassNotFoundException {

        planconnect pc = new planconnect();
        String query = "SELECT tc_bestations.workstation  from tc_bestations where tc_bestations.idtc_bestations in \n"
                + "(select distinct tc_prodmatrix.id_tc_bestations from tc_prodmatrix where tc_prodmatrix.id_tc_becells  = \n"
                + "(SELECT tc_becells.idtc_cells FROM planningdb.tc_becells where tc_becells.cellname = '" + Tc_Cellavalaszto.neve + "'))";

        pc.planconnect(query);

        while (pc.rs.next()) {

            workstations.add(pc.rs.getString(1));

        }

    }


    private void DeleteRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteRowActionPerformed

        jTable2.revalidate();
        jTable2.invalidate();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();

        model.removeRow(jTable2.convertRowIndexToModel(jTable2.getSelectedRow()));

        jTable2.setModel(model);

        Tc_Calculator calc = new Tc_Calculator(this, bt);


    }//GEN-LAST:event_DeleteRowActionPerformed

    private void DeleteAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAreaActionPerformed

        int rows[] = jTable2.getSelectedRows();
        int columns[] = jTable2.getSelectedColumns();

        for (int i = 0; i < rows.length; i++) {

            for (int n = 0; n < columns.length; n++) {

                jTable2.setValueAt("", rows[i], columns[n]);

            }

        }


    }//GEN-LAST:event_DeleteAreaActionPerformed

    private void CellaAdatokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CellaAdatokActionPerformed

        Tc_PartsOfCells p = new Tc_PartsOfCells(this);
        p.setVisible(true);

    }//GEN-LAST:event_CellaAdatokActionPerformed

    private void InsertRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsertRowActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();

        if (jTable2.getValueAt(jTable2.getSelectedRow(), 3).equals("Terv")) {

            int i = jTable2.convertRowIndexToModel(jTable2.getSelectedRow());
            model.insertRow(i, new Object[]{null, null, null, "Tény"});
            model.insertRow(i, new Object[]{null, null, null, "Terv"});

        } else if (jTable2.getValueAt(jTable2.getSelectedRow(), 3).equals("Tény")) {

            int i = jTable2.convertRowIndexToModel(jTable2.getSelectedRow());
            model.insertRow(i - 1, new Object[]{null, null, null, "Tény"});
            model.insertRow(i - 1, new Object[]{null, null, null, "Terv"});

        }

        jTable2.setModel(model);
        Tc_Calculator calc = new Tc_Calculator(this, bt);

    }//GEN-LAST:event_InsertRowActionPerformed

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased

        if (!jTable2.isEditing()) {
            Tc_Calculator calc = new Tc_Calculator(this, bt);
        }

    }//GEN-LAST:event_jTable2KeyReleased

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/paint2.png")));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/paint1.png")));
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        Tc_Szinvalaszto sz = new Tc_Szinvalaszto(this);
        sz.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/pull2.png")));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/pull1.png")));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        Tc_Behuzos bh = new Tc_Behuzos();
        bh.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/sfdc3.png")));

    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/sfdc2.png")));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        Tc_SfdcData s = new Tc_SfdcData(this);
        s.setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void SFDClekeresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SFDClekeresActionPerformed

        //sfdc lekeres
        Tc_AnimationSFDC a = new Tc_AnimationSFDC();
        a.start();
        Tc_SFDCszal sz = new Tc_SFDCszal(this);
        sz.start();


    }//GEN-LAST:event_SFDClekeresActionPerformed

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/search2.png")));

    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/search1.png")));
    }//GEN-LAST:event_jButton4MouseExited

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        String query = jTextField1.getText().trim();
        filter(query);

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseEntered
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/search2.png")));

    }//GEN-LAST:event_jTextField1MouseEntered

    private void jTextField1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseExited
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/search1.png")));
    }//GEN-LAST:event_jTextField1MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/look2.png")));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/look1.png")));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //nezet atalakítas

        //betesszuk egy uj modelbe a tabla jelenlegi adatait
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();

        //betesszuk tommbe az adatokat
        String[][] adatok = new String[model.getRowCount()][model.getColumnCount()];

        for (int i = 0; i < model.getRowCount(); i++) {

            for (int n = 0; n < model.getColumnCount(); n++) {

                try {
                    adatok[i][n] = model.getValueAt(i, n).toString();
                } catch (Exception e) {
                }
            }

        }

        //kinullazzuk a modelt
        model.setRowCount(0);
        //elkezdjuk belepakolni az adatokat  , sor , oszlop
        //az elmentett adat sorait porgetjuk , ha van mar akkor nem irjuk be megegyszer
        for (int r = 0; r < adatok.length; r++) {

            //ezzel figyeljuk hogy van e mar ilyen pn és job
            boolean vanmar = false;
            //vegigporgetjuk az uj tablat 
            for (int sor = 0; sor < model.getRowCount(); sor++) {

                // ha egyezik a pn és a JOB , és a ws es a terv/teny azaz van mar ilyen
                try {
                    if (adatok[r][0].toString().equals(model.getValueAt(sor, 0)) && adatok[r][1].toString().equals(model.getValueAt(sor, 1)) && adatok[r][2].toString().equals(model.getValueAt(sor, 2)) && adatok[r][3].toString().equals(model.getValueAt(sor, 3))) {

                        vanmar = true;
                        //akkor beirjuk a darabot a megfelelo oszlopbol az aktualis cellaba
                        for (int o = 4; o < adatok.length; o++) {

                            try {
                                if (!adatok[r][o].toString().equals("") && adatok[r][o] != null) {

                                    model.setValueAt(adatok[r][o].toString(), sor, o);

                                }
                            } catch (Exception e) {
                            }

                        }

                    }
                } catch (Exception e) {
                }

            }

            //de ha nincs , azaz false a vanemar , kell egy uj sor
            if (vanmar == false) {

                //beallitjuk a pn , job , ws -t és a terv/ tenyt
                model.addRow(new Object[]{adatok[r][0], adatok[r][1], adatok[r][2], adatok[r][3]});

                //megkeressuk az oszlopban a db ot
                for (int o = 4; o < adatok.length; o++) {

                    try {
                        if (!adatok[r][o].toString().equals("") && adatok[r][o] != null) {

                            model.setValueAt(adatok[r][o].toString(), model.getRowCount() - 1, o);

                        }
                    } catch (Exception e) {
                    }

                }

            }

        }

        jTable2.setModel(model);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/envelop2.png")));


    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/kepek/envelop1.png")));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //email kuldes

        Tc_Muszakjelentes m;
        try {
            m = new Tc_Muszakjelentes(this);
            m.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(Tc_Besheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Tc_Besheet.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton7ActionPerformed

    private void filter(String query) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);

        jTable2.setRowSorter(tr);

        tr.setRowFilter(RowFilter.regexFilter(query));

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CellaAdatok;
    private javax.swing.JMenuItem DeleteArea;
    private javax.swing.JMenuItem DeleteRow;
    private javax.swing.JMenuItem InsertRow;
    public javax.swing.JPopupMenu JPopupMenu1;
    private javax.swing.JMenu MuveletekSorokkal;
    private javax.swing.JMenuItem SFDClekeres;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
