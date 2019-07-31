package PlannTool;

import PlannTool.ANIMATIONS.animationpicture;
import PlannTool.GAME.jatek;
import PlannTool.ANIMATIONS.animation;
import PlannTool.CONNECTS.tigerconnect;
import PlannTool.CONNECTS.connect;
import PlannTool.CONNECTS.planconnect;
import PlannTool.CONNECTS.szaifconn;
import PlannTool.CONNECTS.neximconnect;
import PlannTool.CONNECTS.postgreconnect;
import PlannTool.BACKEND.Tc_Bejelentkezes;
import static PlannTool.CTB_CALC.CTB.jTable1;
import static PlannTool.CTB_CALC.CTB.jTable11;
import PlannTool.CTB_CALC.CTB_Bejel;
import PlannTool.CTB_CALC.CTB_Columnrenderer;
import PlannTool.CTB_CALC.CTB_Filechooser;
import PlannTool.CTB_CALC.CTB_PartsToPlanTableRenderer;
import PlannTool.CompleteQty.Methods;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.RowFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import static java.time.Instant.now;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableCellRenderer;
import java.time.LocalDateTime;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import static java.time.LocalDateTime.now;
import java.time.temporal.IsoFields;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.LocalDateTime.now;
import java.time.temporal.WeekFields;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ablak extends javax.swing.JFrame {

    public static DefaultTableModel model = new DefaultTableModel();
    public static DefaultTableModel model1 = new DefaultTableModel();
    public static DefaultTableModel modelacti = new DefaultTableModel();
    public static DefaultTableModel modelstatus = new DefaultTableModel();
    public static DefaultTableModel jobstatus = new DefaultTableModel();
    public DefaultTableModel pcbtabla = new DefaultTableModel();
    public JTable jtable4 = new JTable();
    public Date datum;
    public static Stat stat = new Stat();
//szamoljuk a kereses inditasat
    public static int wgcounter;
    public static LocalDateTime elso = LocalDateTime.now();
    public static String pref;
    public static List<String[][]> lista = new ArrayList<>();   //az rtv tabla oh keszletenek listaja
    public static boolean planner = false;  //a planneri bejelentkezes vizsgalatanak eredmenye
    public static String user = "Ismeretlen"; //a bejelntkezett felhasznalo a be tervezobe
    public static boolean muvez = false;
    public static String rev;
    public static String regirev;
    public static List<String[][]> modelactilist = new ArrayList<>();
    public static List<String[]> wiplist = new ArrayList<>();
    public static String[][] alkatreszkereso = null;
    public static Icon defaulticon = null;

    public ablak() throws SQLException {

        initComponents();
        jTabbedPane1.setUI(new MyTabbedPaneUI(jTabbedPane1));
        seticon();

        jTextField4.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                String query = jTextField4.getText().trim();
                filter1(query);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String query = jTextField4.getText().trim();
                filter1(query);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String query = jTextField4.getText().trim();
                filter1(query);
            }
        });

        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                String query = jTextField1.getText().trim();
                filter(query);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String query = jTextField1.getText().trim();
                filter(query);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String query = jTextField1.getText().trim();
                filter(query);
            }
        });
//beillesztehtővé tesszuk a táblákat
        new ExcelAdapter(jTable4);
        new ExcelAdapter(jTable18);
        new ExcelAdapter(jTable19);
        new ExcelAdapter(jTable16);
        new ExcelAdapter(jTable17);
        new ExcelAdapter(jTable26);

        jTable11.getColumnModel().getColumn(0).setCellRenderer(new Tooltiprenderer());

        //beillesztehtővé tétele a gyártás infónak
        new ExcelAdapter(jTable15);

//renderereljük a prio táblát
        jTable25.getTableHeader().setDefaultRenderer(new Prio_Columnrenderer());
        jTable25.setDefaultRenderer(Object.class, new Prio_Tablerenderer());

//lefuttatjuk a default icom beállítását (kinek milyen animációja legyen)
        ImgUp u = new ImgUp();
        u.CreateDefaultIcon();

    }

    private void seticon() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("PICTURES/1.jpg")));

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable18 = new javax.swing.JTable();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTable19 = new javax.swing.JTable();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable17 = new javax.swing.JTable();
        jButton22 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel25 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jButton15 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel35 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                Object value = getModel().getValueAt(row, col);
                try{
                    if (value.equals("Nem létezik SFDC-ben!")) {
                        comp.setBackground(Color.red);
                        comp.setForeground(Color.BLACK);
                    }
                    else if (value.equals("Minden elindult!")) {
                        comp.setBackground(Color.green);
                        comp.setForeground(Color.BLACK);
                    }

                    else if(value.equals("42Q not released!")){
                        comp.setBackground(Color.orange);
                        comp.setForeground(Color.BLACK);

                    }

                    else if(value.equals("JOB Cancelled!")){
                        comp.setBackground(new Color(133, 162, 209));
                        comp.setForeground(Color.BLACK);

                    }
                    else {
                        comp.setBackground(Color.white);
                        comp.setForeground(Color.BLACK);
                    }}
                    catch(Exception e){}
                    return comp;
                }
            }
            ;
            jButton18 = new javax.swing.JButton();
            jCheckBox5 = new javax.swing.JCheckBox();
            jPanel11 = new javax.swing.JPanel();
            jScrollPane17 = new javax.swing.JScrollPane();
            jTable15 = new javax.swing.JTable();
            jButton20 = new javax.swing.JButton();
            jButton19 = new javax.swing.JButton();
            jPanel12 = new javax.swing.JPanel();
            jPanel13 = new javax.swing.JPanel();
            jScrollPane18 = new javax.swing.JScrollPane();
            jTable16 = new javax.swing.JTable();
            jButton21 = new javax.swing.JButton();
            jPanel14 = new javax.swing.JPanel();
            jPanel15 = new javax.swing.JPanel();
            jButton24 = new javax.swing.JButton();
            jTextField7 = new javax.swing.JTextField();
            jLabel12 = new javax.swing.JLabel();
            jScrollPane20 = new javax.swing.JScrollPane();
            jTable20 = new javax.swing.JTable();
            jScrollPane21 = new javax.swing.JScrollPane();
            jTable21 = new javax.swing.JTable();
            jLabel38 = new javax.swing.JLabel();
            jLabel39 = new javax.swing.JLabel();
            jPanel16 = new javax.swing.JPanel();
            jPanel17 = new javax.swing.JPanel();
            jButton25 = new javax.swing.JButton();
            jTextField16 = new javax.swing.JTextField();
            jScrollPane24 = new javax.swing.JScrollPane();
            jTable22 = new javax.swing.JTable();
            jLabel40 = new javax.swing.JLabel();
            jLabel41 = new javax.swing.JLabel();
            jDateChooser6 = new com.toedter.calendar.JDateChooser();
            jLabel42 = new javax.swing.JLabel();
            jLabel43 = new javax.swing.JLabel();
            jDateChooser7 = new com.toedter.calendar.JDateChooser();
            jScrollPane25 = new javax.swing.JScrollPane();
            jTable23 = new javax.swing.JTable();
            jButton26 = new javax.swing.JButton();
            jLabel44 = new javax.swing.JLabel();
            jTextField20 = new javax.swing.JTextField();
            jLabel45 = new javax.swing.JLabel();
            jTextField21 = new javax.swing.JTextField();
            jComboBox1 = new javax.swing.JComboBox<>();
            jPanel18 = new javax.swing.JPanel();
            jPanel19 = new javax.swing.JPanel();
            jLabel46 = new javax.swing.JLabel();
            jDateChooser8 = new com.toedter.calendar.JDateChooser();
            jLabel47 = new javax.swing.JLabel();
            jDateChooser9 = new com.toedter.calendar.JDateChooser();
            jLabel48 = new javax.swing.JLabel();
            jTextField22 = new javax.swing.JTextField();
            jTextField23 = new javax.swing.JTextField();
            jLabel49 = new javax.swing.JLabel();
            jButton27 = new javax.swing.JButton();
            jScrollPane26 = new javax.swing.JScrollPane();
            jTable24 = new javax.swing.JTable();
            jScrollPane27 = new javax.swing.JScrollPane();
            jList2 = new javax.swing.JList<>();
            jLabel50 = new javax.swing.JLabel();
            jLabel51 = new javax.swing.JLabel();
            jTextField24 = new javax.swing.JTextField();
            jTextField25 = new javax.swing.JTextField();
            jTextField26 = new javax.swing.JTextField();
            jLabel52 = new javax.swing.JLabel();
            jLabel53 = new javax.swing.JLabel();
            jButton28 = new javax.swing.JButton();
            jPanel20 = new javax.swing.JPanel();
            jPanel21 = new javax.swing.JPanel();
            jPanel22 = new javax.swing.JPanel();
            jScrollPane28 = new javax.swing.JScrollPane();
            jTable25 = new javax.swing.JTable();
            jLabel54 = new javax.swing.JLabel();
            jComboBox2 = new javax.swing.JComboBox<>();
            jButton29 = new javax.swing.JButton();
            jLabel55 = new javax.swing.JLabel();
            jComboBox3 = new javax.swing.JComboBox<>();
            jPanel23 = new javax.swing.JPanel();
            jPanel24 = new javax.swing.JPanel();
            jDateChooser10 = new com.toedter.calendar.JDateChooser();
            jButton30 = new javax.swing.JButton();
            jScrollPane29 = new javax.swing.JScrollPane();
            jTable26 = new javax.swing.JTable();
            jScrollPane30 = new javax.swing.JScrollPane();
            jTable27 = new javax.swing.JTable();
            jCheckBox6 = new javax.swing.JCheckBox();
            jScrollPane31 = new javax.swing.JScrollPane();
            jTable28 = new javax.swing.JTable();

            jMenuItem1.setText("Bontás SN-re!");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jPopupMenu2.add(jMenuItem1);

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("PlannTool R_10.0");
            setLocation(new java.awt.Point(500, 300));

            jPanel6.setBackground(new java.awt.Color(255, 255, 255));
            jPanel6.setPreferredSize(new java.awt.Dimension(1100, 550));
            jPanel6.setLayout(new java.awt.BorderLayout());

            jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));
            jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            jScrollPane7.setPreferredSize(new java.awt.Dimension(1100, 600));

            jTabbedPane1.setBackground(new java.awt.Color(255, 51, 51));
            jTabbedPane1.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
            jTabbedPane1.setOpaque(true);
            jTabbedPane1.setPreferredSize(new java.awt.Dimension(1100, 550));
            jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jTabbedPane1StateChanged(evt);
                }
            });

            jPanel1.setPreferredSize(new java.awt.Dimension(1100, 550));

            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton1.setToolTipText("Lekér");
            jButton1.setAlignmentX(500.0F);
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

            jLabel2.setText("Mit keresünk?");

            jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTextField2KeyPressed(evt);
                }
            });

            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "PartNumber", "Workstation", "Qty"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable1.setComponentPopupMenu(jPopupMenu2);
            jTable1.setOpaque(false);
            jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable1MouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(jTable1);

            jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
            jLabel3.setText("All Wip");

            jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Item", "Subinv", "Locator", "Qty"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable2MouseClicked(evt);
                }
            });
            jScrollPane2.setViewportView(jTable2);

            jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
            jLabel4.setText("OH");

            jTextField1.setPreferredSize(new java.awt.Dimension(8, 20));
            jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTextField1KeyReleased(evt);
                }
            });

            jLabel1.setText("Kereső:");

            jLabel10.setText("All Wip tábla összegzése:");

            jTextField6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField6ActionPerformed(evt);
                }
            });

            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png"))); // NOI18N
            jButton3.setToolTipText("Exportálás excelbe!");
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

            jLabel18.setText("Kijelölések összege:");

            jTextField13.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField13ActionPerformed(evt);
                }
            });

            jCheckBox4.setText("-SMT");
            jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox4ActionPerformed(evt);
                }
            });

            jLabel21.setForeground(new java.awt.Color(255, 0, 0));

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCheckBox4))
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2346, Short.MAX_VALUE)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(13, 13, 13))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1)))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(27, 27, 27)
                                    .addComponent(jLabel2)
                                    .addGap(452, 452, 452)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(563, 563, 563)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(344, 344, 344))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(33, 33, 33)
                                    .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(34, 34, 34))
            );

            jTabbedPane1.addTab("Készlet", jPanel1);

            jLabel5.setText("Tól:");

            jLabel6.setText("Ig:");

            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton2.setToolTipText("Lekér");
            jButton2.setBorderPainted(false);
            jButton2.setContentAreaFilled(false);
            jButton2.setPreferredSize(new java.awt.Dimension(132, 33));
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

            jLabel7.setText("Keresett termékszám:");

            jTable3.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Workstation", "Part Number", "Pass", "Shop Order Number", "Sumpass Qty", "Move Qty", "Manu Move Qty"
                }
            ));
            jTable3.setCellSelectionEnabled(true);
            jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable3MouseClicked(evt);
                }
            });
            jScrollPane3.setViewportView(jTable3);

            jLabel8.setText("Kereső:");

            jTextField4.setPreferredSize(new java.awt.Dimension(6, 25));
            jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTextField4KeyReleased(evt);
                }
            });

            jTextField5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField5ActionPerformed(evt);
                }
            });

            jLabel9.setText("Tábla összegzése:");

            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png"))); // NOI18N
            jButton4.setToolTipText("Exportálás excelbe!");
            jButton4.setBorderPainted(false);
            jButton4.setContentAreaFilled(false);
            jButton4.setPreferredSize(new java.awt.Dimension(132, 33));
            jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton4MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton4MouseExited(evt);
                }
            });
            jButton4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });

            jLabel13.setText("óra:perc");

            jLabel14.setText("óra:perc");

            jLabel19.setText("Kijelölések összege:");

            jLabel20.setText("Kijelölések összege:");

            jLabel22.setText("Kijelölések összegzése:");

            jTable18.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Stations"
                }
            ));
            jTable18.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable18MouseClicked(evt);
                }
            });
            jTable18.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTable18KeyReleased(evt);
                }
            });
            jScrollPane22.setViewportView(jTable18);

            jTable19.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Prefixes"
                }
            ));
            jTable19.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable19MouseClicked(evt);
                }
            });
            jTable19.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTable19KeyReleased(evt);
                }
            });
            jScrollPane23.setViewportView(jTable19);

            jCheckBox2.setText("Group by ..");
            jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox2ActionPerformed(evt);
                }
            });

            jCheckBox3.setText("Pirst Pass O..");
            jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox3ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(116, 116, 116)
                            .addComponent(jLabel22)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel9)
                            .addGap(4, 4, 4)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(291, 291, 291))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jScrollPane3))))
                    .addContainerGap(2368, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3141, 3141, 3141)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3141, 3141, 3141)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(36, 36, 36)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(33, 33, 33)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(42, 42, 42)
                            .addComponent(jCheckBox2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCheckBox3)))
                    .addGap(17, 17, 17)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(178, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(645, 645, 645)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(645, 645, 645)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            );

            jTabbedPane1.addTab("Activity", jPanel2);

            jTable4.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null}
                },
                new String [] {
                    "Serial N", "SN", "JOB", "Part Number", "Location", "Last Comp Date"
                }
            ));
            jTable4.setCellSelectionEnabled(true);
            jScrollPane4.setViewportView(jTable4);

            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton5.setToolTipText("Lekér");
            jButton5.setBorderPainted(false);
            jButton5.setContentAreaFilled(false);
            jButton5.setPreferredSize(new java.awt.Dimension(132, 33));
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

            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png"))); // NOI18N
            jButton6.setToolTipText("Tábla törlése");
            jButton6.setBorderPainted(false);
            jButton6.setContentAreaFilled(false);
            jButton6.setPreferredSize(new java.awt.Dimension(132, 33));
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

            jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/selejt1.png"))); // NOI18N
            jButton7.setToolTipText("Tovább a selejtezéshez");
            jButton7.setBorderPainted(false);
            jButton7.setContentAreaFilled(false);
            jButton7.setEnabled(false);
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

            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png"))); // NOI18N
            jButton8.setToolTipText("Exportálás excelbe!");
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

            jTable11.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "PartN", "QTY"
                }
            ));
            jScrollPane12.setViewportView(jTable11);
            if (jTable11.getColumnModel().getColumnCount() > 0) {
                jTable11.getColumnModel().getColumn(1).setMinWidth(1);
                jTable11.getColumnModel().getColumn(1).setPreferredWidth(40);
                jTable11.getColumnModel().getColumn(1).setMaxWidth(100);
            }

            jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
            jLabel29.setText("RTV QTY");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(2348, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 191, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("SN infó", jPanel3);

            jTable5.setAutoCreateRowSorter(true);
            jTable5.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Shop Order", "Part Number", "Worksation", "QTY", "Unit Status"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable5.setCellSelectionEnabled(true);
            jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable5MouseClicked(evt);
                }
            });
            jScrollPane5.setViewportView(jTable5);

            jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search1.png"))); // NOI18N
            jButton9.setToolTipText("Keres");
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

            jLabel17.setText("Kijelölések összege:");

            jTable17.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Jobszámok"
                }
            ));
            jScrollPane19.setViewportView(jTable17);

            jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png"))); // NOI18N
            jButton22.setToolTipText("Exportálás excelbe!");
            jButton22.setBorderPainted(false);
            jButton22.setContentAreaFilled(false);
            jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton22MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton22MouseExited(evt);
                }
            });
            jButton22.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton22ActionPerformed(evt);
                }
            });

            jLabel37.setText("Exportálás excelbe");

            jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png"))); // NOI18N
            jButton23.setToolTipText("Tábla törlése");
            jButton23.setBorderPainted(false);
            jButton23.setContentAreaFilled(false);
            jButton23.setPreferredSize(new java.awt.Dimension(132, 33));
            jButton23.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton23MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton23MouseExited(evt);
                }
            });
            jButton23.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton23ActionPerformed(evt);
                }
            });

            jLabel11.setText("Adatok törlése");

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(jLabel17))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel37)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 746, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jScrollPane5))
                    .addContainerGap())
            );

            jTabbedPane1.addTab("JOB infó", jPanel4);

            jTable6.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null}
                },
                new String [] {
                    "Partnumber", "PCB PN", "QTY"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane6.setViewportView(jTable6);

            jLabel15.setText("PartNumber:");

            jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTextField10KeyPressed(evt);
                }
            });

            jLabel16.setText("Vagy PCB PN:");

            jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTextField11KeyPressed(evt);
                }
            });

            jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search1.png"))); // NOI18N
            jButton10.setToolTipText("Keres");
            jButton10.setBorderPainted(false);
            jButton10.setContentAreaFilled(false);
            jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton10MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton10MouseExited(evt);
                }
            });
            jButton10.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton10ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jTextField11))
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 892, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(2404, Short.MAX_VALUE))
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Harman PCB Infó ", jPanel5);

            jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/upload1.png"))); // NOI18N
            jButton11.setToolTipText("Feltölt");
            jButton11.setBorderPainted(false);
            jButton11.setContentAreaFilled(false);
            jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton11MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton11MouseExited(evt);
                }
            });
            jButton11.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton11ActionPerformed(evt);
                }
            });

            jLabel23.setText("Mikortól:");

            jLabel24.setText("Meddig:");

            jLabel25.setForeground(new java.awt.Color(255, 0, 0));
            jLabel25.setText("Alapból az utolsó lejkérdezett időpontra áll be!");

            jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton12.setToolTipText("Lekér");
            jButton12.setBorderPainted(false);
            jButton12.setContentAreaFilled(false);
            jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton12MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton12MouseExited(evt);
                }
            });
            jButton12.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton12ActionPerformed(evt);
                }
            });

            jTable7.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Part Number", "Serial Number", "Tól ", "Ig", "OH"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane8.setViewportView(jTable7);
            if (jTable7.getColumnModel().getColumnCount() > 0) {
                jTable7.getColumnModel().getColumn(0).setResizable(false);
                jTable7.getColumnModel().getColumn(4).setResizable(false);
                jTable7.getColumnModel().getColumn(4).setPreferredWidth(5);
            }

            jTable8.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Prefix"
                }
            ));
            jScrollPane9.setViewportView(jTable8);

            jLabel26.setForeground(new java.awt.Color(255, 0, 0));
            jLabel26.setText("Alapból az aktuális dátumra áll be!");

            jButton13.setText("Prefixek mentése");
            jButton13.setToolTipText("Prefixek mentése");
            jButton13.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton13ActionPerformed(evt);
                }
            });

            jButton14.setText("Lekérdezés visszamenőleg!");
            jButton14.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton14ActionPerformed(evt);
                }
            });

            jTable9.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Partnumber", "QTY", "P"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable9.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable9MouseClicked(evt);
                }
            });
            jScrollPane10.setViewportView(jTable9);
            if (jTable9.getColumnModel().getColumnCount() > 0) {
                jTable9.getColumnModel().getColumn(1).setPreferredWidth(5);
                jTable9.getColumnModel().getColumn(2).setMinWidth(30);
                jTable9.getColumnModel().getColumn(2).setMaxWidth(30);
            }

            jLabel27.setText("Summa:");

            jLabel28.setText("Részletes találat:");

            jCheckBox1.setText("Select all");
            jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jCheckBox1ItemStateChanged(evt);
                }
            });

            jTable10.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Kivételek (PN)"
                }
            ));
            jScrollPane11.setViewportView(jTable10);

            jButton15.setText("Kivételek mentése!");
            jButton15.setToolTipText("");
            jButton15.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton15ActionPerformed(evt);
                }
            });

            jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png"))); // NOI18N
            jButton17.setToolTipText("Exportálás excelbe!");
            jButton17.setBorderPainted(false);
            jButton17.setContentAreaFilled(false);
            jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton17MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton17MouseExited(evt);
                }
            });
            jButton17.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton17ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel24))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton15)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel25)
                                    .addGap(5, 5, 5)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(217, 217, 217)
                                    .addComponent(jButton14))))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(378, 378, 378))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(206, 206, 206)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(101, 101, 101)
                                    .addComponent(jLabel27))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap(2352, Short.MAX_VALUE))
            );
            jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(25, 25, 25)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(10, 10, 10)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton15)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton14))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton13)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            );

            jTabbedPane1.addTab("OH Query!", jPanel7);

            jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            jLabel31.setText("Keresett PN:");

            jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            jLabel32.setText("Gyártandó mennyiség:");

            jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton16.setToolTipText("Lekér");
            jButton16.setBorderPainted(false);
            jButton16.setContentAreaFilled(false);
            jButton16.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton16MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton16MouseExited(evt);
                }
            });
            jButton16.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton16ActionPerformed(evt);
                }
            });

            jTable12.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null}
                },
                new String [] {
                    "Oracle PN", "SMT ProgNév", "Sor", "Gyártási oldal", "Panelizáció", "Mértidő", "Gyors mérés", "Kalkulált idő", "Hatékonyság (%)", "Gyártási idő / Óra"
                }
            ));
            jScrollPane13.setViewportView(jTable12);
            if (jTable12.getColumnModel().getColumnCount() > 0) {
                jTable12.getColumnModel().getColumn(0).setMinWidth(150);
                jTable12.getColumnModel().getColumn(0).setPreferredWidth(150);
                jTable12.getColumnModel().getColumn(0).setMaxWidth(200);
                jTable12.getColumnModel().getColumn(1).setMinWidth(150);
                jTable12.getColumnModel().getColumn(1).setPreferredWidth(150);
                jTable12.getColumnModel().getColumn(1).setMaxWidth(200);
                jTable12.getColumnModel().getColumn(9).setMinWidth(170);
                jTable12.getColumnModel().getColumn(9).setPreferredWidth(170);
                jTable12.getColumnModel().getColumn(9).setMaxWidth(300);
            }

            jLabel30.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            jLabel30.setText("Backend");

            jLabel33.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
            jLabel33.setText("SMT");

            jTable13.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "Oracle PN", "Cella", "Workstation", "Darab/Óra", "Gyártási idő / Óra"
                }
            ));
            jScrollPane14.setViewportView(jTable13);

            javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
            jPanel8.setLayout(jPanel8Layout);
            jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(483, 483, 483)
                            .addComponent(jLabel30))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(164, 164, 164)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 1095, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 1095, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel31)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel32)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(21, 21, 21)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(36, 36, 36)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel30))
                        .addComponent(jLabel33))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Daraboló", jPanel8);

            jPanel9.setBackground(new java.awt.Color(204, 204, 204));

            jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/sanmina-logo (2).png"))); // NOI18N

            javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
            jPanel9.setLayout(jPanel9Layout);
            jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(132, 132, 132)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(2473, Short.MAX_VALUE))
            );
            jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jLabel34)
                    .addContainerGap(204, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("BeTervező", jPanel9);

            jList1.setToolTipText("Válaszd ki az állomásaidat!\nA CTR+ klikkel több állomás is kiválasztahtó!");
            jScrollPane15.setViewportView(jList1);

            jLabel35.setText("Sor/Cella");

            jLabel36.setText("Lekérdezés futtatása ettől a dátumtól:");

            jTable14.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Sor/Cella", "Partnumber", "JOB", "Startdate", "JOB Full_Q", "Skeleton/TP15"
                }
            ));
            jScrollPane16.setViewportView(jTable14);
            if (jTable14.getColumnModel().getColumnCount() > 0) {
                jTable14.getColumnModel().getColumn(0).setMinWidth(150);
                jTable14.getColumnModel().getColumn(0).setMaxWidth(200);
            }

            jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton18.setToolTipText("Lekér");
            jButton18.setBorderPainted(false);
            jButton18.setContentAreaFilled(false);
            jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton18MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton18MouseExited(evt);
                }
            });
            jButton18.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton18ActionPerformed(evt);
                }
            });

            jCheckBox5.setText("Elhagyott JOB-ok");
            jCheckBox5.setToolTipText("A megadott dátumtól kérdezzük le azokat a JOB -okat amik végleg kikerültek a tervekből! A Sor/Cella ebben az esetben nem releváns!");

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
            jPanel10.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel36)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCheckBox5)
                            .addGap(0, 2730, Short.MAX_VALUE))
                        .addComponent(jScrollPane16))
                    .addContainerGap())
            );
            jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel36))
                                .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addComponent(jCheckBox5)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("JOB figyelő", jPanel10);

            jTable15.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null}
                },
                new String [] {
                    "PartNumber", "Sor", "Első gyártás", "Utolsó gyártás", "Hányszor ment", "mikor ment utoljára"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    true, false, false, false, true, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable15.setCellSelectionEnabled(true);
            jScrollPane17.setViewportView(jTable15);

            jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png"))); // NOI18N
            jButton20.setToolTipText("Lekér");
            jButton20.setAlignmentX(500.0F);
            jButton20.setBorderPainted(false);
            jButton20.setContentAreaFilled(false);
            jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton20MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton20MouseExited(evt);
                }
            });
            jButton20.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton20ActionPerformed(evt);
                }
            });

            jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png"))); // NOI18N
            jButton19.setToolTipText("Tábla törlése");
            jButton19.setBorderPainted(false);
            jButton19.setContentAreaFilled(false);
            jButton19.setPreferredSize(new java.awt.Dimension(132, 33));
            jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton19MouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton19MouseExited(evt);
                }
            });
            jButton19.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton19ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
            jPanel11.setLayout(jPanel11Layout);
            jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 992, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1184, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap(1183, Short.MAX_VALUE))
            );
            jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Gyártás infó", jPanel11);

            jTable16.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String [] {
                    "Széria szám:", "Konténer szám:", "Létrehozás dátuma:"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    true, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable16.setCellSelectionEnabled(true);
            jScrollPane18.setViewportView(jTable16);

            jButton21.setText("Lekérdez");
            jButton21.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton21ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
            jPanel13.setLayout(jPanel13Layout);
            jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jButton21)
                    .addContainerGap(2826, Short.MAX_VALUE))
            );
            jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addComponent(jButton21)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
            jPanel12.setLayout(jPanel12Layout);
            jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("SN konténer infó", jPanel12);

            jButton24.setText("Lekérdez");
            jButton24.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton24ActionPerformed(evt);
                }
            });

            jLabel12.setText("Alkatrész PN");

            jTable20.setAutoCreateRowSorter(true);
            jTable20.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Program", "Partnumber", "Gép", "Oldal", "Modul", "Side", "Slot", "Qty"
                }
            ));
            jScrollPane20.setViewportView(jTable20);
            if (jTable20.getColumnModel().getColumnCount() > 0) {
                jTable20.getColumnModel().getColumn(2).setMinWidth(80);
                jTable20.getColumnModel().getColumn(2).setPreferredWidth(80);
                jTable20.getColumnModel().getColumn(2).setMaxWidth(80);
                jTable20.getColumnModel().getColumn(3).setMinWidth(60);
                jTable20.getColumnModel().getColumn(3).setPreferredWidth(60);
                jTable20.getColumnModel().getColumn(3).setMaxWidth(60);
                jTable20.getColumnModel().getColumn(4).setMinWidth(50);
                jTable20.getColumnModel().getColumn(4).setPreferredWidth(50);
                jTable20.getColumnModel().getColumn(4).setMaxWidth(50);
                jTable20.getColumnModel().getColumn(5).setMinWidth(50);
                jTable20.getColumnModel().getColumn(5).setPreferredWidth(50);
                jTable20.getColumnModel().getColumn(5).setMaxWidth(50);
                jTable20.getColumnModel().getColumn(6).setMinWidth(50);
                jTable20.getColumnModel().getColumn(6).setPreferredWidth(50);
                jTable20.getColumnModel().getColumn(6).setMaxWidth(50);
                jTable20.getColumnModel().getColumn(7).setMinWidth(50);
                jTable20.getColumnModel().getColumn(7).setPreferredWidth(50);
                jTable20.getColumnModel().getColumn(7).setMaxWidth(50);
            }

            jTable21.setAutoCreateRowSorter(true);
            jTable21.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Sor", "Program", "Start", "Szalag db", "Komm"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable21.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable21MouseClicked(evt);
                }
            });
            jScrollPane21.setViewportView(jTable21);
            if (jTable21.getColumnModel().getColumnCount() > 0) {
                jTable21.getColumnModel().getColumn(0).setMinWidth(30);
                jTable21.getColumnModel().getColumn(0).setPreferredWidth(30);
                jTable21.getColumnModel().getColumn(0).setMaxWidth(100);
                jTable21.getColumnModel().getColumn(1).setMinWidth(100);
                jTable21.getColumnModel().getColumn(1).setPreferredWidth(100);
                jTable21.getColumnModel().getColumn(1).setMaxWidth(500);
                jTable21.getColumnModel().getColumn(2).setMinWidth(80);
                jTable21.getColumnModel().getColumn(2).setPreferredWidth(80);
                jTable21.getColumnModel().getColumn(2).setMaxWidth(300);
                jTable21.getColumnModel().getColumn(3).setMinWidth(70);
                jTable21.getColumnModel().getColumn(3).setPreferredWidth(70);
                jTable21.getColumnModel().getColumn(3).setMaxWidth(300);
                jTable21.getColumnModel().getColumn(4).setMinWidth(50);
                jTable21.getColumnModel().getColumn(4).setPreferredWidth(50);
                jTable21.getColumnModel().getColumn(4).setMaxWidth(50);
            }

            jLabel38.setText("Shape:");

            jLabel39.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N

            javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
            jPanel15.setLayout(jPanel15Layout);
            jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton24)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel15Layout.createSequentialGroup()
                                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(1, 1, 1))))
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(14, 14, 14))
            );
            jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel38)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jButton24)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane21))
            );

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Alk. info , szalag gy.", jPanel14);

            jButton25.setText("Lekérdez");
            jButton25.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton25ActionPerformed(evt);
                }
            });

            jTable22.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Refdes", "Component"
                }
            ));
            jScrollPane24.setViewportView(jTable22);
            if (jTable22.getColumnModel().getColumnCount() > 0) {
                jTable22.getColumnModel().getColumn(1).setPreferredWidth(30);
            }

            jLabel40.setText("Mi van az SN-ben?");

            jLabel41.setText("Testers output");

            jLabel42.setText("Tól:");

            jLabel43.setText("Ig:");

            jTable23.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "System", "QTY", "PartNumber", "Pass", "Fail", "FPY"
                }
            ));
            jScrollPane25.setViewportView(jTable23);
            if (jTable23.getColumnModel().getColumnCount() > 0) {
                jTable23.getColumnModel().getColumn(1).setPreferredWidth(30);
                jTable23.getColumnModel().getColumn(2).setPreferredWidth(100);
                jTable23.getColumnModel().getColumn(3).setPreferredWidth(30);
                jTable23.getColumnModel().getColumn(4).setPreferredWidth(30);
                jTable23.getColumnModel().getColumn(5).setPreferredWidth(30);
            }

            jButton26.setText("Lekérdez");
            jButton26.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton26ActionPerformed(evt);
                }
            });

            jLabel44.setText("Óra:");

            jLabel45.setText("Óra:");

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UBT", "MPT" }));
            jComboBox1.setSelectedIndex(-1);

            javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
            jPanel17.setLayout(jPanel17Layout);
            jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                            .addGap(97, 97, 97)
                            .addComponent(jButton25)
                            .addGap(77, 77, 77)))
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane25)
                            .addGap(349, 349, 349))
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGap(217, 217, 217)
                            .addComponent(jButton26)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel45)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGap(70, 70, 70)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGap(86, 86, 86)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(74, 74, 74)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2346, Short.MAX_VALUE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(378, 378, 378))
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(56, 56, 56)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                    .addComponent(jLabel44)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            );
            jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel40)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton25)
                        .addComponent(jButton26)
                        .addComponent(jLabel44)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel45)
                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)))
            );

            javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
            jPanel16.setLayout(jPanel16Layout);
            jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Tiger lekérdezések", jPanel16);

            jLabel46.setText("Tól:");

            jLabel47.setText("Ig:");

            jLabel48.setText("Óra:");

            jLabel49.setText("Óra:");

            jButton27.setText("Lekér");
            jButton27.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton27ActionPerformed(evt);
                }
            });

            jTable24.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "PArtNumber", "QTY", "PO", "City", "Company"
                }
            ));
            jTable24.setCellSelectionEnabled(true);
            jTable24.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable24MouseClicked(evt);
                }
            });
            jScrollPane26.setViewportView(jTable24);
            if (jTable24.getColumnModel().getColumnCount() > 0) {
                jTable24.getColumnModel().getColumn(2).setResizable(false);
                jTable24.getColumnModel().getColumn(4).setResizable(false);
            }

            jScrollPane27.setViewportView(jList2);

            jLabel50.setText("Termékcsalád(ok)");

            jLabel51.setText("Kereső:");

            jTextField24.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField24ActionPerformed(evt);
                }
            });

            jTextField25.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField25ActionPerformed(evt);
                }
            });

            jTextField26.setPreferredSize(new java.awt.Dimension(8, 20));
            jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTextField26KeyReleased(evt);
                }
            });

            jLabel52.setText("Kiszállítások összegzése:");

            jLabel53.setText("Kijelölések összege:");

            jButton28.setText("Exp. to Exc.");
            jButton28.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton28ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
            jPanel19.setLayout(jPanel19Layout);
            jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(45, 45, 45)
                    .addComponent(jLabel47)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton27)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton28)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addGap(28, 28, 28)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addComponent(jLabel51)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2371, Short.MAX_VALUE)
                            .addComponent(jLabel53)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel52)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41))
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addComponent(jScrollPane26)
                            .addContainerGap())))
            );
            jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jDateChooser9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton27)
                            .addComponent(jButton28)))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(21, 21, 21))
            );

            javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
            jPanel18.setLayout(jPanel18Layout);
            jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Kiszállítások", jPanel18);

            javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
            jPanel21.setLayout(jPanel21Layout);
            jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 3428, Short.MAX_VALUE)
            );
            jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 672, Short.MAX_VALUE)
            );

            javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
            jPanel20.setLayout(jPanel20Layout);
            jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("CTB calc.", jPanel20);

            jTable25.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Prioritás", "Title1", "Title 2", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "null", "Title 15"
                }
            ));
            jTable25.setCellSelectionEnabled(true);
            jTable25.setOpaque(false);
            jScrollPane28.setViewportView(jTable25);
            jTable25.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            if (jTable25.getColumnModel().getColumnCount() > 0) {
                jTable25.getColumnModel().getColumn(0).setMinWidth(50);
                jTable25.getColumnModel().getColumn(0).setPreferredWidth(100);
                jTable25.getColumnModel().getColumn(0).setMaxWidth(200);
            }

            jLabel54.setText("Hét száma:");

            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53" }));

            jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/savement.png"))); // NOI18N
            jButton29.setToolTipText("Mentés");
            jButton29.setAlignmentY(0.0F);
            jButton29.setEnabled(false);
            jButton29.setIconTextGap(0);
            jButton29.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jButton29MouseClicked(evt);
                }
            });
            jButton29.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton29ActionPerformed(evt);
                }
            });

            jLabel55.setText("Év:");

            javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
            jPanel22.setLayout(jPanel22Layout);
            jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 1094, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel55)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel54)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton29)
                        .addComponent(jLabel55)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jPanel22Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton29, jComboBox2, jComboBox3, jLabel54, jLabel55});

            jTabbedPane1.addTab("Prioritások", jPanel22);

            jButton30.setText("Lekérdez");
            jButton30.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton30ActionPerformed(evt);
                }
            });

            jTable26.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Partnumbers"
                }
            ));
            jScrollPane29.setViewportView(jTable26);

            jTable27.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Serial N", "Shop Order", "Part Number", "Workstation", "Qty", "Unit Status", "location DateTime", "Last Complete Date", "Complete Date"
                }
            ));
            jScrollPane30.setViewportView(jTable27);

            jCheckBox6.setSelected(true);
            jCheckBox6.setText("Group by PN");

            jTable28.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Partnumbers", "JOB", "QTY"
                }
            ));
            jScrollPane31.setViewportView(jTable28);

            javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
            jPanel24.setLayout(jPanel24Layout);
            jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createSequentialGroup()
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel24Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCheckBox6)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton30)
                            .addComponent(jCheckBox6))
                        .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane29)
                        .addComponent(jScrollPane30)
                        .addComponent(jScrollPane31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)))
            );

            javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
            jPanel23.setLayout(jPanel23Layout);
            jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Complete QTY", jPanel23);

            jScrollPane7.setViewportView(jTabbedPane1);

            jPanel6.add(jScrollPane7, java.awt.BorderLayout.CENTER);

            getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

            pack();
            setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

        int i = jTabbedPane1.getSelectedIndex();

        if (i == 5) {

            //oh query
            jButton12.setEnabled(false);
            jButton11.setEnabled(false);
            jButton13.setEnabled(false);
            jButton15.setEnabled(false);
            bejelentkezes a = new bejelentkezes(this);
            a.setVisible(true);
            String query = "SELECT ig FROM planningdb.oh_querymain order by ig desc limit 1";
            String prefquery = "SELECT distinct prefix FROM planningdb.oh_prefixes;";
            String kivetelquery = "SELECT distinct partnumber FROM planningdb.oh_kivetelek;";
            datum = new java.util.Date();
            jDateChooser4.setDate(datum);
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable7.getModel();
            model.setRowCount(0);
            jTable7.setModel(model);
            model = (DefaultTableModel) jTable9.getModel();
            model.setRowCount(0);
            jTable9.setModel(model);

            planconnect pc = new planconnect();

            try {

                ResultSet rs = (ResultSet) pc.lekerdez(query);

                if (rs.next()) {
                    datum = rs.getDate(1);
                    jDateChooser2.setDate(datum);
                }

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                i = 0;

                ResultSet rs = (ResultSet) pc.lekerdez(prefquery);
                while (rs.next()) {

                    jTable8.setValueAt(rs.getString(1), i, 0);
                    i++;

                }

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                ResultSet rs = (ResultSet) pc.lekerdez(kivetelquery);
                i = 0;
                while (rs.next()) {

                    jTable10.setValueAt(rs.getString(1), i, 0);
                    i++;

                }

                pc.kinyir();

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (i == 1) {

            wgcounter = 0;
            elso = LocalDateTime.now();

        }
//backend terv
        if (i == 7) {

            Tc_Bejelentkezes.nyit = true;
            Tc_Bejelentkezes a = new Tc_Bejelentkezes(this);
            a.setVisible(true);

        }

        if (i == 8) {

//job figyelő
            DefaultListModel lm = new DefaultListModel();
//smt állomások
            String Query = "select distinct stations.name from stations left join terv on terv.stationid = stations.id where terv.active = 1 and terv.startdate >= '2018-01-01 06:00:00' order by stations.name asc";
            planconnect pc = new planconnect();
            try {
                pc.lekerdez(Query);

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                while (pc.rs.next()) {

                    lm.addElement(pc.rs.getString(1));

                }
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }
//beterv állomások
            Query = "SELECT * FROM planningdb.tc_becells order by cellname asc";

            try {
                pc.lekerdez(Query);
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                while (pc.rs.next()) {

                    lm.addElement(pc.rs.getString(2));

                }
            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }
            pc.kinyir();
            jList1.setModel(lm);

        }

//kiszállítások
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Kiszállítások")) {

            postgreconnect pc = new postgreconnect();
            try {
                pc.lekerdez("select distinct \"HBPackage\".customer_type.name from \"HBPackage\".customer_type");
                DefaultListModel listModel = new DefaultListModel();

                listModel.removeAllElements();

                while (pc.rs.next()) {

                    listModel.addElement(pc.rs.getString(1));

                }

                jList2.setModel(listModel);
                pc.kinyir();

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //ctb calc
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("CTB calc.")) {

            CTB_Bejel b = new CTB_Bejel();
            b.setVisible(true);

        }
        //ha a prioritások tabot választjuk     

        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Prioritások")) {

            Prioritasok p = new Prioritasok();
            try {
                p.legordulokbeallit();
                p.setdatetotableheader();
                p.feltolttabla();

                int weeknumber = LocalDateTime.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                //System.out.print(weeknumber);

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            prioment();

        } else {

            //planner = false;
        }

//ez a legvége a change tabbedpainnek
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed

        //kiszedjuk a kijelolt elemeket
        Jobfigyeloszal j = new Jobfigyeloszal();
        j.start();
        animation a = new animation();
        a.start();

    }//GEN-LAST:event_jButton18ActionPerformed

    //prioritas mentes gomb kezelese
    static public void prioment() {

        if (planner) {

            jButton29.setEnabled(true);

        } else {

            jButton29.setEnabled(false);
        }

    }


    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:

        try {

            int valai = Integer.parseInt(jTextField19.getText());
            String value;
            String query = "SELECT cycletime_prog.ID as cpID, oraclepn ,smtprogname, smtline as sline, sequence , boardnumber ,(SELECT value FROM cycletime_data WHERE cycletime_prog_id = cpID AND priority = 1 AND cycletime_data.active = 1 ORDER BY ID DESC LIMIT 1) as mertido,(SELECT value FROM cycletime_data WHERE cycletime_prog_id = cpID AND priority = 2 AND cycletime_data.active = 1 ORDER BY ID DESC LIMIT 1) as gyorsmeres,(SELECT value FROM cycletime_data WHERE cycletime_prog_id = cpID AND priority = 3 AND cycletime_data.active = 1 ORDER BY ID DESC LIMIT 1) as kalkulalt,IFNULL(expectedeffbyprog,COALESCE((SELECT expectedeff FROM cycletime_config WHERE smtline = sline),(SELECT expectedeff FROM cycletime_config WHERE smtline = 'ALL'))) as eff FROM `cycletime_prog` WHERE cycletime_prog.active=1 and oraclepn like '%" + jTextField18.getText().trim() + "%'ORDER BY smtprogname;";
            Integer i = 0;
            Object valami = null;
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable12.getModel();
            model.setRowCount(0);
            try {
                szaifconn con = new szaifconn("com.mysql.jdbc.driver", "jdbc:mysql://143.116.140.113/plrdb", "cpi", "cpi602");

                ResultSet rs = (ResultSet) con.lekerdez(query);

                while (rs.next()) {

                    model.addRow(new Object[]{rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)});

                    if (rs.getString(7) != null) {

                        jTable12.setValueAt(((((Double.parseDouble(rs.getString(7)) / Double.parseDouble(rs.getString(6)))) / ((Double.parseDouble(rs.getString(10))) / 100)) * Integer.parseInt(jTextField19.getText())) / 3600, i, 9);

                    } else if (rs.getString(8) != null) {

                        jTable12.setValueAt(((((Double.parseDouble(rs.getString(8)) / Double.parseDouble(rs.getString(6)))) / ((Double.parseDouble(rs.getString(10))) / 100)) * Integer.parseInt(jTextField19.getText())) / 3600, i, 9);

                    } else if (rs.getString(9) != null) {

                        jTable12.setValueAt(((((Double.parseDouble(rs.getString(9)) / Double.parseDouble(rs.getString(6)))) / ((Double.parseDouble(rs.getString(10))) / 100)) * Integer.parseInt(jTextField19.getText())) / 3600, i, 9);

                    }

                    i++;
                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
            }

            jTable12.setModel(model);

            model1 = (DefaultTableModel) jTable13.getModel();
            model1.setRowCount(0);

            planconnect pc = new planconnect();
            query = "SELECT distinct beciklusidok.WS , beciklusidok.PN , beciklusidok.CELL, beciklusidok.DBPO FROM planningdb.beciklusidok where active = 1 and PN like ('%" + jTextField18.getText().trim() + "%') order by beciklusidok.ID";

            try {
                ResultSet rs = (ResultSet) pc.lekerdez(query);
                while (rs.next()) {

                    model1.addRow(new Object[]{rs.getString(2), rs.getString(1), rs.getString(2), rs.getString(4), (Integer.parseInt(jTextField19.getText())) / Double.parseDouble(rs.getString(4))});

                }

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

            pc.kinyir();

            jTable13.setModel(model1);

        } catch (Exception e) {
            infobox info = new infobox();
            info.infoBox("Nem adtál meg darabszámot!", "Hiba!");
        }

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:++
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable7, new File(fileToSave.getAbsolutePath() + ".xls"));
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        String query = "TRUNCATE TABLE oh_kivetelek";

        planconnect pc = new planconnect();
        pc.feltolt(query, true);
        query = "INSERT INTO oh_kivetelek(partnumber) VALUES ()";

        String prefixlist = "";

        for (int i = 0; i < jTable10.getRowCount(); i++) {

            if (jTable10.getValueAt(i, 0) != null) {

                prefixlist += jTable10.getValueAt(i, 0).toString().trim() + "'),('";

            }

        }

        prefixlist = prefixlist.substring(0, prefixlist.length() - 4);
        query = "INSERT INTO oh_kivetelek(partnumber) VALUES ('" + prefixlist + ")";

        pc.feltolt(query, true);

        infobox inf = new infobox();
        inf.infoBox("A mentés sikeres!", "Üzenet!");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {

            for (int i = 0; i < jTable7.getRowCount(); i++) {
                jTable7.getModel().setValueAt(true, i, 4);
            }

        } else {

            for (int i = 0; i < jTable7.getRowCount(); i++) {
                jTable7.getModel().setValueAt(false, i, 4);
            }
        }

    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
        // TODO add your handling code here:

        String pn = jTable9.getValueAt(jTable9.getSelectedRow(), 0).toString();

        for (int i = 0; i < jTable7.getRowCount(); i++) {

            if (jTable7.getValueAt(i, 0).toString().equals(pn) && (Boolean) jTable9.getValueAt(jTable9.getSelectedRow(), 2) == true) {

                jTable7.setValueAt(true, i, 4);

            }

            if (jTable7.getValueAt(i, 0).toString().equals(pn) && (Boolean) jTable9.getValueAt(jTable9.getSelectedRow(), 2) == false) {

                jTable7.setValueAt(false, i, 4);

            }

        }

    }//GEN-LAST:event_jTable9MouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:

        DefaultTableModel oh_main = new DefaultTableModel();
        oh_main = (DefaultTableModel) jTable7.getModel();
        oh_main.setRowCount(0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tol = dateFormat.format(jDateChooser2.getDate());
        String ig = dateFormat.format(jDateChooser4.getDate());

        String query = "select * from oh_querymain where oh_querymain.lekerdezesidopont between '" + tol + " 00:00:00' and '" + ig + " 23:59:59'";

        planconnect pc = new planconnect();

        try {
            ResultSet rs = (ResultSet) pc.lekerdez(query);

            int sor = 0;

            while (rs.next()) {

                oh_main.addRow(new Object[]{rs.getString(3), rs.getString(2), rs.getString(4), rs.getString(5)/*, rs.getString(6)*/});

                if (rs.getString(6).equals("Y")) {

                    oh_main.setValueAt(true, sor, 4);

                } else {
                    oh_main.setValueAt(false, sor, 4);
                }

                sor++;

            }

            jTable7.setModel(oh_main);

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }

        pc.kinyir();

        //kitoltjuk a summa táblát
        String pn = "";
        int pndarab = 0;
        int k = 0;
        boolean vanemar = false;
        DefaultTableModel sumtabla = new DefaultTableModel();
        sumtabla = (DefaultTableModel) jTable9.getModel();
        sumtabla.setRowCount(0);

        for (int i = 0; i < jTable7.getRowCount(); i++) {

            pn = jTable7.getValueAt(i, 0).toString();
            vanemar = false;
            pndarab = 0;

            for (int m = 0; m < sumtabla.getRowCount(); m++) {
                if (pn.equals(sumtabla.getValueAt(m, 0).toString())) {
                    vanemar = true;

                }
            }

            if (vanemar == false) {
                for (int n = i; n < jTable7.getRowCount(); n++) {

                    if (pn.equals(jTable7.getValueAt(n, 0).toString())) {
                        pndarab++;
                    }

                }

                try {

                    sumtabla.addRow(new Object[]{pn, pndarab, false});

                } catch (Exception e) {
                    System.out.println("nem jott ossze");
                }

            }

        }

        jTable9.setModel(sumtabla);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:

        String query = "TRUNCATE TABLE oh_prefixes";

        planconnect pc = new planconnect();
        pc.feltolt(query, true);
        query = "INSERT INTO oh_prefixes(prefix) VALUES ()";

        String prefixlist = "";

        for (int i = 0; i < jTable8.getRowCount(); i++) {

            if (jTable8.getValueAt(i, 0) != null) {

                prefixlist += jTable8.getValueAt(i, 0).toString() + "'),('";

            }

        }

        prefixlist = prefixlist.substring(0, prefixlist.length() - 4);
        query = "INSERT INTO oh_prefixes(prefix) VALUES ('" + prefixlist + ")";

        pc.feltolt(query, true);

        infobox inf = new infobox();
        inf.infoBox("A mentés sikeres!", "Üzenet!");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:

        animation a = new animation();
        a.start();
        OhQueryszal sz = new OhQueryszal();
        sz.start();

//       
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:

        String adatok = "";
        String mentve = "";
        String user = System.getProperty("user.name");
        for (int i = 0; i < jTable7.getRowCount(); i++) {

            //System.out.println(jTable7.getValueAt(i, 4));
            try {
                if (jTable7.getValueAt(i, 4).equals(true)) {

                    mentve = "Y";

                } else {
                    mentve = "N";
                }
            } catch (Exception e) {

                mentve = "N";
            }

            adatok += "('" + jTable7.getValueAt(i, 1) + "','" + jTable7.getValueAt(i, 0) + "','" + jTable7.getValueAt(i, 2) + "','" + jTable7.getValueAt(i, 3) + "','" + mentve + "','" + user + "'),";

        }

        adatok = adatok.substring(0, adatok.length() - 1);

        String query = "insert into oh_querymain (serial,partnumber,tol,ig,megcsinalva,felhasznalo) values" + adatok + "on duplicate key update megcsinalva = values (megcsinalva) , felhasznalo = values (felhasznalo)";

        planconnect pc = new planconnect();

        try {
            pc.feltolt(query, true);

            infobox inf = new infobox();
            inf.infoBox("A feltöltés sikeres!", "Feltöltés!");
        } catch (Exception e) {

            infobox inf = new infobox();
            inf.infoBox("A feltöltés sikertelen!", "Feltöltés!");

        }

        String levelbe = " \n Figyelem! \n Feltöltés történt az OH query adatbázisba a következő tételekkel: \n";
        String yn = "";
        for (int i = 0; i < jTable7.getRowCount(); i++) {

            if ((Boolean) jTable7.getValueAt(i, 4) == null || (Boolean) jTable7.getValueAt(i, 4) == false) {

                yn = "Nincs megcsinálva!";
            } else {
                yn = "Elkészült az OH!";
            }

            levelbe += jTable7.getValueAt(i, 0).toString() + "  " + jTable7.getValueAt(i, 1).toString() + "  " + jTable7.getValueAt(i, 2) + "  " + jTable7.getValueAt(i, 3) + "  " + yn + "\n";

        }

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), levelbe, "gabor.hanacsek@sanmina.com,roland.bognar@sanmina.com,gina.gerecz@sanmina.com,eva.inczedi@sanmina.com");

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:

        String pn = jTextField10.getText().trim();
        String pcb = jTextField11.getText().trim();

        if ((pn.equals("") && pcb.equals("")) || (pn.length() > 0 && pcb.length() > 0)) {

            infobox.infoBox("Vagy Partnumbert vagy PCB számot adj meg!", "Hiba!");

        } else {

            pcbtabla = (DefaultTableModel) jTable6.getModel();

            if (pn.length() > 0) {

                String query = "SELECT  harman_tipusok.PCB as Board_PN  FROM planningdb.harman_tipusok where harman_tipusok.PN_SMD_Mainboard_PCBA like '" + jTextField10.getText().trim() + "'";
                planconnect pc = new planconnect();

                ResultSet rs;

                try {
                    rs = (ResultSet) pc.lekerdez(query);

                    pcbtabla.setValueAt(pn, 0, 0);
                    pcbtabla = (DefaultTableModel) pc.resultSetToTableModel(rs, pcbtabla, 1);
                    // jTable6.setModel(pcbtabla);

                } catch (SQLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

                pc.kinyir();
                query = "SELECT ifnull(sum(oracle_backup_subinv.quantity),0) as qty from oracle_backup_subinv where oracle_backup_subinv.item ='" + pcbtabla.getValueAt(0, 1).toString().trim() + "'";

                connect con = new connect(query);

                try {
                    if (con.rs.next()) {
                        pcbtabla.setValueAt(con.rs.getString("qty"), 0, 2);
                    }
                    jTable6.setModel(pcbtabla);
                } catch (SQLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (pcb.length() > 0) {

                String query = "SELECT  harman_tipusok.PN_SMD_Mainboard_PCBA as Part  FROM planningdb.harman_tipusok where harman_tipusok.PCB like '" + jTextField11.getText().trim() + "'";
                planconnect con = new planconnect();
                try {
                    ResultSet rs = (ResultSet) con.lekerdez(query);
                    con.kinyir();
                    pcbtabla.setValueAt(jTextField11.getText(), 0, 1);
                    if (rs.next()) {
                        pcbtabla.setValueAt(rs.getString("Part"), 0, 0);
                    }

                    //jTable6.setModel(pcbtabla);
                } catch (SQLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

                query = "SELECT ifnull(sum(oracle_backup_subinv.quantity),0) as qty from oracle_backup_subinv where oracle_backup_subinv.item ='" + pcbtabla.getValueAt(0, 1).toString().trim() + "'";

                connect con1 = new connect(query);

                try {
                    if (con1.rs.next()) {
                        pcbtabla.setValueAt(con1.rs.getString("qty"), 0, 2);
                    }
                    jTable6.setModel(pcbtabla);
                } catch (SQLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        stat.beir(System.getProperty("user.name"), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyPressed
        // TODO add your handling code here:
        if (jTextField11.getText().length() > 0) {
            jTextField10.setEnabled(false);
        } else {

            jTextField10.setEnabled(true);
        }
    }//GEN-LAST:event_jTextField11KeyPressed

    private void jTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyPressed
        // TODO add your handling code here:

        if (jTextField10.getText().length() > 0) {
            jTextField11.setEnabled(false);
        } else {

            jTextField11.setEnabled(true);
        }
    }//GEN-LAST:event_jTextField10KeyPressed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:

        animation a = new animation();
        a.start();
        Jobinfoszal j = new Jobinfoszal();
        j.start();


    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked

        Integer osszeg = 0;
        int[] selectedrows = jTable5.getSelectedRows();

        for (int i = 0; i < selectedrows.length; i++) {
            try {
                osszeg += Integer.parseInt(jTable5.getValueAt(selectedrows[i], 3).toString());
            } catch (Exception e) {
            }

        }

        jTextField12.setText(osszeg.toString());
    }//GEN-LAST:event_jTable5MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable4, new File(fileToSave.getAbsolutePath() + ".xls"));
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        Selejtszal s = new Selejtszal(this);
        s.start();
        animation a = new animation();
        a.start();


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        // TODO add your handling code here:
        modelstatus = (DefaultTableModel) jTable4.getModel();
        for (int i = 0; i < modelstatus.getRowCount(); i++) {
            for (int j = 0; j < modelstatus.getColumnCount(); j++) {

                modelstatus.setValueAt(null, i, j);

            }

        }

        jButton7.setEnabled(false);
        jTable4.setModel(modelstatus);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        animation a = new animation();
        a.start();
        snlekerszal sn = new snlekerszal();
        sn.start();


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable3, new File(fileToSave.getAbsolutePath() + ".xls"));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        String query = jTextField4.getText().trim();
        filter1(query);
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:

        //jTextField4.setText((String) jTable3.getValueAt(jTable3.getSelectedRow(), jTable3.getSelectedColumn()));
        Integer osszeg = 0;
        int[] selectedrows = jTable3.getSelectedRows();

        for (int i = 0; i < selectedrows.length; i++) {

            osszeg += Integer.parseInt(jTable3.getValueAt(selectedrows[i], 4).toString());

        }

        jTextField17.setText(osszeg.toString());
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        Activityszal a = new Activityszal();
        a.start();
        animation an = new animation();
        an.start();
        if (jTable18.isEditing()) {

            jTable18.getCellEditor().cancelCellEditing();

        }
        if (jTable19.isEditing()) {
            jTable19.getCellEditor().cancelCellEditing();
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable1, new File(fileToSave.getAbsolutePath() + "_WIP" + ".xls"));
            exp.fillData(jTable2, new File(fileToSave.getAbsolutePath() + "_OH" + ".xls"));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        String query = jTextField1.getText().toUpperCase().trim();
        filter(query);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        // jTextField1.setText((String) jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()));
        Integer osszeg = 0;
        int[] selectedrows = jTable2.getSelectedRows();

        for (int i = 0; i < selectedrows.length; i++) {

            osszeg += Integer.parseInt(jTable2.getValueAt(selectedrows[i], 3).toString().substring(0, jTable2.getValueAt(selectedrows[i], 3).toString().length() - 3));

        }

        jTextField13.setText(osszeg.toString());
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //jTextField1.setText((String) jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()));

        Integer osszeg = 0;
        int[] selectedrows = jTable1.getSelectedRows();

        for (int i = 0; i < selectedrows.length; i++) {

            osszeg += Integer.parseInt(jTable1.getValueAt(selectedrows[i], 2).toString());

        }

        jTextField13.setText(osszeg.toString());

    }//GEN-LAST:event_jTable1MouseClicked


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //elindítjuk az animációt

        //ha jatek van irva inditjuk a jatekot
        if (jTextField2.getText().equals("Játék!")) {

            jatek j = new jatek();
            j.setVisible(true);

        } else if (jTextField2.getText().equals("Imgup!")) {
            //betalloztatjuk a kep filet

            JFileChooser chooser = CTB_Filechooser.getFileChooserRiport();
            chooser.setDialogTitle("Kép tallózása!");
            FileFilter imageFilter = new FileNameExtensionFilter(
                    "Image files", ImageIO.getReaderFileSuffixes());
            chooser.setFileFilter(imageFilter);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();
                ImgUp u = new ImgUp(file);
                u.setVisible(true);

            }

        } else {
            animation a = new animation();
            a.start();
            keszletszal k = new keszletszal(this);
            k.start();
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        // TODO add your handling code here:
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel.png")));
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        // TODO add your handling code here:
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png")));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        // TODO add your handling code here:
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel.png")));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        // TODO add your handling code here:
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png")));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        // TODO add your handling code here:
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        // TODO add your handling code here:
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/selejt.png")));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        // TODO add your handling code here:
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/selejt1.png")));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles.png")));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        // TODO add your handling code here:
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png")));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        // TODO add your handling code here:
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel.png")));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        // TODO add your handling code here:
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png")));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        // TODO add your handling code here:
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search.png")));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        // TODO add your handling code here:
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search1.png")));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseEntered
        // TODO add your handling code here:
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search.png")));
    }//GEN-LAST:event_jButton10MouseEntered

    private void jButton10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseExited
        // TODO add your handling code here:
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/search1.png")));
    }//GEN-LAST:event_jButton10MouseExited

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered
        // TODO add your handling code here:
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel.png")));
    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        // TODO add your handling code here:
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png")));
    }//GEN-LAST:event_jButton17MouseExited

    private void jButton12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseEntered
        // TODO add your handling code here:
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton12MouseEntered

    private void jButton12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseExited
        // TODO add your handling code here:
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton12MouseExited

    private void jButton11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseEntered
        // TODO add your handling code here:
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/upload.png")));
    }//GEN-LAST:event_jButton11MouseEntered

    private void jButton11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseExited
        // TODO add your handling code here:
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/upload1.png")));
    }//GEN-LAST:event_jButton11MouseExited

    private void jButton16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MouseEntered
        // TODO add your handling code here:
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton16MouseEntered

    private void jButton16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MouseExited
        // TODO add your handling code here:
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton16MouseExited

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered
        // TODO add your handling code here:
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton18MouseEntered

    private void jButton18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseExited
        // TODO add your handling code here:
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton18MouseExited

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered
        // TODO add your handling code here:
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker.png")));
    }//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        // TODO add your handling code here:
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/leker1.png")));
    }//GEN-LAST:event_jButton20MouseExited

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        animation a = new animation();
        a.start();
        Gyartasinfoszal gy = new Gyartasinfoszal();
        gy.start();

    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseEntered
        // TODO add your handling code here:
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles.png")));
    }//GEN-LAST:event_jButton19MouseEntered

    private void jButton19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseExited
        // TODO add your handling code here:
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png")));
    }//GEN-LAST:event_jButton19MouseExited

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable15.getModel();

        for (int o = 0; o < jTable15.getColumnCount(); o++) {

            for (int r = 0; r < jTable15.getRowCount(); r++) {

                model.setValueAt("", r, o);

            }

        }

    }//GEN-LAST:event_jButton19ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
//seria számra lebontása a wip nek

        snrebontos sn = new snrebontos();
        sn.start();
        animation a = new animation();
        a.start();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTable18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable18MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable18MouseClicked

    private void jTable19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable19MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable19MouseClicked

    private void jTable18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable18KeyReleased
        //visszatoltjuk az eredeti adatokat a tablaba majd lefuttatjuk a szurot

        activityszuro sz = new activityszuro();
        sz.visszaallit();
        sz.listabolszurok();
        sz.activitygroup();
        sz.firstpass();


    }//GEN-LAST:event_jTable18KeyReleased

    private void jTable19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable19KeyReleased
        // TODO add your handling code here:
        //visszatoltjuk az eredeti adatokat a tablaba majd lefuttatjuk a szurot

        activityszuro sz = new activityszuro();
        sz.visszaallit();
        sz.listabolszurok();
        sz.activitygroup();
        sz.firstpass();
    }//GEN-LAST:event_jTable19KeyReleased

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // group by pn activity

        activityszuro sz = new activityszuro();
        sz.visszaallit();
        sz.listabolszurok();
        sz.activitygroup();
        sz.firstpass();


    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
        activityszuro sz = new activityszuro();
        sz.visszaallit();
        sz.listabolszurok();
        sz.activitygroup();
        sz.firstpass();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // wipszuro
        wipszuro w = new wipszuro(ablak.jTable1);
        w.smtkiszed();

    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // konténer infó lekérdezés
        //megnezzuk , hogy van e valami az sn oszlopban

        if (jTable16.getValueAt(0, 0) != null && !jTable16.getValueAt(0, 0).toString().equals("")) {
            String snlist = "";
            //összeszedjük az sn eket a querynek megfelelő formátummá
            for (int i = 0; i < jTable16.getRowCount(); i++) {

                if (jTable16.getValueAt(i, 0) != null && !jTable16.getValueAt(0, 0).toString().equals("")) {

                    snlist += "'" + jTable16.getValueAt(i, 0).toString().trim() + "',";

                }

            }

            if (snlist.length() > 0) {

                snlist = snlist.substring(0, snlist.length() - 1);
                snlist = snlist.replace("-SMT", "");
                String query = "select barcode.container_ID from barcode where barcode in (" + snlist + ")";
                szaifconn con = null;
                try {

                    //kitöröljük az adatokat a táblából
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) jTable16.getModel();
                    for (int i = 0; i < model.getRowCount(); i++) {

                        for (int r = 0; r < model.getColumnCount(); r++) {

                            model.setValueAt("", i, r);

                        }

                    }

                    con = new szaifconn("com.mysql.jdbc.driver", "jdbc:mysql://143.116.140.113/plrdb", "cpi", "cpi602");
                    ResultSet rs = (ResultSet) con.lekerdez(query);
                    String contlist = "";
                    while (rs.next()) {

                        contlist += "'" + rs.getString(1) + "',";

                    }

                    if (contlist.length() > 0) {

                        contlist = contlist.substring(0, contlist.length() - 1);
                        query = "SELECT barcode.barcode , container.container , container.insertDate \n"
                                + "FROM plrdb.container\n"
                                + "left join plrdb.barcode on container.ID = barcode.container_ID \n"
                                + "where container.id in (" + contlist + ")";

                        rs = (ResultSet) con.lekerdez(query);
                        int sorszam = 0;
                        while (rs.next()) {

                            model.setValueAt(rs.getString(1), sorszam, 0);
                            model.setValueAt(rs.getString(2), sorszam, 1);
                            model.setValueAt(rs.getString(3), sorszam, 2);
                            sorszam++;
                        }

                        jTable16.setModel(model);

                    }

                    con.kinyir();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");


    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseEntered

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel.png")));

    }//GEN-LAST:event_jButton22MouseEntered

    private void jButton22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseExited
        // TODO add your handling code here:
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/excel1.png")));
    }//GEN-LAST:event_jButton22MouseExited

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // jobinfo export to excel

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable5, new File(fileToSave.getAbsolutePath() + ".xls"));

        }

    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseEntered
        // TODO add your handling code here:
        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles.png")));
    }//GEN-LAST:event_jButton23MouseEntered

    private void jButton23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseExited
        // TODO add your handling code here:
        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PlannTool/PICTURES/torles1.png")));
    }//GEN-LAST:event_jButton23MouseExited

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        //jobinfo tábák adatainak törlése

        for (int i = 0; i < ablak.jTable17.getRowCount(); i++) {

            ablak.jTable17.setValueAt("", i, 0);

        }

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) ablak.jTable5.getModel();
        model.setRowCount(0);
        ablak.jTable5.setModel(model);


    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");

// nexim konnect
        if (jTextField7.getText().length() > 0) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) jTable20.getModel();
            model.setRowCount(0);
            neximconnect nc = new neximconnect();
            try {

                String query = "SELECT DISTINCT JOBNAME, PARTNUMBERNAME, MODEL.NAME AS \"Gép\" , case SETUP.TOPBOTTOM when 0 then 'Top' else 'Bottom' end as oldal ,   FLOOR(FEEDERSETUP.FEEDERID/10000) as MODULE,\n"
                        + "FLOOR(MOD(FEEDERSETUP.FEEDERID,10000)/1000) as SIDE, \n"
                        + "MOD(FEEDERSETUP.FEEDERID,1000) AS SLOT,\n"
                        + "COUNT(FEEDERSETUPREFLIST.FEEDERID) as qty ,  PARTSHAPENAME , FEEDERNAME \n"
                        + "FROM JOBDATA \n"
                        + "LEFT JOIN PRODUCT ON PRODUCT.JOBID = JOBDATA.JOBID\n"
                        + " LEFT JOIN LINE ON LINE.JOBID = PRODUCT.JOBID\n"
                        + "       AND LINE.PRODUCTID = PRODUCT.PRODUCTID\n"
                        + " LEFT JOIN SETUP ON SETUP.JOBID = LINE.JOBID \n"
                        + "       AND SETUP.LINEID = LINE.LINEID\n"
                        + "       AND SETUP.PRODUCTID = LINE.PRODUCTID\n"
                        + "       AND SETUP.TOPBOTTOM = LINE.TOPBOTTOM\n"
                        + " LEFT JOIN FEEDERSETUP ON FEEDERSETUP.SETUPID = SETUP.SETUPID \n"
                        + " LEFT JOIN MODEL ON MODEL.MODELID = FEEDERSETUP.MODELID -- Gépnévhez\n"
                        + "       AND MODEL.JOBID = SETUP.JOBID\n"
                        + "       AND MODEL.PRODUCTID = SETUP.PRODUCTID\n"
                        + "       AND MODEL.TOPBOTTOM = SETUP.TOPBOTTOM\n"
                        + " LEFT JOIN COORDINATE ON COORDINATE.JOBID = JOBDATA.JOBID\n"
                        + "       AND COORDINATE.TOPBOTTOM = LINE.TOPBOTTOM\n"
                        + " LEFT JOIN SEQUENCEBOM ON SEQUENCEBOM.COORDINATEID = COORDINATE.COORDINATEID\n"
                        + "       AND SEQUENCEBOM.JOBID = COORDINATE.JOBID\n"
                        + "       AND SEQUENCEBOM.TOPBOTTOM = COORDINATE.TOPBOTTOM\n"
                        + " LEFT JOIN PARTNUMBER ON PARTNUMBER.PARTNUMBERNAME = SEQUENCEBOM.PARTNUMBER -- Shape-hez kell\n"
                        + "       AND PARTNUMBER.LINEID = LINE.LINEID\n"
                        + " LEFT JOIN VENDERLOTPARTNUMBER ON VENDERLOTPARTNUMBER.PARTNUMBERID = PARTNUMBER.PARTNUMBERID -- Shape-hez kell\n"
                        + " LEFT JOIN FEEDERSETUPREFLIST ON FEEDERSETUPREFLIST.SETUPID = FEEDERSETUP.SETUPID\n"
                        + "       AND FEEDERSETUPREFLIST.MODELID = FEEDERSETUP.MODELID \n"
                        + "       AND FEEDERSETUPREFLIST.FEEDERID = FEEDERSETUP.FEEDERID\n"
                        + "       AND FEEDERSETUPREFLIST.UNITPOSID = FEEDERSETUP.UNITPOSID\n"
                        + "       AND FEEDERSETUPREFLIST.SUBSLOT = FEEDERSETUP.SUBSLOT\n"
                        + "       AND FEEDERSETUPREFLIST.COORDINATEID = COORDINATE.COORDINATEID\n"
                        + "   WHERE VENDERLOTPARTNUMBER.PARTSHAPENAME = (SELECT PARTSHAPENAME FROM VENDERLOTPARTNUMBER LEFT JOIN PARTNUMBER ON PARTNUMBER.PARTNUMBERID = VENDERLOTPARTNUMBER.PARTNUMBERID WHERE PARTNUMBERNAME =  '" + jTextField7.getText().trim() + "' GROUP BY PARTSHAPENAME,PARTNUMBERNAME)      \n"
                        + "     AND FEEDERSETUPREFLIST.FEEDERID IS NOT NULL	-- HA FAMILY SETUP IS KELL, AKKOR EZ A SORT COMMENTELD KI, EZZEL CSAK AZT KAPOD AMIT HASZNAL IS\n"
                        + "     AND FEEDERSETUP.PARTNUMBER IS NOT NULL \n"
                        + "     AND JOBDATA.TRASHFLAG = 0\n"
                        + "     AND SETUP.MASTERSETUP = 1\n"
                        + "GROUP BY JOBNAME,  SETUP.TOPBOTTOM, FEEDERSETUP.PARTNUMBER, FEEDERNAME, FEEDERSETUP.FEEDERID, MODEL.NAME, PARTSHAPENAME, VENDERLOTPARTNUMBER.PMAPP, SETUP.TOPBOTTOM, PARTNUMBERNAME\n"
                        + "ORDER BY jobname ";

                nc.planconnect(query);
                int merete = 0;

                if (nc.rs != null) {
                    nc.rs.last();
                    merete = nc.rs.getRow();
                    nc.rs.beforeFirst();
                    int i = 0;
//beallitjuk az adatstring meretet amiből mindig vissza tudjuk tolteni az adatokat

                    alkatreszkereso = new String[merete][8];

                    while (nc.rs.next()) {

                        model.addRow(new Object[]{nc.rs.getString(1), nc.rs.getString(2), nc.rs.getString(3), nc.rs.getString(4), nc.rs.getString(5), nc.rs.getString(6), nc.rs.getString(7), nc.rs.getString(8)});
                        jLabel39.setText(nc.rs.getString(9));
                        alkatreszkereso[i][0] = nc.rs.getString(1);
                        alkatreszkereso[i][1] = nc.rs.getString(2);
                        alkatreszkereso[i][2] = nc.rs.getString(3);
                        alkatreszkereso[i][3] = nc.rs.getString(4);
                        alkatreszkereso[i][4] = nc.rs.getString(5);
                        alkatreszkereso[i][5] = nc.rs.getString(6);
                        alkatreszkereso[i][6] = nc.rs.getString(7);
                        alkatreszkereso[i][7] = nc.rs.getString(8);

                        i++;

                    }

                    nc.kinyir();
                    jTable20.setModel(model);

//innen jön az a rész , hogy megkeressük az smt-s terveket és kommentelünk ha kell
                    String pnek = "";
                    ArrayList<String> pns = new ArrayList<String>();
                    DefaultTableModel model1 = new DefaultTableModel();
                    model1 = (DefaultTableModel) jTable21.getModel();
                    model1.setRowCount(0);

//bejarjuk a tablat es összerakjuk a pn stringet a querybe
                    outerloop:
                    for (int n = 0; n < jTable20.getRowCount(); n++) {

                        for (int k = 0; k < pns.size(); k++) {

                            if (pns.get(k).equals(jTable20.getValueAt(n, 0).toString())) {

                                continue outerloop;

                            }

                        }
                        pns.add(jTable20.getValueAt(n, 0).toString());
                        pnek += jTable20.getValueAt(n, 0).toString() + "|";

                    }

                    pnek = pnek.substring(0, pnek.length() - 1);

//összerakjuk a queryt
                    query = "select stations.name , terv.partnumber , terv.startdate , terv.comments , terv.qty_full from terv left join stations on stations.id = terv.stationid where terv.startdate >= now() and terv.active = 1 and terv.partnumber regexp '" + pnek + "' group by stations.name , terv.partnumber , terv.startdate , terv.job order by terv.startdate";
                    planconnect pc = new planconnect();
                    pc.lekerdez(query);
                    int size = 0;
                    if (pc.rs != null) {
                        pc.rs.last();    // moves cursor to the last row
                        size = pc.rs.getRow(); // get row id 
                    }
                    pc.rs.beforeFirst();
                    int[] qty = new int[size];
                    i = 0;
                    while (pc.rs.next()) {
                        boolean pipa = false;
                        if (pc.rs.getString(4).contains("Gyűjtsétek")) {

                            pipa = true;

                        }
                        model1.addRow(new Object[]{pc.rs.getString(1).toUpperCase(), pc.rs.getString(2), pc.rs.getString(3), null, pipa});
                        qty[i] = pc.rs.getInt(5);
                        i++;

                    }

                    jTable21.setModel(model1);
                    pc.kinyir();

//végigjárjuk a táblát és megszámoljuk mennyi lehetséges szalag gyűjthető
                    for (int k = 0; k < jTable21.getRowCount(); k++) {  //kistábla a gyártásokkal
                        int osszeg = 0;
                        for (int n = 0; n < jTable20.getRowCount(); n++) {  // az alkatrészes nagy tábla
//ha a programnevek közül az egyik megvan a másikban és a sor stimmel
                            if ((jTable21.getValueAt(k, 1).toString().contains(jTable20.getValueAt(n, 0).toString()) || jTable20.getValueAt(n, 0).toString().contains(jTable21.getValueAt(k, 1).toString())) && jTable21.getValueAt(k, 0).toString().equals(jTable20.getValueAt(n, 2).toString().substring(0, 1))) {

                                osszeg += (Integer.parseInt(jTable20.getValueAt(n, 7).toString()) * qty[k]);

                            }

                        }

                        jTable21.setValueAt(osszeg, k, 3);

                    }

                }

                System.err.println("");

            } catch (SQLException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jTable21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable21MouseClicked
        // ha klikk kijelöljük az alkatrészes táblán a megfelelő sorokat

        String program = jTable21.getValueAt(jTable21.getSelectedRow(), 1).toString();
        String sor = jTable21.getValueAt(jTable21.getSelectedRow(), 0).toString();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable20.getModel();

//visszatöltjük a stringtömbböl az adatokat
        model.setRowCount(0);

        for (int i = 0; i < alkatreszkereso.length; i++) {

            model.addRow(new Object[]{alkatreszkereso[i][0], alkatreszkereso[i][1], alkatreszkereso[i][2], alkatreszkereso[i][3], alkatreszkereso[i][4], alkatreszkereso[i][5], alkatreszkereso[i][6], alkatreszkereso[i][7]});

        }

        for (int n = 0; n < model.getRowCount(); n++) {  // az alkatrészes nagy tábla
//ha a programnevek közül az egyik megvan a másikban és a sor stimmel
            if (((program.contains(model.getValueAt(n, 0).toString()) || model.getValueAt(n, 0).toString().contains(program)) && sor.equals(model.getValueAt(n, 2).toString().substring(0, 1))) == false) {

                model.removeRow(n);
                n--;

            }

        }

        jTable20.setModel(model);

    }//GEN-LAST:event_jTable21MouseClicked

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (jTextField2.getText().equals("Játék!")) {

                jatek j = new jatek();
                j.setVisible(true);

            } else if (jTextField2.getText().equals("Imgup!")) {
                //betalloztatjuk a kep filet

                JFileChooser chooser = CTB_Filechooser.getFileChooserRiport();
                chooser.setDialogTitle("Kép tallózása!");
                FileFilter imageFilter = new FileNameExtensionFilter(
                        "Image files", ImageIO.getReaderFileSuffixes());
                chooser.setFileFilter(imageFilter);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    File file = chooser.getSelectedFile();
                    ImgUp u = new ImgUp(file);
                    u.setVisible(true);

                }

            } else {
                animation a = new animation();
                a.start();
                keszletszal k = new keszletszal(this);
                k.start();
            }

        }

    }//GEN-LAST:event_jTextField2KeyPressed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        //mi van az sn ben gomb
        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable22.getModel();
        model.setRowCount(0);

        tigerconnect tc = new tigerconnect();

        try {

            tc.storedSN(jTextField16.getText().trim());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // teszterek lekérdezése
        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
        Date tol = ablak.jDateChooser6.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String stol = df.format(tol);
        stol += " " + ablak.jTextField20.getText() + ":00";
        Date ig = ablak.jDateChooser7.getDate();
        df = new SimpleDateFormat("yyyy-MM-dd");
        String sig = df.format(ig);
        sig += " " + ablak.jTextField21.getText() + ":00";
        tigerconnect tc = new tigerconnect();
//ha MPT van kiválasztva
        if (jComboBox1.getSelectedItem().toString().equals("MPT")) {
            String header[] = {"System", "Freq", "Type", "PN", "QTY", "FPY"};
            for (int i = 0; i < jTable23.getColumnCount(); i++) {
                TableColumn column1 = jTable23.getTableHeader().getColumnModel().getColumn(i);

                column1.setHeaderValue(header[i]);
                this.repaint();
            }

            try {
                tc.storedMPTTesters(stol, sig);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
//ha UBT van kiválasztva
        if (jComboBox1.getSelectedItem().toString().equals("UBT")) {
            String header[] = {"System", "QTY", "PartNumber", "Pass", "Fail", "FPY"};
            for (int i = 0; i < jTable23.getColumnCount(); i++) {
                TableColumn column1 = jTable23.getTableHeader().getColumnModel().getColumn(i);

                column1.setHeaderValue(header[i]);
                this.repaint();
            }

            try {
                tc.storedUBTTesters(stol, sig);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // kiszállításlekérdezés
        ablak.stat.beir(System.getProperty("user.name"), ablak.jTabbedPane1.getTitleAt(ablak.jTabbedPane1.getSelectedIndex()), "", "gabor.hanacsek@sanmina.com");
        animation a = new animation();
        a.start();
        kiszallitasszal ksz = new kiszallitasszal();
        ksz.start();


    }//GEN-LAST:event_jButton27ActionPerformed

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void jTextField25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField25ActionPerformed

    private void jTextField26KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyReleased
        //kereső a kiszállításokban
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) jTable24.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);

        jTable24.setRowSorter(tr);

        tr.setRowFilter(RowFilter.regexFilter(jTextField26.getText().trim()));

        int total = 0;

        for (int i = 0; i < jTable24.getRowCount(); i++) {
            String value = (String) jTable24.getValueAt(i, 1);
            total += Integer.parseInt(value);
        }

        jTextField25.setText(Integer.toString(total));


    }//GEN-LAST:event_jTextField26KeyReleased

    private void jTable24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable24MouseClicked
        //kijelölések összege a kiszállítások fülön
        Integer osszeg = 0;
        int[] selectedrows = jTable24.getSelectedRows();

        for (int i = 0; i < selectedrows.length; i++) {

            osszeg += Integer.parseInt(jTable24.getValueAt(selectedrows[i], 1).toString());

        }

        jTextField24.setText(osszeg.toString());
    }//GEN-LAST:event_jTable24MouseClicked

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        //export to exc a kiszállításokat
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            ExcelExporter exp = new ExcelExporter();
            exp.fillData(jTable24, new File(fileToSave.getAbsolutePath() + ".xls"));

        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // a sorok priojának mentése

        //kell egy insert string , ami updatel az adatbázisban ha a prio és a dátum már létezik
        String query = "insert into sorokprioja (prio ,sor , datum , priodatum) values";
        //bejárjuk a táblát és beletesszük az adatokat a querybe
        String adatok = "";
        for (int c = 1; c < jTable25.getColumnCount(); c++) {

            for (int r = 0; r < jTable25.getRowCount(); r++) {

                String sor = "";
                try {

                    sor = jTable25.getValueAt(r, c).toString();

                } catch (Exception e) {

                }
                if (!sor.equals("")) {
                    adatok += "('" + jTable25.getValueAt(r, 0).toString() + "','" + sor + "','" + jTable25.getColumnModel().getColumn(c).getHeaderValue() + "','" + jTable25.getValueAt(r, 0).toString() + jTable25.getColumnModel().getColumn(c).getHeaderValue() + "'),";
                }
            }

        }
        adatok = adatok.substring(0, adatok.length() - 1);

        query += adatok + "on duplicate key update sor = values(sor)";
        //System.out.println();
        planconnect pc = new planconnect();
        pc.feltolt(query, true);
        pc.kinyir();

    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton29MouseClicked
        //ha nem aktiva gomb

        if (!planner) {

            Prio_Bejel p = new Prio_Bejel();
            p.setVisible(true);

        }
    }//GEN-LAST:event_jButton29MouseClicked

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // complete qty lekerdez gombja
        //pn lista összeállítása       
        Methods a = new Methods(this);
        String pnlist = a.PNosszefuz(jTable26);
//az api összeállítása
        URL api = null;
        try {
            api = new URL(a.CreateApiUrl(jDateChooser10, "http://143.116.140.120/rest/request.php?page=planning_product_history&product=termekek&format=xml&loc_ts=date", pnlist));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }
//feldolgozzuk az apit
        ArrayList<String> lista = new ArrayList();

        String nodelist = "planning_product_history";
        lista.add("Serial_Number");
        lista.add("Shop_Order_Number");
        lista.add("part_number");
        lista.add("Workstation");
        lista.add("Qty");
        lista.add("Unit_Status");
        lista.add("Location_DateTime");
        lista.add("last_complete_dateTime");
        lista.add("complete_ts");
        xmlfeldolg x = new xmlfeldolg();
        Object[][] adatok = (Object[][]) x.xmlfeldolg(api, nodelist, lista);

//betesszük táblába
        DefaultTableModel model = new DefaultTableModel();
        model.setRowCount(0);
        model = (DefaultTableModel) jTable27.getModel();
        jTable27.setModel(x.totable(model, adatok));
        TablaOszlopSzelesseg(jTable27);
        if (jCheckBox6.isSelected()) {
            a.GroupByPn(adatok);
        } else {

            a.GroupByJOB(adatok);

        }


    }//GEN-LAST:event_jButton30ActionPerformed

    public void gombenged() {

        try {
            jButton12.setEnabled(true);
            jButton11.setEnabled(true);
            jButton13.setEnabled(true);
            jButton15.setEnabled(true);

        } catch (Exception e) {

            System.err.println(e.getStackTrace());

        }

    }

    private void filter(String query) {

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        TableRowSorter<DefaultTableModel> tr1 = new TableRowSorter<DefaultTableModel>(model1);
        jTable1.setRowSorter(tr);
        jTable2.setRowSorter(tr1);
        tr.setRowFilter(RowFilter.regexFilter(query));
        tr1.setRowFilter(RowFilter.regexFilter(query));

        int total = 0;

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String value = (String) jTable1.getValueAt(i, 2);
            total += Integer.parseInt(value);
        }

        jTextField6.setText(Integer.toString(total));

    }

    public ablak(JButton jButton1, JButton jButton2, JLabel jLabel1, JLabel jLabel2, JLabel jLabel3, JLabel jLabel4, JLabel jLabel5, JLabel jLabel6, JLabel jLabel7, JLabel jLabel8, JLabel jLabel9, JMenuItem jMenuItem1, JPanel jPanel1, JPanel jPanel2, JScrollPane jScrollPane1, JScrollPane jScrollPane2, JScrollPane jScrollPane3, JTabbedPane jTabbedPane1, JTable jTable1, JTable jTable2, JTable jTable3, JTextField jTextField1, JTextField jTextField2, JTextField jTextField3, JTextField jTextField5) throws HeadlessException {

        this.jButton1 = jButton1;
        this.jButton2 = jButton2;
        this.jLabel1 = jLabel1;
        this.jLabel2 = jLabel2;
        this.jLabel3 = jLabel3;
        this.jLabel4 = jLabel4;
        this.jLabel5 = jLabel5;
        this.jLabel6 = jLabel6;
        this.jLabel7 = jLabel7;
        this.jLabel8 = jLabel8;
        this.jLabel9 = jLabel9;
        this.jPanel1 = jPanel1;
        this.jPanel2 = jPanel2;
        this.jScrollPane1 = jScrollPane1;
        this.jScrollPane2 = jScrollPane2;
        this.jScrollPane3 = jScrollPane3;
        this.jTabbedPane1 = jTabbedPane1;
        this.jTable1 = jTable1;
        this.jTable2 = jTable2;
        this.jTable3 = jTable3;
        this.jTextField1 = jTextField1;
        this.jTextField2 = jTextField2;
        this.jTextField3 = jTextField3;
        this.jTextField5 = jTextField5;
    }

    private void filter1(String query) {

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(modelacti);
        tr = new TableRowSorter<DefaultTableModel>(modelacti);
        jTable3.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));

        int total = 0;

        for (int i = 0; i < jTable3.getRowCount(); i++) {
            String value = (String) jTable3.getValueAt(i, 4);
            total += Integer.parseInt(value);
        }

        jTextField5.setText(Integer.toString(total));

    }

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
            java.util.logging.Logger.getLogger(ablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ablak().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    public static javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public static javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    public static javax.swing.JCheckBox jCheckBox2;
    public static javax.swing.JCheckBox jCheckBox3;
    public static javax.swing.JCheckBox jCheckBox4;
    public static javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JComboBox<String> jComboBox1;
    public static javax.swing.JComboBox<String> jComboBox2;
    public static javax.swing.JComboBox<String> jComboBox3;
    public static com.toedter.calendar.JDateChooser jDateChooser1;
    public static com.toedter.calendar.JDateChooser jDateChooser10;
    public static com.toedter.calendar.JDateChooser jDateChooser2;
    public static com.toedter.calendar.JDateChooser jDateChooser3;
    public static com.toedter.calendar.JDateChooser jDateChooser4;
    public static com.toedter.calendar.JDateChooser jDateChooser5;
    public static com.toedter.calendar.JDateChooser jDateChooser6;
    public static com.toedter.calendar.JDateChooser jDateChooser7;
    public static com.toedter.calendar.JDateChooser jDateChooser8;
    public static com.toedter.calendar.JDateChooser jDateChooser9;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    public static javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JList<String> jList1;
    public static javax.swing.JList<String> jList2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
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
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public static javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable10;
    public static javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    public static javax.swing.JTable jTable14;
    public static javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    public static javax.swing.JTable jTable17;
    public static javax.swing.JTable jTable18;
    public static javax.swing.JTable jTable19;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTable jTable20;
    private javax.swing.JTable jTable21;
    public static javax.swing.JTable jTable22;
    public static javax.swing.JTable jTable23;
    public static javax.swing.JTable jTable24;
    public static javax.swing.JTable jTable25;
    public static javax.swing.JTable jTable26;
    private javax.swing.JTable jTable27;
    public static javax.swing.JTable jTable28;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    public static javax.swing.JTable jTable7;
    public static javax.swing.JTable jTable8;
    public static javax.swing.JTable jTable9;
    public static javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    public static javax.swing.JTextField jTextField12;
    public static javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    public static javax.swing.JTextField jTextField2;
    public static javax.swing.JTextField jTextField20;
    public static javax.swing.JTextField jTextField21;
    public static javax.swing.JTextField jTextField22;
    public static javax.swing.JTextField jTextField23;
    public static javax.swing.JTextField jTextField24;
    public static javax.swing.JTextField jTextField25;
    public static javax.swing.JTextField jTextField26;
    public static javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    public static javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    public static javax.swing.JTextField jTextField8;
    public static javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
