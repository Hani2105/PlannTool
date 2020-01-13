/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.CTB_CALC.CTB_Tablarenderer;
import PlannTool.CONNECTS.planconnect;
import PlannTool.CONNECTS.postgreconnect;
import static PlannTool.CTB_CALC.CTB_NEW_RawOhEdit.jLabel2;
import static PlannTool.CTB_CALC.CTB_NEW_RawOhEdit.jTextArea1;
import PlannTool.ExcelAdapter;
import PlannTool.ExcelExporter;
import PlannTool.ablak;
import static PlannTool.ablak.jList2;
import PlannTool.universalfilter;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB extends javax.swing.JFrame {

    /**
     * Creates new form CTB
     */
    CTB_Bejel c;

    //uj resz
    CTB_NEW_Variables v = new CTB_NEW_Variables();

    public static String user = "";
    public static BoundedRangeModel buzimodel;
    public static File f;
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
//a horizontalok neveit fogjuk itt tárolni amit be kell olvasni majd az ini fileból
    public static ArrayList<String> Horizontals = new ArrayList<String>();
//a workorderseket fogjuk itt tárolni
    public static ArrayList<String> Workorders = new ArrayList<>();
//a onhandet fogjuk itt tárolni
    public static ArrayList<String> Onhands = new ArrayList<>();
//a onhandet fogjuk itt tárolni
    public static ArrayList<String> Indentedbom = new ArrayList<>();
//a independent demand
    public static ArrayList<String> Demand = new ArrayList<>();
//a allocation listaja
    public static ArrayList<String> Allocations = new ArrayList<>();
//a backend cellak listaja
    public static DefaultListModel becellsmodell = new DefaultListModel();

//a kiszallitasok listaja
    public static DefaultListModel kiszallitasmodell = new DefaultListModel();
//a kiválasztott backend állomások listája a tervszámolhoz kell
    public static ArrayList<String> SelectedBecells = new ArrayList<>();
//a kiválasztott idő a tervek számolásához
    public static String tervido = "YYYY-MM-DD hh:mm:ss";
//ne inditsunk másik szálat amig fut az egyik tipshort szál
    public static boolean topshortszal = true;

//példányosítunk egy kommentet
    public CTB_NEW_PNKomment komment = new CTB_NEW_PNKomment(this, false);
    public CTB_NEW_RawOhEdit rawedit = new CTB_NEW_RawOhEdit(this, false, this);
    public CTB_NEW_ExportShorty exportshorty = new CTB_NEW_ExportShorty(this, false, this);
    public CTB_NEW_EditLostQuantity lostedit = new CTB_NEW_EditLostQuantity(this, false, this);
    public CTB_NEW_Controlpanel control = new CTB_NEW_Controlpanel(this, false, this);
    public CTB_NEW_WarningWindow warning = new CTB_NEW_WarningWindow(this, false);
    public CTB_NEW_TickWindows tick = new CTB_NEW_TickWindows(this, false);
    public CTB_NEW_Helper helper = new CTB_NEW_Helper(this, false);
    public CTB_NEW_WhereUsed whereused = new CTB_NEW_WhereUsed(this, false);

    public CTB(CTB_Bejel c) throws SQLException, ClassNotFoundException, IOException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, ParseException {

        this.c = c;
        setExtendedState(MAXIMIZED_BOTH);
        // UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        initComponents();

        this.jTabbedPane1.setUI(new CTB_TabbedUI(jTabbedPane1));
        seticon();

//beilleszthetővé tesszük az igények táblát
        new ExcelAdapter(jTable11);
//az oh tablat is
        new ExcelAdapter(jTable2);
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
//példányosítunk egy pnkomment ablakot
        komment.setVisible(false);
//elrejtjük az edit ablakot
        rawedit.setVisible(false);
//az exportshorty beállításai
        exportshorty.setVisible(false);
//losteditor ablak
        lostedit.setVisible(false);
//control panel
        control.setVisible(false);
//warning window és tick
        warning.setVisible(false);
        tick.setVisible(false);
        helper.setVisible(false);
        whereused.setVisible(false);

        kiszallitasleker();
        becellsleker();

//betöltjük az ini filet
        CTB_Ini i = new CTB_Ini(jTable11);
        i.Olvas();
//betöltjük a lost tábla adatait az ini filebol
        CTB_LostRead q = new CTB_LostRead();
        q.olvas();
//lekerdezzük a control panel adatait
        kezdokitolt();
//elinditjuk a waitwindows-t

    }

    public static void updateBar(String method, int setmax, int increased) {

        jProgressBar1.setMaximum(setmax);
        jProgressBar1.setValue(increased);
        jProgressBar1.setString(String.valueOf(method + " " + increased) + " / " + String.valueOf(setmax));

    }

//ez tölti ki a control panel adatait
    public void kezdokitolt() {
        //lekerdezzuk a ctb horiotal valtozojat es beletesdzuk a tablaba
        for (int i = 0; i < CTB.Horizontals.size(); i++) {
            control.jTextField20.setText(CTB.Horizontals.get(i).toString());
        }
        //lekerdezzuk a workorderseket
        //lekerdezzuk a ctb horiotal valtozojat es beletesdzuk a tablaba
        for (int i = 0; i < CTB.Workorders.size(); i++) {
            control.jTextField16.setText(CTB.Workorders.get(i).toString());
        }
        for (int i = 0; i < CTB.Onhands.size(); i++) {
            control.jTextField17.setText(CTB.Onhands.get(i).toString());
        }
        for (int i = 0; i < CTB.Indentedbom.size(); i++) {
            control.jTextField19.setText(CTB.Indentedbom.get(i).toString());
        }
        for (int i = 0; i < CTB.Demand.size(); i++) {
            control.jTextField18.setText(CTB.Demand.get(i).toString());
        }
        for (int i = 0; i < CTB.Allocations.size(); i++) {
            control.jTextField15.setText(CTB.Allocations.get(i).toString());
        }
    }

    //lekérjük a kiszállítások területének listáját és feltöltjük a jlistet
    public void kiszallitasleker() {

        postgreconnect pc = new postgreconnect();
        try {
            pc.lekerdez("select distinct \"HBPackage\".customer_type.name from \"HBPackage\".customer_type");
            //DefaultListModel listModel = new DefaultListModel();

            //kiszallitasmodell.removeAllElements();
            while (pc.rs.next()) {

                kiszallitasmodell.addElement(pc.rs.getString(1));

            }
            try {
                control.jList1.setModel(kiszallitasmodell);
            } catch (Exception e) {
            }
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
            //DefaultListModel listModel = new DefaultListModel();

            //becellsmodell.removeAllElements();
            while (pc.rs.next()) {

                becellsmodell.addElement(pc.rs.getString(2));

            }

            control.jList2.setModel(becellsmodell);
            pc.kinyir();

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

    }

//fileokbeolvasása
    public void FilesToTables() throws IOException, FileNotFoundException, ParseException {

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
//----------------------------------------------------------------------------------------------------------------------            
//az összes filet olvassuk be a könyvtárból
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {

//kinullazzuk a tablakat
                DefaultTableModel model = new DefaultTableModel();
//alloctable                 
                model = (DefaultTableModel) jTable3.getModel();
                model.setRowCount(0);
                //model.setColumnCount(0);
                jTable3.setModel(model);
//workorders
                model = (DefaultTableModel) jTable5.getModel();
                model.setRowCount(0);
                //model.setColumnCount(0);
                jTable5.setModel(model);
//onhands
                model = (DefaultTableModel) jTable2.getModel();
                model.setRowCount(0);
                //model.setColumnCount(0);
                jTable2.setModel(model);
//demand
                model = (DefaultTableModel) jTable4.getModel();
                model.setRowCount(0);
                //model.setColumnCount(0);
                jTable4.setModel(model);
//bom          
                model = (DefaultTableModel) jTable6.getModel();
                model.setRowCount(0);
                //model.setColumnCount(0);
                jTable6.setModel(model);
//hori
                model = (DefaultTableModel) jTable13.getModel();
                model.setRowCount(0);
                // model.setColumnCount(0);
                jTable13.setModel(model);

                List<String> result = walk.filter(Files::isRegularFile)
                        .map(x -> x.toString()).collect(Collectors.toList());

                for (int i = 0; i < result.size(); i++) {
                    //allocation behúzása
                    if (result.get(i).contains(control.jTextField15.getText()) && !control.jTextField15.getText().equals("")) {

                        BasefileToTableWalk(jTable3, result.get(i));

                    } //workorders behúzása                  
                    else if (result.get(i).contains(control.jTextField16.getText()) && !control.jTextField16.getText().equals("")) {

                        BasefileToTableWalk(jTable5, result.get(i));

                    }//onhands 
                    else if (result.get(i).contains(control.jTextField17.getText()) && !control.jTextField17.getText().equals("")) {

                        BasefileToTableWalk(jTable2, result.get(i));

                    } //independent demand
                    else if (result.get(i).contains(control.jTextField18.getText()) && !control.jTextField18.getText().equals("")) {

                        BasefileToTableWalk(jTable4, result.get(i));

                    } //indented bom
                    else if (result.get(i).contains(control.jTextField19.getText()) && !control.jTextField19.getText().equals("")) {

                        BasefileToTableWalk(jTable6, result.get(i));

                    } //horizontal
                    else if (result.get(i).contains(control.jTextField20.getText()) && !control.jTextField20.getText().equals("")) {

                        HorizontalsToTable(result.get(i));

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void BasefileToTableWalk(JTable t, String riportname) {

        //felvesszük a tábla modelljét ahova a riportot be kell tenni
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) t.getModel();
        // model.setRowCount(0);
        //model.setColumnCount(0);
//letrehozunk egy olvasot
        BufferedReader in;

//most kell ciklust indítani
        String pathfile = riportname;
        File File = new File(pathfile);
        try {
            in = new BufferedReader(new FileReader(File));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                model.addRow(cells);

            }

        } catch (Exception e) {
            e.printStackTrace();
            warning.SetMessage("Nem találom a(z) " + riportname + " filet!");
            return;
            //JOptionPane.showMessageDialog(this, "Nem találom a(z) " + a + " filet!");
        }

    }

    public void BasefileToTable(String path, ArrayList a, JTable t, String riportname) {

        //felvesszük a tábla modelljét ahova a riportot be kell tenni
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) t.getModel();
        //model.setRowCount(0);
        //model.setColumnCount(0);
//letrehozunk egy olvasot
        BufferedReader in;
//megvizsgáljuk , hogy nem nulla e a mérete a listnek , ha az akkor csöcs van

        if (a.size() == 0) {

            //JOptionPane.showMessageDialog(this, "Nem találom a(z) " + riportname + " filet!");
            warning.SetMessage("Nem találom a(z) " + riportname + " filet!");
            return;

        }
//most kell ciklust indítani
        for (int i = 0; i < a.size(); i++) {

            String pathfile = path + "\\\\" + a.get(i) + ".tab";
            File File = new File(pathfile);
            try {
                in = new BufferedReader(new FileReader(File));
                String line = in.readLine();
                while ((line = in.readLine()) != null) {

                    String[] cells = line.split("\\t");
                    model.addRow(cells);

                }

            } catch (Exception e) {
                warning.SetMessage("Nem találom a(z) " + a + " filet!");
                return;
                //JOptionPane.showMessageDialog(this, "Nem találom a(z) " + a + " filet!");
            }
        }

    }

    public void HorizontalsToTable(String riportname) throws FileNotFoundException, ParseException {

        //felvesszük a tábla modelljét
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable13.getModel();
        // model.setRowCount(0);
        //model.setColumnCount(0);
//letrehozunk egy olvasot
        BufferedReader in;

        // String pathfile = path + "\\\\" + CTB.Horizontals.get(i) + ".tab";
        File File = new File(riportname);
        try {
            in = new BufferedReader(new FileReader(File));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {

                String[] cells = line.split("\\t");
                model.addRow(cells);

            }

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Nem találom a(z) " + CTB.Horizontals.get(i) + " filet!");
            warning.SetMessage("Nem találom a(z) " + riportname + " filet!");
        }

//át kéne alakítani a dátumokat hetekké a modellben
        for (int s = 0; s < model.getRowCount(); s++) {

            for (int o = 0; o < model.getColumnCount(); o++) {
                try {
                    if (model.getValueAt(s, o).toString().contains("Week")) {
//a week alatti cella lesz a dátum
                        String datestring = model.getValueAt(s + 1, o).toString();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                        Date date = df.parse(datestring);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int week = cal.get(Calendar.WEEK_OF_YEAR);
                        int year = cal.get(Calendar.YEAR);
                        if (week == 1) {
                            cal.add(Calendar.DATE, 4);
                        }
                        int yearplusz = cal.get(Calendar.YEAR);

                        if (year == yearplusz) {
                            model.setValueAt(String.valueOf(year).substring(2, 4) + String.format("%02d", week), s + 1, o);

                        } else {

                            model.setValueAt(String.valueOf(yearplusz).substring(2, 4) + String.format("%02d", week), s + 1, o);

                        }

                    }
                } catch (Exception e) {
                }
            }
        }

        jTable13.setModel(model);
        CTB.TablaOszlopSzelesseg(jTable13);

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
        jPopupMenu3 = new javax.swing.JPopupMenu();
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
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();
        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
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
        jPanel10 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem9 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem10 = new javax.swing.JCheckBoxMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem6 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem13 = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem7 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem8 = new javax.swing.JCheckBoxMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem11 = new javax.swing.JCheckBoxMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem12 = new javax.swing.JCheckBoxMenuItem();

        torles.setText("Kijelölés törlése!");
        torles.setName(""); // NOI18N
        torles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torlesActionPerformed(evt);
            }
        });
        jPopupMenu1.add(torles);

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
        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jTable1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable1.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Open PO", "Stock", "Open JOB", "Need to build", "CTB", "Plan", "Comment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCellSelectionEnabled(true);
        jTable1.setName("CTB_Table"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
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

        jTextField2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jScrollPane9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jTable9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Anyagszám", "Felhasználható mennyiség", "Leírása", "Jelenlegi készlet", "Master megjegyzése", "OpSeq", "Supply", "Beépülés", "Elhagyott mennyiség", "Horizontal adat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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

        jTextField10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jScrollPane11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jTable11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable11.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Plan", "Shipped", "Need to plan", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52"
            }
        ));
        jTable11.setCellSelectionEnabled(true);
        jTable11.setComponentPopupMenu(jPopupMenu1);
        jTable11.setName("Data_In_Table"); // NOI18N
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

        jLabel20.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel20.setText("Kijelölés összege:");

        jTextField15.setBackground(new java.awt.Color(204, 204, 204));
        jTextField15.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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

        jTextField16.setBackground(new java.awt.Color(204, 204, 204));
        jTextField16.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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

        jProgressBar1.setStringPainted(true);

        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addGap(6, 6, 6)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1038, 1038, 1038))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(jScrollPane9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11))
            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel20))
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("CTB", jPanel1);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Components", "Description", "Raw OH", "WO Variancia", "New Prod", "Lost", "Horizontal", "Master Comment", "Total OH"
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

        jButton1.setText("Export to Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1417, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BOM + OH (Generated)", jPanel8);

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

        jLabel4.setText("Sorok hozzáadása:");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jButton2.setText("Felvitt adatok mentése");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1417, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE))
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
                .addContainerGap(1214, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
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
                .addContainerGap(1214, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
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
                .addGap(6, 6, 6)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane5)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel13))
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
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
                .addContainerGap(1220, Short.MAX_VALUE))
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
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Indented BOM", jPanel7);

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
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 1417, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Shipping", jPanel10);

        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartNumber", "Qty", "Komment"
            }
        ));
        jTable12.setName("Lost"); // NOI18N
        jScrollPane14.setViewportView(jTable12);
        if (jTable12.getColumnModel().getColumnCount() > 0) {
            jTable12.getColumnModel().getColumn(0).setMinWidth(50);
            jTable12.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable12.getColumnModel().getColumn(0).setMaxWidth(300);
            jTable12.getColumnModel().getColumn(1).setMinWidth(50);
            jTable12.getColumnModel().getColumn(1).setPreferredWidth(75);
            jTable12.getColumnModel().getColumn(1).setMaxWidth(100);
        }

        jButton6.setText("Ment");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel22.setText("Kereső:");

        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
        });

        jLabel23.setText("Sor hozzáadása:");

        jButton7.setText("+");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(929, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane14)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(jButton7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lost", jPanel11);

        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22", "Title 23", "Title 24", "Title 25", "Title 26", "Title 27", "Title 28", "Title 29", "Title 30", "Title 31", "Title 32", "Title 33", "Title 34", "Title 35", "Title 36", "Title 37", "Title 38", "Title 39", "Title 40", "Title 41", "Title 42", "Title 43", "Title 44", "Title 45", "Title 46", "Title 47", "Title 48", "Title 49", "Title 50", "Title 51", "Title 52", "Title 53", "Title 54", "Title 55", "Title 56", "Title 57", "Title 58", "Title 59", "Title 60", "Title 61", "Title 62", "Title 63", "Title 64", "Title 65", "Title 66", "Title 67", "Title 68", "Title 69", "Title 70", "Title 71", "Title 72", "Title 73"
            }
        ));
        jTable13.setCellSelectionEnabled(true);
        jTable13.setName("Horizontal_Table"); // NOI18N
        jScrollPane15.setViewportView(jTable13);

        jLabel24.setText("Kereső:");

        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField19KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1417, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Horizontal", jPanel12);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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

        jMenuItem2.setText("Elhagyott anyagok szerkesztése");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem5.setText("Segítség ablak");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem3.setText("Pivotálás");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Beállítások");

        jMenuItem6.setText("Control Panel");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);
        jMenu2.add(jSeparator3);

        jCheckBoxMenuItem9.setSelected(true);
        jCheckBoxMenuItem9.setText("Horizontál adatok mutatása");
        jCheckBoxMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItem9);

        jCheckBoxMenuItem10.setSelected(true);
        jCheckBoxMenuItem10.setText("Elhagyott anyagok mutatása");
        jCheckBoxMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItem10);

        jMenuBar1.add(jMenu2);

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

        jCheckBoxMenuItem13.setText("Add SA");
        jCheckBoxMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem13ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem13);
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

        buttonGroup2.add(jCheckBoxMenuItem5);
        jCheckBoxMenuItem5.setText("OH All location");
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem5);

        buttonGroup2.add(jCheckBoxMenuItem4);
        jCheckBoxMenuItem4.setText("OH Stage only");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem4);
        jMenu1.add(jSeparator2);

        jCheckBoxMenuItem7.setSelected(true);
        jCheckBoxMenuItem7.setText("Calc with Lost");
        jMenu1.add(jCheckBoxMenuItem7);

        jCheckBoxMenuItem8.setText("Calc with Horizontals");
        jCheckBoxMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem8);
        jMenu1.add(jSeparator4);

        jCheckBoxMenuItem11.setSelected(true);
        jCheckBoxMenuItem11.setText("Calc with Entered PO");
        jCheckBoxMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem11);
        jMenu1.add(jSeparator5);

        jCheckBoxMenuItem12.setText("Calc with FGOODS (Stock calc)");
        jCheckBoxMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem12);

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

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        jTable1.revalidate();
        universalfilter uf = new universalfilter(jTextField2.getText().trim(), this.jTable1);
        //universalfilter um = new universalfilter(jTextField2.getText().trim(), this.jTable11);
        //indítunk egy topshortot is ugy hogy megadjuk a pn-t trimelve
        CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread(jTextField2.getText().trim());
        t.start();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable11KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            setCursor(new Cursor(Cursor.WAIT_CURSOR));
//összehasonlítjuk a táblákat
            CTB_NEW_CompareTables c = new CTB_NEW_CompareTables(jTable11, jTable1);
            CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(c.compare(), CTB_NEW_FullCalc.calculations.FULL);
            try {
                f.start();
            } catch (Exception e) {
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        }

    }//GEN-LAST:event_jTable11KeyPressed

    private void jTable11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable11KeyReleased
        // TODO add your handling code here:
        new CTB_NEW_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, 2);
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread();
            t.start();
        }

    }//GEN-LAST:event_jTable11KeyReleased

    private void jTable11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable11MouseReleased
        // TODO add your handling code here:
        new CTB_NEW_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, 2);

        CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread();
        t.start();


    }//GEN-LAST:event_jTable11MouseReleased

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        // TODO add your handling code here:
        jTable11.revalidate();
        universalfilter u = new universalfilter(jTextField10.getText().trim(), this.jTable11);
        // universalfilter m = new universalfilter(jTextField10.getText().trim(), this.jTable1);
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
//adatok betöltése lokális fileokból

        setCursor(new Cursor(Cursor.WAIT_CURSOR));
//a ...tab filekot betesszük a sheetekbe majd elvégezzük a számolásokat
        try {
            FilesToTables();
        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }
        //indítunk egy fullcalcot
//        CTB_NEW_FullCalc full = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
//        full.start();
        CTB_NEW_FullCalc full = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        full.start();

        //jTable1.setModel(v.ctbmodel);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        universalfilter u = new universalfilter(jTextField12.getText().trim(), jTable10);
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
        //ha a  rawoh-n klikelünk akkor jelenjen meg az ablak
        if (CTB.jTable9.getSelectedColumn() == 3) {
            rawedit.jLabel2.setText(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString());
            rawedit.jTextArea1.setText(jTable9.getValueAt(jTable9.getSelectedRow(), 4).toString());
            rawedit.setVisible(true);
            rawedit.jTextField1.requestFocus();
            //elhagyott anyagok szerkesztése
        } else if (CTB.jTable9.getSelectedColumn() == 8) {
            lostedit.jTextField2.setText(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString());
            lostedit.jTextField1.setText(jTable9.getValueAt(jTable9.getSelectedRow(), 8).toString());
//a komment beírása 
            for (int i = 0; i < jTable12.getRowCount(); i++) {
                try {
                    if (jTable12.getValueAt(i, 0).toString().equals(lostedit.jTextField2.getText())) {

                        lostedit.jTextArea1.setText(jTable12.getValueAt(i, 2).toString());
                    }
                } catch (Exception e) {
                }

            }
            lostedit.setVisible(true);

        } //ha a pn-en klikkelünk az oh tábla nyíljon meg lekeresve
        else if (CTB.jTable9.getSelectedColumn() == 1) {

            jTabbedPane1.setSelectedIndex(2);
            jTextField1.setText(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString());
            universalfilter uf = new universalfilter(jTextField1.getText().trim(), jTable2);

        }

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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // 

        exportshorty.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // TODO add your handling code here:

        CTB_NEW_FullCalc calc = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        calc.start();


    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jCheckBoxMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem2ActionPerformed
        // TODO add your handling code here:
        CTB_NEW_FullCalc calc = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        calc.start();
    }//GEN-LAST:event_jCheckBoxMenuItem2ActionPerformed

    private void jCheckBoxMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem3ActionPerformed

        // TODO add your handling code here:
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.CHANGEOH);
        f.start();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jCheckBoxMenuItem3ActionPerformed

    private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.CHANGEOH);
        f.start();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


    }//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

    private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.CHANGEOH);
        f.start();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

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

    private void jCheckBoxMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem6ActionPerformed
        // TODO add your handling code here:

        CTB_NEW_FullCalc calc = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        calc.start();

    }//GEN-LAST:event_jCheckBoxMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        whereused.setPn(jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString().trim());
        whereused.getData();
        whereused.setVisible(true);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
        //kereső
        universalfilter u = new universalfilter(jTextField17.getText().trim(), jTable12);

    }//GEN-LAST:event_jTextField17KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //sor hozzáadása a lost táblához

        //megnézzük , hogy inté convertálató e a texfield adata
        int pluszsor = 0;
        try {

            pluszsor = Integer.parseInt(jTextField18.getText());

        } catch (Exception e) {

            warning.SetMessage("Nem jó sor mennyiséget adtál meg!");
//            JOptionPane.showMessageDialog(this,
//                    cccccc,
//                    "Hiba!",
//                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //kiszedjük a modellt
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable12.getModel();

        for (int i = 0; i < pluszsor; i++) {

            model.addRow(new Object[]{"", "", " "});

        }

        jTable12.setModel(model);


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {

            //bejárjuk a tábla adatait és kiirjuk a fileba
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable12.getModel();

//megprobáljuk számmá convertálni a qty -t ha nem sikerül kiirjuk és kiállunk
            for (int i = 0; i < model.getRowCount(); i++) {

                if (!model.getValueAt(i, 0).toString().equals("")) {

                    try {
                        Integer.parseInt(model.getValueAt(i, 1).toString());
                    } catch (Exception e) {
                        warning.SetMessage("Nem jó számot adtál meg a " + String.valueOf(i + 1) + ". sorban!");
//
//                        JOptionPane.showMessageDialog(this,
//                                "Nem jó számot adtál meg a " + String.valueOf(i + 1) + ". sorban!",
//                                "Adat hiba!",
//                                JOptionPane.ERROR_MESSAGE);
                        CTB_LostRead r = new CTB_LostRead();
                        r.olvas();
                        return;
                    }

                }
            }

            // lost file mentése
            String filepath = "C:\\Users\\" + System.getProperty("user.name") + "\\";
            File file = new File(filepath + "CTB_Lost.ini");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);

            for (int i = 0; i < model.getRowCount(); i++) {
//ha üres a pn nem irjuk ki azt a sort
                if (model.getValueAt(i, 0).equals("")) {

                    continue;

                }

                bw.write(model.getValueAt(i, 0) + ";" + model.getValueAt(i, 1) + ";" + model.getValueAt(i, 2));
                bw.newLine();

            }

            bw.close();

            tick.SetMessage("Sikeres mentés!");
//            JOptionPane.showMessageDialog(this,
//                    "Sikeres mentés!");

//visszaolvassuk
            CTB_LostRead r = new CTB_LostRead();
            r.olvas();

        } catch (IOException ex) {
            Logger.getLogger(CTB.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // show control panel
        control.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //export to excel
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable7, new File(fileToSave.getAbsolutePath() + ".xls"));

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        //egér kattintás 
        //ha a komment oszlopban vagyunk

        if (jTable1.getSelectedColumn() == 7) {
            //beállítjuk a partnumbert a labelbe
            komment.jLabel2.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
            //megkeressük a kommentet a pndatasban es beallítjuk a textareába
            for (int i = 0; i < PnDatas.size(); i++) {

                if (PnDatas.get(i)[1].equals(komment.jLabel2.getText())) {

                    komment.jTextArea1.setText(PnDatas.get(i)[2]);
                    break;
                } else {

                    komment.jTextArea1.setText("");

                }

            }

            //megjelenítjük a komment ablakot
            komment.setVisible(true);

        }

        new CTB_NEW_LinkTables(jScrollPane1, jTable1, jScrollPane11, jTable11, 1);
        CTB_NEW_TopShortThread t = new CTB_NEW_TopShortThread();
        t.start();

    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // lostedit
        lostedit.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jCheckBoxMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem8ActionPerformed
        //futtassuk le a kalkulációkat
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.CHANGEOH);
        f.start();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_jCheckBoxMenuItem8ActionPerformed

    private void jTextField19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyReleased
        // TODO add your handling code here:
        universalfilter u = new universalfilter(jTextField19.getText().trim(), jTable13);
    }//GEN-LAST:event_jTextField19KeyReleased

    private void jCheckBoxMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem9ActionPerformed
        // TODO add your handling code here:
        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(true, CTB_NEW_FullCalc.calculations.FULL);
        f.start();
    }//GEN-LAST:event_jCheckBoxMenuItem9ActionPerformed

    private void jCheckBoxMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem10ActionPerformed
        // TODO add your handling code here:
        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(true, CTB_NEW_FullCalc.calculations.CHANGELOST);
        f.start();
    }//GEN-LAST:event_jCheckBoxMenuItem10ActionPerformed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        //sorok hozzáadása
        //ha enter

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//megpróbáljuk integerré alakítani a beírt szöveget
            try {

                for (int i = 0; i < Integer.parseInt(jTextField3.getText()); i++) {

                    CTB_NEW_Variables.ohmodel.addRow(new Object[]{"STOCK1", "", "", "", "Net-Asset", "", "", "", "", "", ""});

                }
//beshortoljuk a táblát
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable2.getModel());
                jTable2.setRowSorter(sorter);
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                int columnIndexToSort = 2;
                sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
                sorter.setSortKeys(sortKeys);
                sorter.sort();
                tick.SetMessage("Hozzáadtunk " + jTextField3.getText() + " sort!");

            } catch (Exception e) {

                warning.SetMessage("Valószínűleg nem számot adtál meg!");
                jTextField3.setText("");
                jTextField3.requestFocus();
            }

        }

    }//GEN-LAST:event_jTextField3KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // az új oh adatok mentése
        //megállítjuk a szerkesztést
        if (jTable2.isEditing()) {
            jTable2.getCellEditor().stopCellEditing();

        }
        //végigjárjuk a táblát és kiszedjük azokat a sorokat amikben nincs pn
        for (int i = 0; i < CTB_NEW_Variables.ohmodel.getRowCount(); i++) {

            if (CTB_NEW_Variables.ohmodel.getValueAt(i, 2).equals("")) {

                CTB_NEW_Variables.ohmodel.removeRow(i);
                i--;

            }
//ha nem számot adott meg darabnak
            try {
                if (!CTB_NEW_Variables.ohmodel.getValueAt(i, 2).toString().contains("Part")) {
                    Integer.parseInt(CTB_NEW_Variables.ohmodel.getValueAt(i, 6).toString().replace(",", ""));
                }

            } catch (Exception e) {
                warning.SetMessage("Nem jó darabszámot adtál meg a " + CTB_NEW_Variables.ohmodel.getValueAt(i, 2).toString() + " esetében, töröljük!");
            }

        }
//vissza kell térni a ctb oldalra és indítani egy kalkulációt
        jTabbedPane1.setSelectedIndex(0);
        CTB_NEW_FullCalc f = new CTB_NEW_FullCalc(true, CTB_NEW_FullCalc.calculations.CHANGEOH);
        f.start();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // segítség ablak
        helper.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jCheckBoxMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem11ActionPerformed
        // entered po belevétele
        CTB_NEW_FullCalc full = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        full.start();
    }//GEN-LAST:event_jCheckBoxMenuItem11ActionPerformed

    private void jCheckBoxMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem12ActionPerformed
        // fgoods a stock esetében kell vagy nem
        CTB_NEW_FullCalc full = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        full.start();
    }//GEN-LAST:event_jCheckBoxMenuItem12ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // pivot csinálás
        makePivot();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jCheckBoxMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem13ActionPerformed
        //add sa
        CTB_NEW_FullCalc full = new CTB_NEW_FullCalc(false, CTB_NEW_FullCalc.calculations.FULL);
        full.start();

    }//GEN-LAST:event_jCheckBoxMenuItem13ActionPerformed

    public void makePivot() {

        //elindulunk bejárjuk az adatbeviteli táblát
        for (int i = 0; i < CTB_NEW_Variables.tervtablemodel.getRowCount(); i++) {
//ha nem üres a sor
            if (!CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0).toString().equals("")) {
//felvesszük a pn-t
                String pn = CTB_NEW_Variables.tervtablemodel.getValueAt(i, 0).toString().trim();
//továbbmegyünk a táblában és keresünk még ilyen pn-eket
                for (int n = i + 1; n < CTB_NEW_Variables.tervtablemodel.getRowCount(); n++) {
//ha egyezik a pn
                    if (pn.equals(CTB_NEW_Variables.tervtablemodel.getValueAt(n, 0).toString().trim())) {
//bejárjuk az oszlopokat és ahol van szám, azt hozzáadjuk az i edik sorban szereplő összeghez
                        for (int c = 4; c < CTB_NEW_Variables.tervtablemodel.getColumnCount(); c++) {
//ha van benne szám       
                            try {
                                if (!CTB_NEW_Variables.tervtablemodel.getValueAt(n, c).toString().equals("")) {

//az első számnak léteznie kell!!
                                    int elso = 0;
                                    try {
                                        elso = Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(i, c).toString().trim());
                                    } catch (Exception e) {
                                    }

//akkor összeadjuk az i -edik oszlopba
                                    CTB_NEW_Variables.tervtablemodel.setValueAt(elso + Integer.parseInt(CTB_NEW_Variables.tervtablemodel.getValueAt(n, c).toString().trim()), i, c);

                                }
                            } catch (Exception e) {
                            }
                        }
//kitöröljük az n edik sort és visszaugrunk egyet az i vel
                        CTB_NEW_Variables.tervtablemodel.removeRow(n);
                        //i--;

                    }

                }

            }

        }

    }

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlannTool/PICTURES/ctb1.jpg")));

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
            //table.repaint();
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
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem10;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem11;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem12;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem13;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem6;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem7;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem8;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem9;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    public static javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu3;
    public static javax.swing.JProgressBar jProgressBar1;
    public static javax.swing.JProgressBar jProgressBar2;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable10;
    public static javax.swing.JTable jTable11;
    public static javax.swing.JTable jTable12;
    public static javax.swing.JTable jTable13;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    public static javax.swing.JTable jTable6;
    public static javax.swing.JTable jTable7;
    public static javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JMenuItem torles;
    // End of variables declaration//GEN-END:variables

}
