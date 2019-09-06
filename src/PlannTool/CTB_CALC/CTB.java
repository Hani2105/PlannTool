/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.ANIMATIONS.animation;
import PlannTool.BACKEND.Tc_Betervezo;
import PlannTool.BACKEND.Tc_Stringbolint;
import PlannTool.CTB_CALC.CTB_Tablarenderer;
import PlannTool.CONNECTS.planconnect;
import PlannTool.CONNECTS.postgreconnect;
import PlannTool.ExcelAdapter;
import PlannTool.GAME.jatek;
import PlannTool.MyTabbedPaneUI;
import PlannTool.ablak;
import static PlannTool.ablak.jList2;
import static PlannTool.ablak.jTable4;
import static PlannTool.ablak.jTextField25;
import PlannTool.infobox;
import PlannTool.universalfilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import static oracle.net.aso.C11.f;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

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
    boolean link = false;
    public BoundedRangeModel buzimodel;
    public static File f;
//letezik e mar ohszerkeszto tabla
    public static boolean ohedit = false;
//az ohedit miatt kell rögzíteni h melyik sorban vagyunk
    public static int selectedrow;
    public static int selectedcolumn;
//riportok elérési utja
    public static String scenpath = "";
    public static String riportpath = "";
//a pn hez tartozó adatok tömbje amit tooltipbe teszünk ki
    public static ArrayList<String[]> PnDatas = new ArrayList<>();
//ez lesz a shorttáblából lekérdezett pn készlet adata
    public static Object[][] WipStockData = null;
    public static Object[][] OraStockData = null;

    public CTB(CTB_Bejel c) throws SQLException, ClassNotFoundException, IOException {

        this.c = c;

        initComponents();
        this.jTabbedPane1.setUI(new CTB_TabbedUI(jTabbedPane1));
        setExtendedState(MAXIMIZED_BOTH);
        seticon();

//beilleszthetővé tesszük az igények táblát
        new ExcelAdapter(jTable11);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setDefaultRenderer(Object.class, new CTB_Tablarenderer());
        jTable9.setDefaultRenderer(Object.class, new CTB_ShortTablarenderer());
        jTable11.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable11.setDefaultRenderer(Object.class, new CTB_PartsToPlanTableRenderer());
        jTable1.getTableHeader().setDefaultRenderer(new CTB_Columnrenderer());
        jTable11.getTableHeader().setDefaultRenderer(new CTB_Columnrenderer());
        jTable9.getTableHeader().setDefaultRenderer(new CTB_Columnrenderer());

        TablaOszlopSzelesseg(jTable1);
        TablaOszlopSzelesseg(jTable11);
        jTable1.setBackground(new Color(0, 0, 0, 0));
        jScrollPane1.setBackground(new Color(0, 0, 0, 0));
        jScrollPane1.setOpaque(false);
        jTable1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        jTable11.setBackground(new Color(0, 0, 0, 0));
        jScrollPane11.setBackground(new Color(0, 0, 0, 0));
        jScrollPane11.setOpaque(false);
        jTable11.setOpaque(false);
        jScrollPane11.getViewport().setOpaque(false);
        buzimodel = jScrollPane11.getVerticalScrollBar().getModel();
        kiszallitasleker();
        becellsleker();
//betöltjük az ini filet
        CTB_Ini i = new CTB_Ini(jTable11);
        i.Olvas();

//lekerjuk a PN adatokat a tooltiphez
        CTB_PnDatas p = new CTB_PnDatas();
        p.adatleker();

    }

    //Tervszamol
    public void tervszamol() {

        //kiszedjük a selected itemeket
        int[] selectedindex = CTB.jList2.getSelectedIndices();

        ArrayList<String> selecteditems = new ArrayList<>();
        for (int i = 0; i < selectedindex.length; i++) {

            selecteditems.add(CTB.jList2.getModel().getElementAt(selectedindex[i]));

        }

//vegigfutunk a listan és megprobálunk keresni ilyen tabot a backendtervezőben
        for (int i = 0; i < selecteditems.size(); i++) {
//áttesszük egy modelbe az adatokat
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) Tc_Betervezo.Besheets.get(selecteditems.get(i)).jTable2.getModel();
            int elsooszlop = 0;
//megkeressük az első oszlopot ahonnan el kell indulni számolni a tervet
            for (int c = 0; c < model.getColumnCount(); c++) {

                if (model.getColumnName(c).contains(CTB.jTextField14.getText())) {

                    elsooszlop = c;
                    break;

                }

            }

//innen bejárjuk a táblát jobbra és lefelé (figyelni a kalkulált oszlopra!!) és megpróbáljuk megkeresni a plan táblában szereplő pn eket ha nagyobb mint 0 az első oszlop!!
            if (elsooszlop > 0) {
                for (int p = 0; p < CTB.jTable11.getRowCount(); p++) {
                    String partnumber = CTB.jTable11.getValueAt(p, 0).toString();
                    int osszeg = 0;
                    for (int r = 0; r < model.getRowCount(); r++) {
//ha olyan sorba érünk ahol egyezik a pn (model 0) és nem tény a sor akkor bejárjuk az oszlopokat és megprobáljuk kiszedni az összeget
                        try {
                            if (partnumber.equals(model.getValueAt(r, 0).toString()) && !model.getValueAt(r, 3).toString().equals("Tény")) {

                                for (int c = elsooszlop; c < model.getColumnCount() - 1; c++) {

                                    osszeg += new Tc_Stringbolint(model.getValueAt(r, c).toString()).db;

                                }

                            }

                        } catch (Exception e) {
                        }

                    }
//beállítjuk a táblába

                    if (osszeg > 0) {

                        CTB.jTable11.setValueAt(osszeg, p, 1);

                    } else {
                        CTB.jTable11.setValueAt("", p, 1);
                    }

                }
            }
        }

    }

    //lekérjük a kiszállítások területének listáját és feltöltjük a jlistet
    public void kiszallitasleker() {

        postgreconnect pc = new postgreconnect();
        try {
            pc.lekerdez("select distinct \"HBPackage\".customer_type.name from \"HBPackage\".customer_type");
            DefaultListModel listModel = new DefaultListModel();

            listModel.removeAllElements();

            while (pc.rs.next()) {

                listModel.addElement(pc.rs.getString(1));

            }

            jList1.setModel(listModel);
            pc.kinyir();

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //backend cellák lekérése  
    public void becellsleker() {

        planconnect pc = new planconnect();
        try {
            pc.lekerdez("SELECT * FROM planningdb.tc_becells");
            DefaultListModel listModel = new DefaultListModel();

            listModel.removeAllElements();

            while (pc.rs.next()) {

                listModel.addElement(pc.rs.getString(2));

            }

            jList2.setModel(listModel);
            pc.kinyir();

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

    }

//fileokbeolvasása
    public void FilesToTables() throws IOException {

        JFileChooser chooser = CTB_Filechooser.getFileChooserRiport();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(c);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            CTB_Filechooser.setLastDirRiports(chooser.getSelectedFile());
            //String path = chooser.getSelectedFile().getAbsolutePath();
            //eltesszük fileba
            CTB_Ini q = new CTB_Ini(jTable11);
            q.inikezel(CTB_Ini.indit.riportpath);
            String path = file.getParent();
            path = path.replace("\\", "\\\\");
//kiirjuk fileba

//oh behúzása
            String pathfile = path + "\\\\On Hand.tab";
            File File = new File(pathfile);
            OhfileToTable(File);
//alloc behúzása
            pathfile = path + "\\\\Allocation.tab";
            File = new File(pathfile);
            AllocfileToTable(File);
//demand behúzása
            pathfile = path + "\\\\IndependentDemand.tab";
            File = new File(pathfile);
            DemandfileToTable(File);
//wo-k behúzása
            pathfile = path + "\\\\Work Orders All.tab";
            File = new File(pathfile);
            WofileToTable(File);
//indented behúzása
            pathfile = path + "\\\\Indented BOM for pivot.tab";
            File = new File(pathfile);
            IntendedfileToTable(File);
        }

    }

//ohfile bedolgozása a táblába
    public void OhfileToTable(File f) {

        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable2.getModel();
        ohmodel.setRowCount(0);
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                ohmodel.addRow(cells);

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Nem találom az On Hand filet!");

        }

        jTable2.setModel(ohmodel);

    }

//allocationfile bedolgozása a táblába
    public void AllocfileToTable(File f) {

        DefaultTableModel allocmodel = new DefaultTableModel();
        allocmodel = (DefaultTableModel) jTable3.getModel();
        allocmodel.setRowCount(0);
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                allocmodel.addRow(cells);

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Nem találom az Allocation filet!");

        }

        jTable3.setModel(allocmodel);

    }
    //demand betöltése

    public void DemandfileToTable(File f) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                model.addRow(cells);

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Nem találom az IndependentDemand filet!");

        }

        jTable4.setModel(model);

    }

    //wo-k betöltése
    public void WofileToTable(File f) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable5.getModel();
        model.setRowCount(0);
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                model.addRow(cells);

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Nem találom a Work Orders All filet!");

        }

        jTable5.setModel(model);

    }

    public void IntendedfileToTable(File f) {

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable6.getModel();
        model.setRowCount(0);
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                model.addRow(cells);

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Nem találom az Indented BOM for pivot filet!");

        }

        jTable6.setModel(model);

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

//beallitjuk a valtozot , hogy csak az aktualis szintre nezzunk e ctb-t vagy vilagitsuk e at az egeszet
//ha az egeszet nezzük , ki kell venni az sa-kat!!! , ha nem , akkor maradhatnak de csak level 1 kell!!
        int lvl = 100;
        String sa = "SA";
        if (jCheckBoxMenuItem6.isSelected()) {

            lvl = 1;
            sa = "kiskutyafüle";

        }

//miután ez megvan végigmegyünk rajta oszlopszor és megpróbáljuk megkereseni az adott oszlop pn-ét az intended bom ban,
        for (int o = 1; o < model.getColumnCount(); o++) {
//felvesszük pn-nek a pn-t :p
            String pn = model.getColumnName(o).trim();

            for (int r = 0; r < indentedmodel.getRowCount(); r++) {
                boolean tovabb = false;
//ha egyezik a pn az intended bom pn -ével és van ora type
                if (pn.equals(indentedmodel.getValueAt(r, 0).toString().trim()) && !indentedmodel.getValueAt(r, 8).toString().equals(sa) && !indentedmodel.getValueAt(r, 5).toString().equals("0") && Integer.parseInt(indentedmodel.getValueAt(r, 5).toString()) <= lvl) {

//itt megvizsgáljuk , hogy kell a a phantom item vagy nem
//ha nincs kipipálva az azt jelenti , hogy nem kell beletenni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem1.isSelected() && indentedmodel.getValueAt(r, 8).toString().equals("PH")) {

                        continue;

                    }

//itt megvizsgaljuk , hogy kell e a bulk
//ha nincs kipipálva az azt jelenti , hogy nem kell vele számolni , ergo ha az akkor ugrunk a ciklusban
                    if (!jCheckBoxMenuItem2.isSelected() && indentedmodel.getValueAt(r, 11).toString().equals("Bulk")) {

                        continue;

                    }

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
//a késztermék mennyisége!!
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        torles = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTextField11 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jTextField14 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
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
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem6 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();

        torles.setText("Kijelölés törlése!");
        torles.setName(""); // NOI18N
        torles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torlesActionPerformed(evt);
            }
        });
        jPopupMenu1.add(torles);

        jMenuItem2.setText("PN komment");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Készlet infó");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem3);

        jMenuItem4.setText("Mi használja");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem4);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 153));
        jTabbedPane1.setName("BOM_Table"); // NOI18N
        jTabbedPane1.setOpaque(true);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Open PO", "Stock", "Open JOB", "Need to build", "CTB", "TERV"
            }
        ));
        jTable1.setCellSelectionEnabled(true);
        jTable1.setComponentPopupMenu(jPopupMenu2);
        jTable1.setName("CTB_Table"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTable1MouseMoved(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
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

        jLabel1.setFont(new java.awt.Font("Segoe Script", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel2.setText("Üdv");

        jLabel9.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel9.setText("Kereső:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTable9.setAutoCreateRowSorter(true);
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PartNumber", "Qty", "Description", "Raw OH", "Master Comment", "OpSeq", "Supply", "QTY/Board"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.setToolTipText("<html>A mennyiségek szerkesztéséhez klikk a Raw OH oszlopra! <br>Egyéb infók , jobb klikk a PN -en!</html>");
        jTable9.setCellSelectionEnabled(true);
        jTable9.setComponentPopupMenu(jPopupMenu3);
        jTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable9MouseClicked(evt);
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
            jTable9.getColumnModel().getColumn(5).setMinWidth(20);
            jTable9.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTable9.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel17.setText("Kereső:");

        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jTextField9.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N

        jButton1.setText("Set");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable11.setAutoCreateRowSorter(true);
        jTable11.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Plan", "Shipped", "Need to plan", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
            }
        ));
        jTable11.setCellSelectionEnabled(true);
        jTable11.setComponentPopupMenu(jPopupMenu1);
        jTable11.setName("pofutable"); // NOI18N
        jTable11.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTable11MouseMoved(evt);
            }
        });
        jTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable11MouseReleased(evt);
            }
        });
        jTable11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable11KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable11KeyReleased(evt);
            }
        });
        jScrollPane11.setViewportView(jTable11);
        if (jTable11.getColumnModel().getColumnCount() > 0) {
            jTable11.getColumnModel().getColumn(0).setMinWidth(100);
            jTable11.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable11.getColumnModel().getColumn(0).setMaxWidth(500);
            jTable11.getColumnModel().getColumn(3).setMinWidth(100);
            jTable11.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable11.getColumnModel().getColumn(3).setMaxWidth(300);
        }

        jButton4.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 0, 0));
        jButton4.setText("<-->");
        jButton4.setToolTipText("Link tables");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel5.setText("Scenario:");

        jLabel6.setFont(new java.awt.Font("Segoe Script", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel7.setText("Kiszállítások:");

        jList1.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jScrollPane10.setViewportView(jList1);

        jTextField11.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTextField11.setText("YYYY-MM-DD hh:mm:ss");
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField11KeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel8.setText("Sorok száma:");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/savement.png"))); // NOI18N
        jButton2.setAlignmentY(0.0F);
        jButton2.setIconTextGap(0);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel18.setText("Hét száma:");

        jTextField13.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N

        jButton3.setText("Set");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel19.setText("Backend cellák:");

        jList2.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jScrollPane13.setViewportView(jList2);

        jTextField14.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTextField14.setText("YYYY-MM-DD hh:mm:ss");
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField14KeyPressed(evt);
            }
        });

        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel20.setText("Kijelölés összege:");

        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField15KeyReleased(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel21.setText("Kijelölés összege:");

        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel2))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton5))
                                            .addComponent(jLabel8))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField16))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel19))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton3))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton1)
                                                .addComponent(jButton5)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel21)
                                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 881, Short.MAX_VALUE)
                    .addComponent(jScrollPane11)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, jLabel5, jLabel6});

        jTabbedPane1.addTab("CTB", jPanel1);

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Warehouse", "Location", "Part", "Site ", "Type", "Date", "Quantity", "Unit Cost", "Inventory Store", "Part Description", "Master comment"
            }
        ));
        jTable2.setCellSelectionEnabled(true);
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
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1263, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
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
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1263, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1263, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1263, Short.MAX_VALUE))
            .addComponent(jScrollPane5)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE))
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
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1269, Short.MAX_VALUE))
            .addComponent(jScrollPane6)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE))
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
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1269, Short.MAX_VALUE))
            .addComponent(jScrollPane7)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BOM (Generated)", jPanel8);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Component", "QoH", "Wo War (Rem. Qty)", "New Prod", "Total OH", "Desc", "Master Comment"
            }
        ));
        jTable8.setCellSelectionEnabled(true);
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
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1286, Short.MAX_VALUE))
            .addComponent(jScrollPane8)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OH (Calculated)", jPanel9);

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Qty", "PO", "City", "Company"
            }
        ));
        jTable10.setCellSelectionEnabled(true);
        jTable10.setName("Shipping"); // NOI18N
        jScrollPane12.setViewportView(jTable10);

        jLabel16.setText("Kereső:");

        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 1466, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Shipping", jPanel10);

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

        jMenu3.setText("Eszközök");

        jMenuItem7.setText("Adatok betöltése lokális fileokból!");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem1.setText("Export Shorty");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

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

        jMenu1.setText("Kalkulációs beállítások");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jCheckBoxMenuItem6.setText("CTB Current LVL Only");
        jCheckBoxMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem6);

        jCheckBoxMenuItem1.setText("CTB Include Phantom ");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem2.setText("CTB Include Bulk");
        jCheckBoxMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem2);
        jMenu1.add(jSeparator1);

        buttonGroup2.add(jCheckBoxMenuItem3);
        jCheckBoxMenuItem3.setSelected(true);
        jCheckBoxMenuItem3.setText("OH Net-Asset");
        jCheckBoxMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem3);

        buttonGroup2.add(jCheckBoxMenuItem4);
        jCheckBoxMenuItem4.setText("OH Stage only");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem4);

        buttonGroup2.add(jCheckBoxMenuItem5);
        jCheckBoxMenuItem5.setText("OH All location");
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem5);

        jMenuBar1.add(jMenu1);

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

    public void Ohcalc(int miindit /*ha egy akkor csak egy részét futtatjuk , 0-a futtatja az egészet!!*/) {

//az ohtábla modellje
        jTable2.revalidate();
        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable2.getModel();
//a bomtábla modellje
        jTable7.revalidate();
        DefaultTableModel bommmodel = new DefaultTableModel();
        bommmodel = (DefaultTableModel) jTable7.getModel();
//az allocation tábla modellje
        jTable3.revalidate();
        DefaultTableModel allocmodel = new DefaultTableModel();
        allocmodel = (DefaultTableModel) jTable3.getModel();
//létrehozunk egy tömböt a calculált bom táblának
        String[][] calcbom = new String[bommmodel.getRowCount()][bommmodel.getColumnCount()];
//beletesszük az adatokat
        for (int r = 0; r < bommmodel.getRowCount(); r++) {

            for (int o = 0; o < bommmodel.getColumnCount(); o++) {
                try {
                    calcbom[r][o] = bommmodel.getValueAt(r, o).toString();
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
            for (int i = 0; i < bommmodel.getRowCount(); i++) {  //jtable7  ez a bom tábla

                ohtabla.addRow(new Object[ohtabla.getColumnCount()]);
                ohtabla.setValueAt(bommmodel.getValueAt(i, 0).toString(), i, 0);

            }

            for (int i = 0; i < ohtabla.getRowCount(); i++) {
                int osszeg = 0;
                String desc = "";
//vegigjarjuk az oh táblát és kiszedjük az összesített oh-t
                for (int o = 0; o < ohmodel.getRowCount(); o++) {

// ha a net asset ceckbox van kipipálva
                    if (jCheckBoxMenuItem3.isSelected()) {
                        try {
                            if ((ohtabla.getValueAt(i, 0).toString().equals(ohmodel.getValueAt(o, 2).toString())) && (ohmodel.getValueAt(o, 4).equals("Net-Asset"))) {
//kiszedjük a buzi vesszőt a stringből
                                try {
                                    desc = ohmodel.getValueAt(o, 9).toString();
                                } catch (Exception e) {
                                }
                                try {
                                    osszeg += Math.round(Float.valueOf(ohmodel.getValueAt(o, 6).toString().replace(",", "")).intValue());
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                        }
                    } //ha csak a stock van kiválasztva
                    else if (jCheckBoxMenuItem4.isSelected()) {
                        try {
                            if ((ohtabla.getValueAt(i, 0).toString().equals(ohmodel.getValueAt(o, 2).toString())) && (ohmodel.getValueAt(o, 0).toString().contains("STOCK"))) {

                                try {
                                    desc = ohmodel.getValueAt(o, 9).toString();
                                } catch (Exception e) {
                                }
                                try {
                                    osszeg += Math.round(Float.valueOf(ohmodel.getValueAt(o, 6).toString().replace(",", "")).intValue());
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                        }
                        //ha minden lokációra kiváncsiak vagyunk

                    } else if (jCheckBoxMenuItem5.isSelected()) {

                        try {
                            if ((ohtabla.getValueAt(i, 0).toString().equals(ohmodel.getValueAt(o, 2).toString()))) {
//kiszedjük a buzi vesszőt a stringből
                                try {
                                    desc = ohmodel.getValueAt(o, 9).toString();
                                } catch (Exception e) {
                                }
                                try {
                                    osszeg += Math.round(Float.valueOf(ohmodel.getValueAt(o, 6).toString().replace(",", "")).intValue());
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {
                        }

                    }

                }

                ohtabla.setValueAt(osszeg, i, 1);
                ohtabla.setValueAt(desc, i, 5);

//kiszedjük az elallokált mennyiségeket is
                osszeg = 0;
                for (int o = 0; o < allocmodel.getRowCount(); o++) {

                    try {
                        if (ohtabla.getValueAt(i, 0).toString().equals(allocmodel.getValueAt(o, 4).toString())) {

                            osszeg += Integer.parseInt(allocmodel.getValueAt(o, 11).toString());
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

                }

            }

            ohtabla.setValueAt(osszeg, oh, 3);

        }

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

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // scenario mentése
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        CTB_Scenario c = new CTB_Scenario(this);
        try {
            c.ScenarioSave();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // scenario beolvasása
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        CTB_Scenario s = new CTB_Scenario(this);
        try {
            s.ScenarioLoad();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField3.getText().trim(), jTable8);
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField8.getText().trim(), jTable7);
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField7.getText().trim(), jTable6);
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTable6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable6KeyPressed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField6.getText().trim(), jTable5);
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField5.getText().trim(), jTable4);
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        universalfilter uf = new universalfilter(jTextField4.getText().trim(), jTable3);
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // oh tabla szuro
        universalfilter uf = new universalfilter(jTextField1.getText().trim(), jTable2);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // sorok hozzáadása az igény táblához
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable11.getModel();
        model.setRowCount(0);
        //kiszedjük a textboxból a számot
        try {
            int sorszam = Integer.parseInt(jTextField9.getText());

            for (int i = 0; i < sorszam; i++) {

                model.addRow(new Object[jTable11.getColumnCount()]);

            }

        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox("Nem adtál meg számot!", "Hiba$");

        }

        jTable11.setModel(model);
        link = false;
        jButton4.setForeground(Color.red);
        new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 1);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        jTable1.revalidate();
        universalfilter uf = new universalfilter(jTextField2.getText().trim(), this.jTable1);
        //universalfilter um = new universalfilter(jTextField2.getText().trim(), this.jTable11);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased

        //alpagszuro();
        CTB_TopshortThread t = new CTB_TopshortThread();
        t.start();

        new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 1);


    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        //kikapcsoljuk az editálásokat

        if (jTable9.isEditing()) {

            jTable9.getCellEditor().stopCellEditing();

        }
        if (jTable1.isEditing()) {

            jTable1.getCellEditor().stopCellEditing();

        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
            jTable1.revalidate();
            int sor = jTable1.getSelectedRow();
            int oszlop = jTable1.getSelectedColumn();

            try {
                jTable1.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            };
            if (!link) {

                Ohcalc(1);
                CompToCtb();
                CtbKalk();
                NeedToBuild();
                TablaOszlopSzelesseg(jTable1);
                jTable1.setRowSelectionInterval(sor, sor);
                jTable1.setColumnSelectionInterval(oszlop, oszlop);
                jButton4.setForeground(Color.red);
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);
                //link = false;

            }

//-----------------------------------------------------------------------------------------------------------------------------------/////
            if (link) {

//felvesszük a plantáblát is
                DefaultTableModel tervmodel = new DefaultTableModel();
                tervmodel = (DefaultTableModel) jTable11.getModel();
//felvesszük a ctb táblát is
                DefaultTableModel ctbmodel = new DefaultTableModel();
                ctbmodel = (DefaultTableModel) jTable1.getModel();

                //ki kell számolni ,hogy mennyit kell még betervezni , ezt úgy tesszük , hogy kivonjuk a betervezett hetek összegéből a stockot , az open job-ot és a tervet
                for (int i = 0; i < tervmodel.getRowCount(); i++) {

                    //ha van valami írva a nulladik oszlopba
                    if (tervmodel.getValueAt(i, 0) != null) {
                        int osszeg = 0;
                        //bejárjuk oszlopszor
                        for (int o = 4; o < tervmodel.getColumnCount(); o++) {
                            try {
                                osszeg += Integer.parseInt(tervmodel.getValueAt(i, o).toString());
                            } catch (Exception e) {
                            }

                        }

                        //megvan az összeg , akkor kivonjuk a cuccokat
                        try {
                            osszeg = osszeg - Integer.parseInt(ctbmodel.getValueAt(i, 2).toString());
                        } catch (Exception e) {
                        }
                        try {
                            osszeg = osszeg - Integer.parseInt(ctbmodel.getValueAt(i, 3).toString());
                        } catch (Exception e) {
                        }
                        try {
                            osszeg = osszeg - Integer.parseInt(tervmodel.getValueAt(i, 1).toString());
                        } catch (Exception e) {

                        }

//kiszállítás kivonása
                        try {
                            osszeg = osszeg - Integer.parseInt(tervmodel.getValueAt(i, 2).toString());
                        } catch (Exception e) {

                        }

                        //beszetteljük az eredményt a táblába a need to planhez 
                        tervmodel.setValueAt(osszeg, i, 3);

                        int plan = 0;

                        try {

                            plan = Integer.parseInt(tervmodel.getValueAt(i, 1).toString());

                        } catch (Exception e) {

                        }

//ha az összeg (needtoplan negatí , akkor csak a plannel számolunk)
                        if (osszeg <= 0) {
                            try {
                                ctbmodel.setValueAt(plan, i, 6);
                            } catch (Exception e) {
                            }

                        } //ellenkező esetben mindkettővel               
                        else {

                            ctbmodel.setValueAt(osszeg + plan, i, 6);

                        }

                    }

                }

                jTable1.setModel(ctbmodel);
                jTable11.setModel(tervmodel);
                tervszamol();
                TablaOszlopSzelesseg(jTable11);
                jTable1.revalidate();
                Ohcalc(1);
                CompToCtb();
                CtbKalk();
                NeedToBuild();
                TablaOszlopSzelesseg(jTable1);
                jTable1.setRowSelectionInterval(sor, sor);
                jTable1.setColumnSelectionInterval(oszlop, oszlop);

                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);

            }
        }


    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //alpagszuro();
        if (jTable9.isEditing()) {

            jTable9.getCellEditor().stopCellEditing();

        }
        CTB_TopshortThread t = new CTB_TopshortThread();
        t.start();


    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable11KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            fullcalc(false);
        }

    }//GEN-LAST:event_jTable11KeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

//ha nem vagyunk kapcsolódva
        if (!link) {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
            //számolás a beírt pn ekkel és mennyiségekkel...
            //létrehozunk egy tömböt a beírt pn ekből és mennyiségekből
            jTable11.revalidate();
            String[][] adatok = new String[jTable11.getRowCount()][2];
            DefaultTableModel tervmodel = new DefaultTableModel();
            tervmodel = (DefaultTableModel) jTable11.getModel();

            //bejárjuk a táblát és feltöltjük a tömböt
            for (int i = 0; i < tervmodel.getRowCount(); i++) {

                if (tervmodel.getValueAt(i, 0) != null && !tervmodel.getValueAt(i, 0).toString().equals("")) {

                    try {
                        adatok[i][0] = tervmodel.getValueAt(i, 0).toString().trim();
                    } catch (Exception e) {
                    }

                    //végigjárjuk az oszlopokat és összeadjuk a mennyiséget
                    int osszeg = 0;
                    for (int o = 3; o < tervmodel.getColumnCount(); o++) {
                        try {
                            osszeg += Integer.parseInt(tervmodel.getValueAt(i, o).toString());
                        } catch (Exception e) {
                        }
                    }

                    adatok[i][1] = String.valueOf(osszeg);

                } else {
                    tervmodel.removeRow(i);
                    i--;
                }
            }
            jTable11.setModel(tervmodel);
            TablaOszlopSzelesseg(jTable11);
            //kinullázzuk a ctb táblát
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable1.getModel();
            model.setColumnCount(7);
            model.setRowCount(0);

            //akkor most hozzáadjuk az uj adatokat a ctb modellhez
            for (int i = 0; i < adatok.length; i++) {
                if (adatok[i][0] != null) {
                    model.addRow(new Object[]{adatok[i][0], "", "", "", "", "", "" /*adatok[i][1]*/});
                }

            }

            jTable1.setModel(model);
            CTB_Wipquery wip = new CTB_Wipquery(jTable1);
            wip.start();
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

            TablaOszlopSzelesseg(jTable1);
            jButton4.setForeground(Color.GREEN);
            link = true;

//összekapcsolódás
            new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 1);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
            ablak.stat.beir(System.getProperty("user.name"), "CTB kapcsolodas!", "", "gabor.hanacsek@sanmina.com");
        } //ha kapcsolódva vagyunk szétkapcsolunk
        else {

            link = false;
            jButton4.setForeground(Color.RED);

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 1);
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTable11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable11KeyReleased
        // TODO add your handling code here:
        new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 2);
        CTB_TopshortThread t = new CTB_TopshortThread();
        t.start();
    }//GEN-LAST:event_jTable11KeyReleased

    private void jTable11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable11MouseReleased
        // TODO add your handling code here:
        new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 2);
        CTB_TopshortThread t = new CTB_TopshortThread();
        t.start();
    }//GEN-LAST:event_jTable11MouseReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        // TODO add your handling code here:
        jTable11.revalidate();
        universalfilter u = new universalfilter(jTextField10.getText().trim(), this.jTable11);
        // universalfilter m = new universalfilter(jTextField10.getText().trim(), this.jTable1);
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        //Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        try {
            //a ...tab filekot betesszük a sheetekbe majd elvégezzük a számolásokat
            FilesToTables();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        OpenPoCalc();
        StockCalc();
        OpenOrderCalc();
        BomCalc();
        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();
        TablaOszlopSzelesseg(jTable1);

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyPressed
        //kiszállítások lekérdezése
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
//felvesszük a kiszállításokat egy datamodellbe
            DefaultTableModel kiszallitasmodel = new DefaultTableModel();
            kiszallitasmodel = (DefaultTableModel) jTable10.getModel();
//felvesszük a plantáblát is egy modellbe
            DefaultTableModel planmodel = new DefaultTableModel();
            planmodel = (DefaultTableModel) jTable11.getModel();

            String termekek = "";
            for (int i = 0; i < jList1.getModel().getSize(); i++) {

                if (jList1.isSelectedIndex(i)) {

                    termekek += "'" + jList1.getModel().getElementAt(i).toString() + "',";

                }

            }

            if (termekek.equals("")) {

                infobox inf = new infobox();
                inf.infoBox("Nem választottál ki termékcsaládot!", "Hiba!");
                return;

            }

            termekek = termekek.substring(0, termekek.length() - 1);

            String query = "select partnumber.partnumber, sum(containers.quantity) , shipping_content.po , customer_address.city , customer_address.company FROM \"HBPackage\".containers \n"
                    + "left join \"HBPackage\".partnumber on containers.partnumber_id=partnumber.id \n"
                    + "left join \"HBPackage\".second_containers on second_containers.id = containers.second_ctn_id \n"
                    + "left join \"HBPackage\".shipping_content on shipping_content.id = second_containers.shipping_content_id left join \"HBPackage\".shipping on shipping.id = shipping_content.shipping_id \n"
                    + "left join \"HBPackage\".customer_address on customer_address.id = shipping.address_id \n"
                    + "where containers.second_ctn_id in (SELECT id FROM \"HBPackage\".second_containers where shipping_id in\n"
                    + "(SELECT \"HBPackage\".shipping.id FROM \"HBPackage\".shipping left join \"HBPackage\".customer_type on customer_type.id = shipping.customer_type \n"
                    + "where targetdate >= '" + jTextField11.getText().trim() + "'\n"
                    + "and customer_type.name in (" + termekek + ") )) \n"
                    + "group by partnumber.partnumber , shipping_content.po , customer_address.city , customer_address.company";

            postgreconnect pc = new postgreconnect();
            try {
                pc.lekerdez(query);
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) jTable10.getModel();
                model.setRowCount(0);

                while (pc.rs.next()) {

                    model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5),});

                }

                jTable10.setModel(model);

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            pc.kinyir();

//ki kell tölteni a plantábla adatait is a kiszállítási mennyiségekkel
            for (int i = 0; i < planmodel.getRowCount(); i++) {

                String pn = "";
                try {
                    pn = planmodel.getValueAt(i, 0).toString();
                } catch (Exception e) {
                    continue;
                }
                int osszeg = 0;

                for (int s = 0; s < kiszallitasmodel.getRowCount(); s++) {

                    if (kiszallitasmodel.getValueAt(s, 0).equals(pn)) {

                        osszeg += Integer.parseInt(kiszallitasmodel.getValueAt(s, 1).toString());

                    }

                }

                planmodel.setValueAt(osszeg, i, 2);

            }

            jTable11.setModel(planmodel);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        }


    }//GEN-LAST:event_jTextField11KeyPressed

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        universalfilter u = new universalfilter(jTextField12.getText().trim(), jTable10);
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //mentjük a scenariot (előtte meg kellett hogy legyen nyitva egy)
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (CTB.f != null) {

            FileWriter fw = null;
            try {
                fw = new FileWriter(CTB.f);
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                //beirunk valami faszssagot
                bw.write("Kezdodik a file!");
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                bw.newLine();
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

            JTable[] tablakneve = new JTable[10];
            tablakneve[0] = jTable1;
            tablakneve[1] = jTable2;
            tablakneve[2] = jTable3;
            tablakneve[3] = jTable4;
            tablakneve[4] = jTable5;
            tablakneve[5] = jTable6;
            tablakneve[6] = jTable7;
            tablakneve[7] = jTable8;
            tablakneve[8] = jTable11;
            tablakneve[9] = jTable10;

            for (int i = 0; i < tablakneve.length; i++) {

                try {
                    CTB_Scenario.TableToText(tablakneve[i], CTB.f, bw);
                } catch (IOException ex) {
                    Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            try {
                //beirunk valami faszssagot
                bw.write("Vege a Filenak!");
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                bw.newLine();
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
        // ha klikkelünk az egérrel a short táblán

        //ha a raw sorba klikkelünk elinditjuk a ctb rawohedit-et
        if (jTable9.getSelectedColumn() == 3) {
//felvesszük a selected row-t a ctb táblából

            this.selectedrow = jTable1.getSelectedRow();
            this.selectedcolumn = jTable1.getSelectedColumn();

            if (!ohedit) {

                CTB_Rawohedit r = new CTB_Rawohedit(jTable9, this);
                r.setVisible(true);
            } else {

                infobox info = new infobox();
                info.infoBox("CSak egy anyagot szerkessz egyszerre!", "Ájjáj!");

            }

        } //ha a pn-re klikkelünk lekérdezzük a készlletet és beírjuk tooltipként
//        else if (jTable9.getSelectedColumn() == 0) {
////kiszedjük a PN-t
//            String pn = jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString();
//
//            CTB_StockFromShort st = new CTB_StockFromShort(pn);
//            st.start();
//        }

    }//GEN-LAST:event_jTable9MouseClicked

    private void torlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_torlesActionPerformed
        // terület törlése

        int rows[] = this.jTable11.getSelectedRows();
        int columns[] = this.jTable11.getSelectedColumns();

        for (int i = 0; i < rows.length; i++) {

            for (int n = 0; n < columns.length; n++) {

                this.jTable11.setValueAt(null, rows[i], columns[n]);

            }

        }

    }//GEN-LAST:event_torlesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // plantábla oszlopainak átnevezése a hét számának megfelelően

        int hetszama = 1;

        try {

            hetszama = Integer.parseInt(jTextField13.getText());

            for (int i = 4; i < 14; i++) {

                jTable11.getColumnModel().getColumn(i).setHeaderValue(hetszama);
                hetszama++;

            }

            jTable11.repaint();
            TablaOszlopSzelesseg(jTable11);

        } catch (Exception e) {

            infobox info = new infobox();
            info.infoBox("Nem valid számot adtál meg!", "Figyelem!");

        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14KeyPressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // 

        CTB_ExportShorty e = new CTB_ExportShorty();
        e.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // TODO add your handling code here:

        fullcalc(true);

    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jCheckBoxMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem2ActionPerformed
        // TODO add your handling code here:
        fullcalc(true);
    }//GEN-LAST:event_jCheckBoxMenuItem2ActionPerformed

    private void jCheckBoxMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem3ActionPerformed
        // TODO add your handling code here:
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

//neatble lokira szűrés a pn kalkulációnál
//fel kell oldani a szűrőket!
        jTable1.setRowSorter(null);
        jTable2.setRowSorter(null);
        jTable3.setRowSorter(null);
        jTable4.setRowSorter(null);
        jTable5.setRowSorter(null);
        jTable6.setRowSorter(null);
        jTable7.setRowSorter(null);
        jTable8.setRowSorter(null);
        jTable11.setRowSorter(null);

        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();
        TablaOszlopSzelesseg(jTable1);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jCheckBoxMenuItem3ActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
        // TODO add your handling code here:
        //csak stock lokin lévő anyagokkal számolunk
        //fel kell oldani a szűrőket!
        jTable1.setRowSorter(null);
        jTable2.setRowSorter(null);
        jTable3.setRowSorter(null);
        jTable4.setRowSorter(null);
        jTable5.setRowSorter(null);
        jTable6.setRowSorter(null);
        jTable7.setRowSorter(null);
        jTable8.setRowSorter(null);
        jTable11.setRowSorter(null);
        jTable10.setRowSorter(null);
        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();
        TablaOszlopSzelesseg(jTable1);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
        // TODO add your handling code here:
        // mindenféle anyagokkal számolunk
        //fel kell oldani a szűrőket!
        jTable1.setRowSorter(null);
        jTable2.setRowSorter(null);
        jTable3.setRowSorter(null);
        jTable4.setRowSorter(null);
        jTable5.setRowSorter(null);
        jTable6.setRowSorter(null);
        jTable7.setRowSorter(null);
        jTable8.setRowSorter(null);
        jTable11.setRowSorter(null);
        jTable10.setRowSorter(null);
        Ohcalc(0);
        CompToCtb();
        CtbKalk();
        NeedToBuild();
        TablaOszlopSzelesseg(jTable1);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //plusz sorok hozzáadása a meglévőkhöz
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable11.getModel();
        int sor = 0;
        try {
            sor = Integer.parseInt(jTextField9.getText());

            for (int i = 0; i < sor; i++) {

                model.addRow(new Object[14]);

            }

            jTable11.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Nem adtál meg , vagy nem jó sor számot adtál meg!",
                    "Hiba!",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyReleased

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTable1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseMoved
        //kijelölés összeadása
        int[] selectedrow = jTable1.getSelectedRows();
        int[] selectedcolumn = jTable1.getSelectedColumns();
        int osszeg = 0;
        for (int r = 0; r < selectedrow.length; r++) {

            for (int c = 0; c < selectedcolumn.length; c++) {
                try {
                    osszeg += Integer.parseInt(jTable1.getValueAt(selectedrow[r], selectedcolumn[c]).toString());
                } catch (Exception e) {
                }

            }

        }

        jTextField15.setText(String.valueOf(osszeg));


    }//GEN-LAST:event_jTable1MouseMoved

    private void jTable11MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable11MouseMoved
        // TODO add your handling code here:
        //kijelölés összeadása
        int[] selectedrow = jTable11.getSelectedRows();
        int[] selectedcolumn = jTable11.getSelectedColumns();
        int osszeg = 0;
        for (int r = 0; r < selectedrow.length; r++) {

            for (int c = 0; c < selectedcolumn.length; c++) {
                try {
                    osszeg += Integer.parseInt(jTable11.getValueAt(selectedrow[r], selectedcolumn[c]).toString());
                } catch (Exception e) {
                }

            }

        }

        jTextField16.setText(String.valueOf(osszeg));
    }//GEN-LAST:event_jTable11MouseMoved

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        CTB_Ini i = new CTB_Ini(jTable11);
        try {
            i.inikezel(CTB_Ini.indit.part);
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            //pn kommentelés
            CTB_PnKomment p = new CTB_PnKomment(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
            p.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jCheckBoxMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem6ActionPerformed
        // TODO add your handling code here:
        fullcalc(true);
        CTB_TopshortThread t = new CTB_TopshortThread();
        t.start();
    }//GEN-LAST:event_jCheckBoxMenuItem6ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // készlet infó a short táblából
        CTB_StockInfo si = new CTB_StockInfo(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString());
        si.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // mi használja az anyagot
        CTB_WhereUsed w = new CTB_WhereUsed(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString());
        w.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    public void fullcalc(boolean eriggye) {

        if (jTable1.isEditing()) {

            jTable1.getCellEditor().stopCellEditing();

        }
        if (jTable11.isEditing()) {

            jTable11.getCellEditor().stopCellEditing();

        }

        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);

//megvizsgáljuk hogy egyformák e a táblák
        CTB_CompareTables c = new CTB_CompareTables(jTable1, jTable11);
        if (!c.compare() || eriggye) {

            //számolás a beírt pn ekkel és mennyiségekkel...
            //létrehozunk egy tömböt a beírt pn ekből és mennyiségekből
            jTable11.revalidate();
            String[][] adatok = new String[jTable11.getRowCount()][2];
            DefaultTableModel tervmodel = new DefaultTableModel();
            tervmodel = (DefaultTableModel) jTable11.getModel();

            //bejárjuk a táblát és feltöltjük a tömböt
            for (int i = 0; i < tervmodel.getRowCount(); i++) {

                if (tervmodel.getValueAt(i, 0) != null && !tervmodel.getValueAt(i, 0).toString().equals("")) {

                    try {
                        adatok[i][0] = tervmodel.getValueAt(i, 0).toString().trim();
                    } catch (Exception e) {
                    }

                    //végigjárjuk az oszlopokat és összeadjuk a mennyiséget
                    int osszeg = 0;
                    for (int o = 3; o < tervmodel.getColumnCount(); o++) {
                        try {
                            osszeg += Integer.parseInt(tervmodel.getValueAt(i, o).toString());
                        } catch (Exception e) {
                        }
                    }

                    adatok[i][1] = String.valueOf(osszeg);

                } else {
                    tervmodel.removeRow(i);
                    i--;
                }
            }
            jTable11.setModel(tervmodel);
            TablaOszlopSzelesseg(jTable11);
            //kinullázzuk a ctb táblát
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable1.getModel();
            model.setColumnCount(7);
            model.setRowCount(0);

            //akkor most hozzáadjuk az uj adatokat a ctb modellhez
            for (int i = 0; i < adatok.length; i++) {
                if (adatok[i][0] != null) {
                    model.addRow(new Object[]{adatok[i][0], "", "", "", "", "", "" /*adatok[i][1]*/});
                }

            }

            jTable1.setModel(model);
            CTB_Wipquery wip = new CTB_Wipquery(jTable1);
            wip.start();
            OpenPoCalc();
            StockCalc();
            OpenOrderCalc();
//a pn-jeink bom matrixat rakja össze
            BomCalc();
//ez kiszámolja a felhasználható oh mennyiséget  (lasúúúúúúúú)
            Ohcalc(0);

//kiteszi a ctb táblába azokat az alkatrészeket amiket használ a termék
            //CompToCtb();
//kitölti a ctb táblába , hogy hány termékre vagyunk tiszták
            //CtbKalk();
            //NeedToBuild();
            TablaOszlopSzelesseg(jTable1);
            jButton4.setForeground(Color.GREEN);
            link = true;

//összekapcsolódás
            new CTB_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, this, 1);

            ablak.stat.beir(System.getProperty("user.name"), "CTB kapcsolodas!", "", "gabor.hanacsek@sanmina.com");

        }

//felvesszük a plantáblát is
        DefaultTableModel tervmodel = new DefaultTableModel();
        tervmodel = (DefaultTableModel) jTable11.getModel();
//felvesszük a ctb táblát is
        DefaultTableModel ctbmodel = new DefaultTableModel();
        ctbmodel = (DefaultTableModel) jTable1.getModel();

        //ki kell számolni ,hogy mennyit kell még betervezni , ezt úgy tesszük , hogy kivonjuk a betervezett hetek összegéből a stockot , az open job-ot és a tervet
        for (int i = 0; i < tervmodel.getRowCount(); i++) {

            //ha van valami írva a nulladik oszlopba
            if (tervmodel.getValueAt(i, 0) != null) {
                int osszeg = 0;
                //bejárjuk oszlopszor
                for (int o = 4; o < tervmodel.getColumnCount(); o++) {
                    try {
                        osszeg += Integer.parseInt(tervmodel.getValueAt(i, o).toString());
                    } catch (Exception e) {
                    }

                }

                //megvan az összeg , akkor kivonjuk a cuccokat
                try {
                    osszeg = osszeg - Integer.parseInt(ctbmodel.getValueAt(i, 2).toString());
                } catch (Exception e) {
                }
                try {
                    osszeg = osszeg - Integer.parseInt(ctbmodel.getValueAt(i, 3).toString());
                } catch (Exception e) {
                }
                try {
                    osszeg = osszeg - Integer.parseInt(tervmodel.getValueAt(i, 1).toString());
                } catch (Exception e) {

                }

//kiszállítás kivonása
                try {
                    osszeg = osszeg - Integer.parseInt(tervmodel.getValueAt(i, 2).toString());
                } catch (Exception e) {

                }

                //beszetteljük az eredményt a táblába a need to planhez 
                tervmodel.setValueAt(osszeg, i, 3);

                int plan = 0;

                try {

                    plan = Integer.parseInt(tervmodel.getValueAt(i, 1).toString());

                } catch (Exception e) {

                }

//ha az összeg (needtoplan negatí , akkor csak a plannel számolunk)
                if (osszeg <= 0) {
                    try {
                        ctbmodel.setValueAt(plan, i, 6);
                    } catch (Exception e) {
                    }

                } //ellenkező esetben mindkettővel               
                else {
                    try {
                        ctbmodel.setValueAt(osszeg + plan, i, 6);
                    } catch (Exception e) {
                    }

                }

            }

        }

        jTable1.setModel(ctbmodel);
        jTable11.setModel(tervmodel);

        jTable1.revalidate();
        //System.out.println(System.currentTimeMillis());
        tervszamol();
        //System.out.println(System.currentTimeMillis() + "--tervszamol");
        Ohcalc(1);
        //System.out.println(System.currentTimeMillis() + "--ohcalc");
        CompToCtb();
        //System.out.println(System.currentTimeMillis() + "--comptoctb");
        CtbKalk();
        //System.out.println(System.currentTimeMillis() + "--ctbkalk");
        NeedToBuild();
        //System.out.println(System.currentTimeMillis() + "--needtobuild");
        TablaOszlopSzelesseg(jTable1);
        TablaOszlopSzelesseg(jTable11);

        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);

    }

    public void topshort() {
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
        DefaultTableModel ohmodel = new DefaultTableModel();
        ohmodel = (DefaultTableModel) jTable2.getModel();
        for (i = 0; i < shortmodel.getRowCount(); i++) {
            String pn = shortmodel.getValueAt(i, 0).toString();
//vegigtekerjuk az oh táblát is sajna és kiszedjük a kommenteket a pn -ekhez  
            String komment = "";

            for (int n = 0; n < ohmodel.getRowCount(); n++) {

                if (pn.equals(ohmodel.getValueAt(n, 2).toString())) {
                    try {
                        komment += ohmodel.getValueAt(n, 10).toString() + ",";
                    } catch (Exception e) {
                    }

                }

            }

            for (int k = 0; k < rawohmodel.getRowCount(); k++) {

                if (shortmodel.getValueAt(i, 0).toString().equals(rawohmodel.getValueAt(k, 0).toString())) {

                    shortmodel.setValueAt(rawohmodel.getValueAt(k, 5), i, 2);
                    shortmodel.setValueAt(rawohmodel.getValueAt(k, 1), i, 3);
                    shortmodel.setValueAt(komment, i, 4);

                }
            }

        }

        jTable9.setModel(shortmodel);

        TablaOszlopSzelesseg(jTable9);
    }

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlannTool/PICTURES/ctb1.jpg")));

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
            outerloop:
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

                                //continue outerloop;
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

    public static void TablaOszlopSzelesseg(JTable table) {

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                try {
                    Component comp = table.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 1, width);
                } catch (Exception e) {
                }
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
        try {
            table.repaint();
        } catch (Exception e) {
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem6;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    public static javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    public static javax.swing.JList<String> jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem7;
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
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    public static javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable10;
    public static javax.swing.JTable jTable11;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    public static javax.swing.JTable jTable6;
    public static javax.swing.JTable jTable7;
    public static javax.swing.JTable jTable8;
    public static javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    public static javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JMenuItem torles;
    // End of variables declaration//GEN-END:variables

}
