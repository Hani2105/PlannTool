/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.CTB_CALC.CTB_Tablarenderer;
import PlannTool.CONNECTS.planconnect;
import PlannTool.ExcelAdapter;
import static PlannTool.ablak.jTable4;
import PlannTool.infobox;
import PlannTool.universalfilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB extends javax.swing.JFrame {

    /**
     * Creates new form CTB
     */
    CTB_Bejel c;
    public static String user = "";
    static String[][] tabla1 = null;

    public CTB(CTB_Bejel c) throws SQLException, ClassNotFoundException {

        this.c = c;

        initComponents();

//beilleszthetővé tesszük az igények táblát
        new ExcelAdapter(jTable10);

        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setDefaultRenderer(Object.class, new CTB_Tablarenderer());
        jTable1.getTableHeader().setDefaultRenderer(new CTB_Columnrenderer());

        this.jTabbedPane1.setUI(new CTB_TabbedUI(this.jTabbedPane1));
        setExtendedState(MAXIMIZED_BOTH);
        seticon();
        PnToTable();
        UploadsDate();
        FillOhTable();
        FillAllocTable();
        FillDemandTable();
        FillWoTable();
        FillBomTable();
        OpenPoCalc();
        StockCalc();
        OpenOrderCalc();
//a pn-jeink bom matrixat rakja össze
        BomCalc();
//ez kiszámolja a felhasználható oh mennyiséget  (lasúúúúúúúú)
        Ohcalc(0);

//kiteszi a ctb táblába azokat az alkatrészeket amiket használ a termék
        CompToCtb();
//kitölti a ctb táblába , hogy hány termékre vagyunk tiszták
        CtbKalk();
        NeedToBuild();
        SaveCtbTableData();

        TablaOszlopSzelesseg(jTable1);
        jTable1.setBackground(new Color(0, 0, 0, 0));
        jScrollPane1.setBackground(new Color(0, 0, 0, 0));
        jScrollPane1.setOpaque(false);
        jTable1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

    }

    public void SaveCtbTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        tabla1 = new String[jTable1.getRowCount()][jTable1.getColumnCount()];
        for (int sor = 0; sor < model.getRowCount(); sor++) {

            for (int oszlop = 0; oszlop < model.getColumnCount(); oszlop++) {
                try {
                    tabla1[sor][oszlop] = model.getValueAt(sor, oszlop).toString();
                } catch (Exception e) {
                    tabla1[sor][oszlop] = "";
                }

            }

        }
    }

    public void alpagszuro(JMenuItem j) {

        int sor = jTable1.getSelectedRow();
        int oszlop = jTable1.getSelectedColumn();
        visszatolt();
        DefaultTableModel tempmodel = new DefaultTableModel();
        tempmodel = (DefaultTableModel) jTable1.getModel();
        tempmodel.setRowCount(0);

        if (oszlop > 6 && j.getText().equals("Szűrő")) {

//pörgetjük a modelt és ahol nem nulla az érték a kijelölt sorban ott egy az egyben hozzáadjuk a teljes sort a tempmodellhez
            for (int i = 0; i < tabla1.length; i++) {

                if (tabla1[i][oszlop] != null && !tabla1[i][oszlop].toString().equals("")) {

                    tempmodel.addRow(new Object[tabla1[1].length]);
//be is állítjuk az értékit az eredeti modellnek megfelelően
                    for (int k = 0; k < tabla1[1].length; k++) {
                        tempmodel.setValueAt(tabla1[i][k], tempmodel.getRowCount() - 1, k);
                    }

                }

            }

            jTable1.setModel(tempmodel);
        } else {

            visszatolt();
        }
        try {
            jTable1.setRowSelectionInterval(sor, sor);
            jTable1.setColumnSelectionInterval(oszlop, oszlop);
        } catch (Exception e) {
        }

    }

    public void visszatolt() {
        //helyreállítja az eredeti táblát a tabla1 ből

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < tabla1.length; i++) {
            model.addRow(new Object[tabla1[1].length]);
            for (int k = 0; k < tabla1[1].length; k++) {

                model.setValueAt(tabla1[i][k], i, k);

            }

        }

        jTable1.setModel(model);

    }

    public void PnToTable() throws SQLException, ClassNotFoundException {

        String query = "SELECT idtc_bepns , partnumber FROM planningdb.tc_bepns";
        planconnect pc = new planconnect();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        pc.lekerdez(query);
        pc.rs.last();
        int utsosor = pc.rs.getRow();
        pc.rs.beforeFirst();

        String[][] adatok = new String[utsosor][2];
        int i = 0;
        while (pc.rs.next()) {

            //model.addRow(new Object[]{pc.rs.getString(2)});
            adatok[i][0] = pc.rs.getString(1);
            adatok[i][1] = pc.rs.getString(2);
            i++;
        }

//lekérdezzük a felhasználó pn-jeit is
        query = "SELECT username , idpns FROM planningdb.CTB_UserData where username = '" + c.jTextField1.getText() + "'";

        pc.lekerdez(query);
        String[] pnids = null;

//eltesszük egy string tömbbe az id-kat
        while (pc.rs.next()) {
            pnids = pc.rs.getString(2).split(",");
        }

//vegigporgetjuk az adatokat es a pnids-t , ha egyezik a modelben akkor hozzaadunk egy sort
        for (int n = 0; n < adatok.length; n++) {

            for (int k = 0; k < pnids.length; k++) {

                if (adatok[n][0].equals(pnids[k])) {

                    model.addRow(new Object[]{adatok[n][1]});

                }

            }

        }

        pc.kinyir();
        jTable1.setModel(model);

    }

    public void BomCalc() {
//beállítjuk a tábla oszlopainak a számát , ami a CTB tábla PN száma és el is nevezzük őket ennek megfelelően
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable7.getModel();
        model.setRowCount(0);
        model.setColumnCount(2);
        for (int i = 0; i < jTable1.getRowCount(); i++) {

            model.addColumn(jTable1.getValueAt(i, 0).toString());

        }

        DefaultTableModel indentedmodel = new DefaultTableModel();
        indentedmodel = (DefaultTableModel) jTable6.getModel();

//miután ez megvan végigmegyünk rajta oszlopszor és megpróbáljuk megkereseni az adott oszlop pn-ét az intended bom ban,
        for (int o = 1; o < model.getColumnCount(); o++) {
//felvesszük pn-nek a pn-t :p
            String pn = model.getColumnName(o).trim();

            for (int r = 0; r < indentedmodel.getRowCount(); r++) {
//ha egyezik a pn az intended bom pn jével és nem phantom az item
                if (pn.equals(indentedmodel.getValueAt(r, 0).toString().trim()) && !indentedmodel.getValueAt(r, 8).toString().equals("") && !indentedmodel.getValueAt(r, 8).toString().equals("PH")) {
//felvesszük a componentet stringnek
                    String comp = indentedmodel.getValueAt(r, 7).toString();
//akkor megvizsgáljuk , hogy a componentet felvettük e már sorba a modellbe
//kell egy boolean , hogy kell e uj sort létrehozni
                    boolean ujsor = true;
                    for (int i = 0; i < model.getRowCount(); i++) {
//ha találunk olyan sort akkor oda írunk
                        if (model.getValueAt(i, 0).toString().equals(comp)) {

                            model.setValueAt(indentedmodel.getValueAt(r, 13).toString(), i, o);
                            model.setValueAt(indentedmodel.getValueAt(r, 9).toString(), i, 1);
                            ujsor = false;
                            break;
                        }
//ha nem találunk akkor hozzá kell adni egy sort

                    }
//ha nem írtunk , ezért kell egy uj sort hozzáadni akkor ezt tesszük
                    if (ujsor) {
                        model.addRow(new Object[model.getColumnCount()]);
                        model.setValueAt(comp, model.getRowCount() - 1, 0);
                        model.setValueAt(indentedmodel.getValueAt(r, 13).toString(), model.getRowCount() - 1, o);
                        model.setValueAt(indentedmodel.getValueAt(r, 9).toString(), model.getRowCount() - 1, 1);

                    }

                }

            }

        }

        jTable7.setModel(model);
        TablaOszlopSzelesseg(jTable7);

    }

    public void OpenPoCalc() {

//kiszámoljuk az open po-kat
//végigszaladunk a ctb táblán
        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel demandmodel = new DefaultTableModel();
        demandmodel = (DefaultTableModel) jTable4.getModel();

        for (int i = 0; i < ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk a demand táblán
            for (int d = 0; d < demandmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e a demand tábla pn-el és booked a státusz hozzáadjuk a darabot a darabhoz
                try {
                    if ((ctbmodel.getValueAt(i, 0).toString().equals(demandmodel.getValueAt(d, 7).toString()) && (demandmodel.getValueAt(d, 12).toString().equals("Booked")))) {

                        osszeg += Integer.parseInt(demandmodel.getValueAt(d, 17).toString());

                    }
                } catch (Exception e) {
                }

            }

            ctbmodel.setValueAt(osszeg, i, 1);

        }

        jTable1.setModel(ctbmodel);

    }

    public void StockCalc() {

//végigszaladunk a ctb táblán
        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < ohmodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el 
                try {
                    if ((ctbmodel.getValueAt(i, 0).toString().equals(ohmodel.getValueAt(d, 2).toString()) && (ohmodel.getValueAt(d, 4).toString().equals("Net-Asset")))) {

                        osszeg += Integer.parseInt(ohmodel.getValueAt(d, 6).toString());

                    }
                } catch (Exception e) {
                }

            }

            ctbmodel.setValueAt(osszeg, i, 2);

        }

        jTable1.setModel(ctbmodel);

    }

    public void OpenOrderCalc() {

//végigszaladunk a ctb táblán
        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel womodel = new DefaultTableModel();
        womodel = (DefaultTableModel) jTable5.getModel();

        for (int i = 0; i < ctbmodel.getRowCount(); i++) {
            int osszeg = 0;
//és végigszaladunk az OH táblán
            for (int d = 0; d < womodel.getRowCount(); d++) {
//ha egyezik a ctb tábla pn e az OH tábla pn-el 
                try {
                    if ((ctbmodel.getValueAt(i, 0).toString().equals(womodel.getValueAt(d, 1).toString()) && (womodel.getValueAt(d, 8).toString().contains("Released")))) {

                        osszeg += Integer.parseInt(womodel.getValueAt(d, 17).toString());

                    }
                } catch (Exception e) {
                }

            }

            ctbmodel.setValueAt(osszeg, i, 3);

        }

        jTable1.setModel(ctbmodel);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jTextField9 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();

        jMenuItem9.setText("Szűrő");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem9);

        jMenuItem8.setText("Szűrő ki");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem8);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setName("BOM_Table"); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Open Order", "Stock", "Open JOB", "Need to build", "CTB", "TERV"
            }
        ));
        jTable1.setCellSelectionEnabled(true);
        jTable1.setComponentPopupMenu(jPopupMenu2);
        jTable1.setName("CTB_Table"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setMinWidth(5);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(3).setMinWidth(5);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(4).setMinWidth(5);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(5).setMinWidth(5);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(6).setMinWidth(5);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(5);
        }

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 255, 51));

        jLabel2.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel2.setText("Üdv");

        jLabel4.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 51));
        jLabel4.setText("Allokáció nincs mentve!");

        jLabel5.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setText("Onhand nincs mentve!");

        jLabel6.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 51));
        jLabel6.setText("Workorders nincs mentve!");

        jLabel7.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("Demand nincs mentve!");

        jLabel8.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 51));
        jLabel8.setText("Indented BOM nincs mentve!");

        jLabel9.setText("Kereső:");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTable9.setAutoCreateRowSorter(true);
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PartNumber", "Qty", "Description", "Raw OH", "Master Comment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.setToolTipText("A Raw OH és a Komment szerekszthető!");
        jTable9.setCellSelectionEnabled(true);
        jTable9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable9KeyPressed(evt);
            }
        });
        jScrollPane9.setViewportView(jTable9);
        if (jTable9.getColumnModel().getColumnCount() > 0) {
            jTable9.getColumnModel().getColumn(1).setMinWidth(20);
            jTable9.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable9.getColumnModel().getColumn(1).setMaxWidth(150);
            jTable9.getColumnModel().getColumn(3).setMinWidth(20);
            jTable9.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTable9.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/ctb.jpg"))); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(1797, 893));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1793, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel9)
                        .addGap(6, 6, 6)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(393, 393, 393)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addComponent(jLabel5)
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addGap(6, 6, 6)
                .addComponent(jLabel7)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel9))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(4, 4, 4))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jLabel8))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("CTB", jPanel1);

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Warehouse", "Location", "Part", "Site ", "Type", "Date", "Quantity", "Unit Cost", "Inventory Store", "Part Description"
            }
        ));
        jTable2.setName("OH_Table"); // NOI18N
        jScrollPane2.setViewportView(jTable2);

        jLabel3.setText("Kereső:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1722, Short.MAX_VALUE)
                .addGap(73, 73, 73))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2))
        );

        jTabbedPane1.addTab("OnHand", jPanel3);

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Scheduled Receipt", "Scheduled Receipt Line", "Assembly", "Order Type", "Part", "Site", "Base Key", "Due Date", "Total Quantity", "Quantity Issued", "In Kit", "Remaining Quantity", "Usage Factor", "WORoutOpSeq", "WIPSupplyType", "WOSupplySubinv", "WOSupplyLoc"
            }
        ));
        jTable3.setCellSelectionEnabled(true);
        jTable3.setName("Alloc_Table"); // NOI18N
        jScrollPane3.setViewportView(jTable3);
        jTable3.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jLabel11.setText("Kereső:");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1726, Short.MAX_VALUE)
                .addGap(69, 69, 69))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Allocation", jPanel4);

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order Customer Id", "Order Customer Name", "Order", "Line", "Order Index", "Order Type", "Order Site", "Part", "Site", "Reference", "Priority", "PrioritySequence", "Status", "Customer", "Due Date", "Promised Date", "Request Date", "Quantity", "Shipped Qty", "Unit Selling Price", "CustomerPO", "CustomerPOLine", "SO More Information 1", "Family", "Forecast Extension"
            }
        ));
        jTable4.setCellSelectionEnabled(true);
        jTable4.setName("Demand_Table"); // NOI18N
        jScrollPane4.setViewportView(jTable4);

        jLabel12.setText("Kereső:");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1730, Short.MAX_VALUE)
                .addGap(65, 65, 65))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Demand", jPanel5);

        jTable5.setAutoCreateRowSorter(true);
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer", "Part", "Description", "Plant", "Planner", "WorkOrder", "Type", "WO CLASS", "Status", "Priority", "PrioritySequence", "Start", "Available", "Calculated", "Demand", "DueDate", "Available", "RemainingQty", "TotalQty", "Action", "Notes", "QtyScrapped", "BuildInWIP", "CatSet CustReporting"
            }
        ));
        jTable5.setCellSelectionEnabled(true);
        jTable5.setName("WO_Table"); // NOI18N
        jScrollPane5.setViewportView(jTable5);

        jLabel13.setText("Kereső:");

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1730, Short.MAX_VALUE)
                .addGap(65, 65, 65))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Workorders", jPanel6);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Assembly", "Assembly ORA Type", "Site", "Parent", "Parent ORA Type", "Level", "Op Seq", "Component", "Component ORA TYPE", "Description", "Component Site", "wip supply type", "Supply Type", "Quantity Per", "Change", "In", "Out"
            }
        ));
        jTable6.setCellSelectionEnabled(true);
        jTable6.setName("Indented_Table"); // NOI18N
        jTable6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable6KeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jLabel14.setText("Kereső:");

        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1712, Short.MAX_VALUE)
                .addGap(83, 83, 83))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Indented BOM", jPanel7);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Components", "Description"
            }
        ));
        jTable7.setCellSelectionEnabled(true);
        jTable7.setName("BOM_Table"); // NOI18N
        jScrollPane7.setViewportView(jTable7);

        jLabel15.setText("Kereső:");

        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1739, Short.MAX_VALUE)
                .addGap(56, 56, 56))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BOM (Generated)", jPanel8);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Component", "QoH", "Wo War (Rem. Qty)", "New Prod", "Total OH", "Desc", "Master Comment"
            }
        ));
        jTable8.setName("CalcOH_Table"); // NOI18N
        jScrollPane8.setViewportView(jTable8);

        jLabel10.setText("Kereső:");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1712, Short.MAX_VALUE)
                .addGap(83, 83, 83))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OH (Calculated)", jPanel9);

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PartNumber", "WK1", "WK2", "WK3", "WK4", "WK5", "WK6", "WK7", "WK8"
            }
        ));
        jTable10.setCellSelectionEnabled(true);
        jScrollPane10.setViewportView(jTable10);
        if (jTable10.getColumnModel().getColumnCount() > 0) {
            jTable10.getColumnModel().getColumn(0).setMinWidth(100);
            jTable10.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable10.getColumnModel().getColumn(0).setMaxWidth(500);
        }

        jButton1.setText("Sorok hozzáadása");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Számolás a beírt mennyiségekkel és PN ekkel!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel17.setText("Kereső:");

        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField10KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 1040, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel17)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 974, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Gyártandó mennyiségek megadása", jPanel10);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jMenu1.setText("Beállítások");

        jMenuItem1.setText("PartNumber kezelő");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Feltöltések");

        jMenuItem2.setText("Allocation");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("On Hand");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Work Orders All");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Independent Demand");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Indented BOM");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Eszközök");

        jMenuItem12.setText("Adatok importálása");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem12);

        jMenuItem11.setText("Adatok betöltése a táblákba az adatbázisból");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem7.setText("BOM kalkuláció");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem10.setText("Visszaállít az alapadatokból");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Scenario");

        jMenuItem13.setText("Scenario mentése");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem13);

        jMenuItem14.setText("Scenario betöltése");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //megnyitunk egy pn kezelőt 
        CTB_Pnkezelo pn;
        try {
            pn = new CTB_Pnkezelo(this);
            pn.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    public void FillOhTable() throws SQLException, ClassNotFoundException {

        String query = "select * from CTB_Onhand where user = '" + c.jTextField1.getText() + "'";
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        planconnect pc = new planconnect();

        pc.lekerdez(query);
        while (pc.rs.next()) {

            model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5), pc.rs.getString(6), pc.rs.getString(7), pc.rs.getString(8), pc.rs.getString(9), pc.rs.getString(10)});

        }
        jTable2.setModel(model);
        pc.kinyir();
        TablaOszlopSzelesseg(jTable2);
    }

    public void FillAllocTable() throws SQLException, ClassNotFoundException {

        String query = "select * from CTB_Allocation where user = '" + c.jTextField1.getText() + "'";
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
        planconnect pc = new planconnect();

        pc.lekerdez(query);
        while (pc.rs.next()) {

            model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5), pc.rs.getString(6), pc.rs.getString(7), pc.rs.getString(8), pc.rs.getString(9), pc.rs.getString(10), pc.rs.getString(11), pc.rs.getString(12), pc.rs.getString(13), pc.rs.getString(14), pc.rs.getString(15), pc.rs.getString(16), pc.rs.getString(17)});

        }
        jTable3.setModel(model);
        pc.kinyir();
        TablaOszlopSzelesseg(jTable3);
    }

    public void Ohcalc(int miindit /*ha egy akkor csak egy részét futtatjuk , 0-a futtatja az egészet!!*/) {
////az indented bumnak letrehozunk egy tombot
//
//        String[][] indenttomb = new String[jTable6.getRowCount()][3];
//        for (int ind = 0; ind < jTable6.getRowCount(); ind++) {
//            try {
//                indenttomb[ind][0] = jTable6.getValueAt(ind, 0).toString(); //assembly
//                indenttomb[ind][1] = jTable6.getValueAt(ind, 7).toString(); //Component
//                indenttomb[ind][2] = jTable6.getValueAt(ind, 13).toString(); //qty/
//            } catch (Exception e) {
//            }
//
//        }

//létrehozunk egy tömböt a calculált bom táblának
        String[][] calcbom = new String[jTable7.getRowCount()][jTable7.getColumnCount()];
//beletesszük az adatokat
        for (int r = 0; r < jTable7.getRowCount(); r++) {

            for (int o = 0; o < jTable7.getColumnCount(); o++) {
                try {
                    calcbom[r][o] = jTable7.getValueAt(r, o).toString();
                } catch (Exception e) {
                }

            }

        }

//a ctb tablanak is letrehozunk egy tombot
        String[][] ctbtomb = new String[jTable1.getRowCount()][2];
        for (int ctb = 0; ctb < jTable1.getRowCount(); ctb++) {
            String tervdarab = "0";
            ctbtomb[ctb][0] = jTable1.getValueAt(ctb, 0).toString();//pn
            try {
                tervdarab = jTable1.getValueAt(ctb, 6).toString();  //terv darab
            } catch (Exception e) {
            }
            ctbtomb[ctb][1] = tervdarab;
        }

//ez szamolja ki a valóvab felhasználható alkatrész mennyiséget
//a táblában létrehozunk annyi sort amennyi a BOM tábla sora és beírjuk a PN eket is rögvest 
        DefaultTableModel ohtabla = new DefaultTableModel();
        ohtabla = (DefaultTableModel) jTable8.getModel();

        if (miindit == 0) {

            ohtabla.setRowCount(0);
            for (int i = 0; i < jTable7.getRowCount(); i++) {  //jtable7  ez a bom tábla

                ohtabla.addRow(new Object[ohtabla.getColumnCount()]);
                ohtabla.setValueAt(jTable7.getValueAt(i, 0).toString(), i, 0);

            }

            for (int i = 0; i < ohtabla.getRowCount(); i++) {
                int osszeg = 0;
                String desc = "";
//vegigjarjuk az oh táblát és kiszedjük az összesített oh-t
                for (int o = 0; o < jTable2.getRowCount(); o++) {
                    try {
                        if ((ohtabla.getValueAt(i, 0).toString().equals(jTable2.getValueAt(o, 2).toString())) && (jTable2.getValueAt(o, 4).equals("Net-Asset"))) {
//kiszedjük a buzi vesszőt a stringből
                            try {
                                desc = jTable2.getValueAt(o, 9).toString();
                            } catch (Exception e) {
                            }
                            try {
                                osszeg += Math.round(Float.valueOf(jTable2.getValueAt(o, 6).toString().replace(",", "")).intValue());
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }

                }

                ohtabla.setValueAt(osszeg, i, 1);
                ohtabla.setValueAt(desc, i, 5);

//kiszedjük az elallokált mennyiségeket is
                osszeg = 0;
                for (int o = 0; o < jTable3.getRowCount(); o++) {

                    try {
                        if (ohtabla.getValueAt(i, 0).toString().equals(jTable3.getValueAt(o, 4).toString())) {

                            osszeg += Integer.parseInt(jTable3.getValueAt(o, 11).toString());
                        }
                    } catch (Exception e) {
                    }

                }

                ohtabla.setValueAt(osszeg, i, 2);

            }

        }
//elindulunk bejárni az OH táblát
        for (int oh = 0; oh < ohtabla.getRowCount(); oh++) {
            int osszeg = 0;
//elkezdjük bejárni a ctb tábla pn oszlopát is
            for (int ctb = 0; ctb < ctbtomb.length; ctb++) {

//ha van beírva darab az adott pn hez
                if (!ctbtomb[ctb][1].equals("0")) {

//elkezdjük forgatni a calcbom tömböt , és ha egyezik a component tovább megyünk
                    for (int bom = 0; bom < calcbom.length; bom++) {

                        if (calcbom[bom][0].trim().equals(ohtabla.getValueAt(oh, 0).toString().trim())) {
                            //elkezdjük forgatni a bom oszlopait is és megkeressük a pn-t

                            for (int pnbom = 0; pnbom < calcbom[1].length; pnbom++) {

                                //ha egyezik a pn összeadjuk
                                if (jTable7.getColumnName(pnbom).trim().equals(ctbtomb[ctb][0].toString().trim())) {

                                    try {
                                        osszeg += ((Integer.parseInt(ctbtomb[ctb][1]))) * (Math.round(Float.valueOf(calcbom[bom][pnbom].toString()).intValue()));
                                        break;
                                    } catch (Exception e) {
                                    }

                                }

                            }

                        }

                    }

////elkezdjük bejárni az intendet tömböt is
//                    for (int ind = 0; ind < indenttomb.length; ind++) {
////ha találkozunk olyan sorral ahol az intended tömbben a pn egyenlő a ctb tömb pn ével és az intended tömb componente egyenlő az oh tábla componentével akkor számolunk
//                        if (indenttomb[ind][0].toString().trim().equals(ctbtomb[ctb][0].toString().trim()) && indenttomb[ind][1].toString().trim().equals(ohtabla.getValueAt(oh, 0).toString().trim())) {
//                            try {
//                                osszeg += ((Integer.parseInt(ctbtomb[ctb][1]))) * (Math.round(Float.valueOf(indenttomb[ind][2].toString()).intValue()));
//                                break;
//                            } catch (Exception e) {
//                            }
//                        }
//
//                    }
                }

            }

            ohtabla.setValueAt(osszeg, oh, 3);

        }

        System.out.println();

//ki kell számolni a végleges felhasználható oh-t is
        for (int i = 0; i < ohtabla.getRowCount(); i++) {

            int osszeg = Integer.parseInt(ohtabla.getValueAt(i, 1).toString()) - Integer.parseInt(ohtabla.getValueAt(i, 2).toString()) - Integer.parseInt(ohtabla.getValueAt(i, 3).toString());
            ohtabla.setValueAt(osszeg, i, 4);
        }

        jTable8.setModel(ohtabla);
        TablaOszlopSzelesseg(jTable8);

    }

//legkisebb szam legyen a ctb
    public void CtbKalk() {

//megkeressük a legkisebb számot a pn ek közül
        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel bommodel = new DefaultTableModel();
        bommodel = (DefaultTableModel) jTable7.getModel();
        for (int i = 0; i < ctbmodel.getRowCount(); i++) {
            int legkisebb = 999999999;

            int aktualis;

            for (int o = 7; o < ctbmodel.getColumnCount(); o++) {
                try {
                    aktualis = Integer.parseInt(ctbmodel.getValueAt(i, o).toString());

                    if (aktualis < legkisebb) {

                        legkisebb = aktualis;

                    }
                } catch (Exception e) {
                }

            }
//ha 999999 a szám az azt jelenti , hogy nincs minden rendben a bom al
            if (legkisebb == 999999999) {

                ctbmodel.setValueAt("BOM hiba!", i, 5);

            } else {

                ctbmodel.setValueAt(legkisebb, i, 5);
            }
        }

        jTable1.setModel(ctbmodel);
    }

    public void FillDemandTable() throws SQLException, ClassNotFoundException {

        String query = "select * from CTB_Demand where user = '" + c.jTextField1.getText() + "'";
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        planconnect pc = new planconnect();

        pc.lekerdez(query);
        while (pc.rs.next()) {

            model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5), pc.rs.getString(6), pc.rs.getString(7), pc.rs.getString(8), pc.rs.getString(9), pc.rs.getString(10), pc.rs.getString(11), pc.rs.getString(12), pc.rs.getString(13), pc.rs.getString(14), pc.rs.getString(15), pc.rs.getString(16), pc.rs.getString(17), pc.rs.getString(18), pc.rs.getString(19), pc.rs.getString(20), pc.rs.getString(21), pc.rs.getString(22), pc.rs.getString(23), pc.rs.getString(24), pc.rs.getString(25)});

        }
        jTable4.setModel(model);
        pc.kinyir();
        TablaOszlopSzelesseg(jTable4);
    }

    public void FillWoTable() throws SQLException, ClassNotFoundException {

        String query = "select * from CTB_Workorders where user = '" + c.jTextField1.getText() + "'";
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable5.getModel();
        model.setRowCount(0);
        planconnect pc = new planconnect();

        pc.lekerdez(query);
        while (pc.rs.next()) {

            model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5), pc.rs.getString(6), pc.rs.getString(7), pc.rs.getString(8), pc.rs.getString(9), pc.rs.getString(10), pc.rs.getString(11), pc.rs.getString(12), pc.rs.getString(13), pc.rs.getString(14), pc.rs.getString(15), pc.rs.getString(16), pc.rs.getString(17), pc.rs.getString(18), pc.rs.getString(19), pc.rs.getString(20), pc.rs.getString(21), pc.rs.getString(22), pc.rs.getString(23), pc.rs.getString(24)});

        }
        jTable5.setModel(model);
        pc.kinyir();
        TablaOszlopSzelesseg(jTable5);
    }

    public void FillBomTable() throws SQLException, ClassNotFoundException {

        String query = "select * from CTB_IndentedBom where user = '" + c.jTextField1.getText() + "'";
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable6.getModel();
        model.setRowCount(0);
        planconnect pc = new planconnect();

        pc.lekerdez(query);
        while (pc.rs.next()) {

            model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5), pc.rs.getString(6), pc.rs.getString(7), pc.rs.getString(8), pc.rs.getString(9), pc.rs.getString(10), pc.rs.getString(11), pc.rs.getString(12), pc.rs.getString(13), pc.rs.getString(14), pc.rs.getString(15), pc.rs.getString(16), pc.rs.getString(17)});

        }
        jTable6.setModel(model);
        pc.kinyir();
        TablaOszlopSzelesseg(jTable6);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        //allokáció feltöltése

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            path = path.replace("\\", "\\\\");
//truncate table
            String query = "delete from CTB_Allocation where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path + "' \n"
                    + "INTO TABLE CTB_Allocation\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);

            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

            pc.kinyir();

        }


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        //oh feltöltés
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            path = path.replace("\\", "\\\\");
//truncate table
            String query = "delete from CTB_Onhand where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path + "' \n"
                    + "INTO TABLE CTB_Onhand\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);
            pc.kinyir();
            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // demand feltöltése
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            path = path.replace("\\", "\\\\");
//truncate table
            String query = "delete from CTB_Demand where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path + "' \n"
                    + "INTO TABLE CTB_Demand\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);
            pc.kinyir();
            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // wo all
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            path = path.replace("\\", "\\\\");
//truncate table
            String query = "delete from CTB_Workorders where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path + "' \n"
                    + "INTO TABLE CTB_Workorders\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);
            pc.kinyir();
            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jMenuItem4ActionPerformed

    public void NeedToBuild() {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            //openorder
            int openorder = 0;
            int stock = 0;
            int openjob = 0;
            int terv = 0;

            try {
                openorder = Integer.parseInt(model.getValueAt(i, 1).toString());
            } catch (Exception e) {
            }
            try {
                stock = Integer.parseInt(model.getValueAt(i, 2).toString());
            } catch (Exception e) {
            }
            try {
                openjob = Integer.parseInt(model.getValueAt(i, 3).toString());
            } catch (Exception e) {
            }
            try {
                terv = Integer.parseInt(model.getValueAt(i, 6).toString());
            } catch (Exception e) {
            }

            model.setValueAt(openorder - (terv + openjob + stock), i, 4);

        }

        jTable1.setModel(model);

    }

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        //indented bom

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            path = path.replace("\\", "\\\\");
//truncate table
            String query = "delete from CTB_IndentedBom where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path + "' \n"
                    + "INTO TABLE CTB_IndentedBom\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);
            pc.kinyir();
            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        //bom kalkuláció

        BomCalc();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // oh tabla szuro
        universalfilter uf = new universalfilter(jTextField1.getText().trim(), jTable2);

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField2.getText().trim(), jTable1);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField3.getText().trim(), jTable8);
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField4.getText().trim(), jTable3);
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField5.getText().trim(), jTable4);
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField6.getText().trim(), jTable5);
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField7.getText().trim(), jTable6);
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField8.getText().trim(), jTable7);
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jTable6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable6KeyPressed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        //kikapcsoljuk az editálásokat

        if (jTable9.isEditing()) {

            jTable9.getCellEditor().stopCellEditing();

        }
        if (jTable1.isEditing()) {

            jTable1.getCellEditor().stopCellEditing();

        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            jTable1.revalidate();
            int sor = jTable1.getSelectedRow();
            int oszlop = jTable1.getSelectedColumn();

            try {
                jTable1.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            };
            Ohcalc(1);
            CompToCtb();
            CtbKalk();
            NeedToBuild();
            TablaOszlopSzelesseg(jTable1);
            jTable1.setRowSelectionInterval(sor, sor);
            jTable1.setColumnSelectionInterval(oszlop, oszlop);

        }


    }//GEN-LAST:event_jTable1KeyPressed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            // az adatok lekérése a táblákba az adatbázisból
            UploadsDate();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FillOhTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FillAllocTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FillDemandTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FillWoTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FillBomTable();
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased

        //alpagszuro();
        topshorts();


    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //alpagszuro();
        if (jTable9.isEditing()) {

            jTable9.getCellEditor().stopCellEditing();

        }
        topshorts();

    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // 

        alpagszuro(jMenuItem9);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        alpagszuro(jMenuItem8);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jTable9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable9KeyPressed

        jTable9.revalidate();

        if (jTable9.isEditing()) {

            jTable9.getCellEditor().stopCellEditing();

        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // updateljuk az oh táblát

            DefaultTableModel ohmodel = new DefaultTableModel();
            ohmodel = (DefaultTableModel) jTable8.getModel();
            DefaultTableModel shortmodel = new DefaultTableModel();
            shortmodel = (DefaultTableModel) jTable9.getModel();

            for (int i = 0; i < shortmodel.getRowCount(); i++) {

                String pn = shortmodel.getValueAt(i, 0).toString();
                String qty = shortmodel.getValueAt(i, 3).toString();
                String komment = "";
                try {
                    komment = shortmodel.getValueAt(i, 4).toString();
                } catch (Exception e) {
                }

//bepörgetjük a nagy táblát és ha találjuk a pn-t beállítjuk a darabokat
                for (int k = 0; k < ohmodel.getRowCount(); k++) {

                    if (ohmodel.getValueAt(k, 0).toString().equals(pn)) {

                        ohmodel.setValueAt(qty, k, 1);
                        ohmodel.setValueAt(komment, k, 6);
                        break;

                    }
                }

            }

            jTable8.setModel(ohmodel);
            Ohcalc(1);
            CompToCtb();
            CtbKalk();
            NeedToBuild();
            TablaOszlopSzelesseg(jTable1);

        }
    }//GEN-LAST:event_jTable9KeyPressed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // OH kalkulácio
        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // összes adat behúzása egyszerre
        //allokáció feltöltése

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            //String path = chooser.getCurrentDirectory().getAbsolutePath();
            String path1 = path.replace("\\", "\\\\");
            path1 += "\\\\Alloc.tab";

//truncate table
            String query = "delete from CTB_Allocation where user = '" + CTB.user + "'";
            planconnect pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path1 + "' \n"
                    + "INTO TABLE CTB_Allocation\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, false);
//onhand
            String path2 = path.replace("\\", "\\\\");
            path2 += "\\\\Onhand.tab";
            query = "delete from CTB_Onhand where user = '" + CTB.user + "'";
            pc = new planconnect();
            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path2 + "' \n"
                    + "INTO TABLE CTB_Onhand\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, false);
//workorders            

            String path3 = path.replace("\\", "\\\\");
            path3 += "\\\\Workorders.tab";
            query = "delete from CTB_Workorders where user = '" + CTB.user + "'";

            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path3 + "' \n"
                    + "INTO TABLE CTB_Workorders\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, false);
//demand
            String path4 = path.replace("\\", "\\\\");
            path4 += "\\\\Demand.tab";
            query = "delete from CTB_Demand where user = '" + CTB.user + "'";

            pc.feltolt(query, false);
            query = "LOAD DATA LOCAL INFILE '" + path4 + "' \n"
                    + "INTO TABLE CTB_Demand\n"
                    + "FIELDS TERMINATED BY '\\t' \n"
                    + "ESCAPED BY '\\b' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\r\\n'\n"
                    + "IGNORE 2 LINES \n"
                    + "set user = '" + CTB.user + "'";
            pc.feltolt(query, true);

            pc.kinyir();
            try {
                UploadsDate();
            } catch (SQLException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();


    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // scenario mentése

        CTB_Scenario c = new CTB_Scenario(this);
        try {
            c.ScenarioSave();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // scenario beolvasása
        CTB_Scenario s = new CTB_Scenario(this);
        try {
            s.ScenarioLoad();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // sorok hozzáadása az igény táblához
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable10.getModel();
        //kiszedjük a textboxból a számot
        try {
            int sorszam = Integer.parseInt(jTextField9.getText());

            for (int i = 0; i < sorszam; i++) {

                model.addRow(new Object[jTable10.getColumnCount()]);

            }

        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox("Nem adtál meg számot!", "Hiba$");

        }

        jTable10.setModel(model);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //számolás a beírt pn ekkel és mennyiségekkel...

        //létrehozunk egy tömböt a beírt pn ekből és mennyiségekből
        String[][] adatok = new String[jTable10.getRowCount()][2];

//bejárjuk a táblát és feltöltjük a tömböt
        for (int i = 0; i < jTable10.getRowCount(); i++) {
            try {
                adatok[i][0] = jTable10.getValueAt(i, 0).toString().trim();
            } catch (Exception e) {
            }

//végigjárjuk az odzlopokat és összeadjuk a mennyiséget
            int osszeg = 0;
            for (int o = 0; o < jTable10.getColumnCount(); o++) {
                try {
                    osszeg += Integer.parseInt(jTable10.getValueAt(i, o).toString());
                } catch (Exception e) {
                }
            }

            adatok[i][1] = String.valueOf(osszeg);

        }

        //kinullázzuk a ctb táblát
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        model.setColumnCount(7);
        model.setRowCount(0);

//akkor most hozzáadjuk az uj adatokat a ctb modellhez
        for (int i = 0; i < adatok.length; i++) {
            if (adatok[i][0] != null) {
                model.addRow(new Object[]{adatok[i][0], "", "", "", "", "", adatok[i][1]});
            }

        }

        jTable1.setModel(model);

//lefuttatjuk az összes cuccot a pn beillesztésen kívül
        OpenPoCalc();
        StockCalc();
        OpenOrderCalc();
//a pn-jeink bom matrixat rakja össze
        BomCalc();
//ez kiszámolja a felhasználható oh mennyiséget  (lasúúúúúúúú)
        Ohcalc(0);

//kiteszi a ctb táblába azokat az alkatrészeket amiket használ a termék
        CompToCtb();
//kitölti a ctb táblába , hogy hány termékre vagyunk tiszták
        CtbKalk();
        NeedToBuild();
        SaveCtbTableData();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyPressed
        // kereső a gyártandó mennyiségek hozzáadásánál

        universalfilter u = new universalfilter(jTextField10.getText().trim(), jTable10);
    }//GEN-LAST:event_jTextField10KeyPressed

    public void topshorts() {
//betesszük a használatos anyagokat atáblába

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel shortmodel = new DefaultTableModel();
        shortmodel = (DefaultTableModel) jTable9.getModel();
        shortmodel.setRowCount(0);
        String[][] tmppns = new String[model.getColumnCount() - 7][2];

//betesszük az adatokat a tömbbe , első a pn , aztán a darab
        for (int i = 7; i < model.getColumnCount(); i++) {

            tmppns[i - 7][0] = model.getColumnName(i);

            try {
                tmppns[i - 7][1] = model.getValueAt(jTable1.getSelectedRow(), i).toString();
                if (tmppns[i - 7][1].toString().equals("")) {

                    tmppns[i - 7][1] = "99999999";

                }

            } catch (Exception e) {

                tmppns[i - 7][1] = "99999999";

            }

        }

//most kéne buborékrendezni..
        int i;
        int j;
        String darabszam;
        String partnumber;
        for (i = tmppns.length - 1; 0 < i; --i) {
            for (j = 0; j < i; ++j) {
                if (Integer.parseInt(tmppns[j][1].toString()) > Integer.parseInt(tmppns[j + 1][1].toString())) {
                    // csere
                    partnumber = tmppns[j][0].toString();
                    darabszam = tmppns[j][1].toString();

                    tmppns[j][0] = tmppns[j + 1][0];
                    tmppns[j][1] = tmppns[j + 1][1];

                    tmppns[j + 1][0] = partnumber;
                    tmppns[j + 1][1] = darabszam;

                }
            }
        }

        //betesszük a shortmodelbe
        for (i = 0; i < tmppns.length; i++) {

            if (!tmppns[i][1].toString().equals("99999999")) {
                shortmodel.addRow(new Object[]{tmppns[i][0], tmppns[i][1]});
            }

        }

        jTable9.setModel(shortmodel);

//megkereseeük a pn ek leírását és beállítjuk a táblába
        DefaultTableModel rawohmodel = new DefaultTableModel();
        rawohmodel = (DefaultTableModel) jTable8.getModel();
        for (i = 0; i < shortmodel.getRowCount(); i++) {

            for (int k = 0; k < rawohmodel.getRowCount(); k++) {

                if (shortmodel.getValueAt(i, 0).toString().equals(rawohmodel.getValueAt(k, 0).toString())) {

                    shortmodel.setValueAt(rawohmodel.getValueAt(k, 5), i, 2);
                    shortmodel.setValueAt(rawohmodel.getValueAt(k, 1), i, 3);
                    shortmodel.setValueAt(rawohmodel.getValueAt(k, 6), i, 4);

                }
            }

        }

        jTable9.setModel(shortmodel);

        //TablaOszlopSzelesseg(jTable9);
    }

    void UploadsDate() throws SQLException, ClassNotFoundException {

        String query = "SELECT distinct Timestamp FROM planningdb.CTB_Allocation where user = '" + c.jTextField1.getText() + "' order by Timestamp desc";
        planconnect pc = new planconnect();
        pc.lekerdez(query);
        while (pc.rs.next()) {

            jLabel4.setText("Alloc mentve: " + pc.rs.getString(1));

        }

        query = "SELECT distinct Timestamp FROM planningdb.CTB_Workorders where user = '" + c.jTextField1.getText() + "' order by Timestamp desc";
        pc.lekerdez(query);
        while (pc.rs.next()) {

            jLabel6.setText("Workorders mentve: " + pc.rs.getString(1));

        }

        query = "SELECT distinct Timestamp FROM planningdb.CTB_Onhand where user = '" + c.jTextField1.getText() + "' order by Timestamp desc";
        pc.lekerdez(query);
        while (pc.rs.next()) {

            jLabel5.setText("Onhand mentve: " + pc.rs.getString(1));

        }

        query = "SELECT distinct Timestamp FROM planningdb.CTB_Demand where user = '" + c.jTextField1.getText() + "' order by Timestamp desc";
        pc.lekerdez(query);
        while (pc.rs.next()) {

            jLabel7.setText("Demand mentve: " + pc.rs.getString(1));

        }
        query = "SELECT distinct Timestamp FROM planningdb.CTB_IndentedBom where user = '" + c.jTextField1.getText() + "' order by Timestamp desc";
        pc.lekerdez(query);
        while (pc.rs.next()) {

            jLabel8.setText("Indented BOM mentve: " + pc.rs.getString(1));

        }

        pc.kinyir();

    }

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlannTool/PICTURES/ctb1.png")));

    }

    //kell egy metódus ami kilistázza az anyagokat a ctb oldalra a pn-ek után
    public void CompToCtb() {

        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();
        ctbmodel.setColumnCount(7);
        DefaultTableModel bommodel = new DefaultTableModel();
        bommodel = (DefaultTableModel) jTable7.getModel();
        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable8.getModel();

        //hozzáadjuk első körben az összes pn-t columnként ami szerepel a BOM mátrixban, és felveszem pn ként
        for (int i = 0; i < bommodel.getRowCount(); i++) {
            String component = bommodel.getValueAt(i, 0).toString();
            ctbmodel.addColumn(component);

//forgatom a ctbmodelt is , és megnézem , hogy az adott pn hez tartozik e ez az alkatrész
            for (int ctb = 0; ctb < ctbmodel.getRowCount(); ctb++) {
                String pn = ctbmodel.getValueAt(ctb, 0).toString();

                for (int bomoszlop = 0; bomoszlop < bommodel.getColumnCount(); bomoszlop++) {

                    if (bommodel.getColumnName(bomoszlop).trim().equals(pn) && bommodel.getValueAt(i, bomoszlop) != null) {
//beforgatjuk az oh táblát és kiszedjük azt az összeget ami a használható (utolsó oszlop)
                        int ohosszeg = 0;
                        for (int oh = 0; oh < ohmodel.getRowCount(); oh++) {
//ha ahoz a comphoz ertünk akkor kiszedjük az összeget
                            if (jTable8.getValueAt(oh, 0).toString().equals(component)) {

                                ohosszeg = Integer.parseInt(ohmodel.getValueAt(oh, 4).toString());
                                //elosztjuk a felhasznalhao oh-t azzal a mennyiséggel amivel az alapanyag épül az adott termékbe
                                try {
                                    ctbmodel.setValueAt(ohosszeg / Math.round(Float.valueOf(bommodel.getValueAt(i, bomoszlop).toString()).intValue()), ctb, 7 + i);
                                } catch (Exception e) {
                                }
                                break;

                            }
                        }

                    }

                }

            }

        }

        jTable1.setModel(ctbmodel);

        TablaOszlopSzelesseg(jTable1);

    }

    public void TablaOszlopSzelesseg(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
//            if (width > 300) {
//                width = 300;
//            }

//a column szelesseget is megvizsgaljuk
            int maxWidth = 0;
            TableColumn column1 = columnModel.getColumn(column);
            TableCellRenderer headerRenderer = column1.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = column1.getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);

            if (width > maxWidth) {
                columnModel.getColumn(column).setPreferredWidth(width);
            } else {
                columnModel.getColumn(column).setPreferredWidth(maxWidth);
            }
        }
    }

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
            java.util.logging.Logger.getLogger(CTB.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CTB.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CTB.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CTB.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new CTB().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    public static javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel6;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    public static javax.swing.JTable jTable6;
    public static javax.swing.JTable jTable7;
    public static javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
