/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.CONNECTS.planconnect;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author gabor_hanacsek
 */
public class Prioritasok {

    public void feltolttabla() throws SQLException, ClassNotFoundException {

//lekérdezzük a sorainkat
        planconnect pc = new planconnect();
        String query = "SELECT distinct stations.name from stations\n"
                + "left join terv on stations.id = terv.stationid\n"
                + "where terv.startdate >= '" + ablak.jTable25.getColumnModel().getColumn(1).getHeaderValue().toString() + "' and terv.startdate <= '" + ablak.jTable25.getColumnModel().getColumn(14).getHeaderValue().toString() + "' and active = 1\n"
                + "order by name asc";

        pc.lekerdez(query);

        pc.rs.last();
        int utsosor = pc.rs.getRow();
        pc.rs.beforeFirst();

//létrehozunk egy tömböt ami a sort , a kommentet és azt tartalmazza , hogy kiválasztottuk e már
//az első legyen üres
        String adatok[][] = new String[utsosor + 1][3];
        adatok[0][0] = "";
//beletesszük az adtokat
        int i = 1;
        while (pc.rs.next()) {

            adatok[i][0] = pc.rs.getString(1);
            i++;
        }

//felépítjük úgy a táblát , hogy a combobox tartalmazza a sorokat és annyi sor legyen a táblában amennyi gyártósorunk van        
        JComboBox comboBox = new JComboBox();

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) ablak.jTable25.getModel();
        model.setRowCount(0);
        if (adatok.length > 1) {
            for (i = 0; i < adatok.length; i++) {

                comboBox.addItem(adatok[i][0].toString().toUpperCase());

            }
            for (i = 0; i < adatok.length; i++) {

                //comboBox.addItem(adatok[i][0]);
                model.addRow(new Object[]{i + 1 + " prio"});
                //model.addRow(new Object[]{"Komment:", "", ""});

            }
        } 

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//megnezzuk , hogy van e ismetlodes
                int editedsor = ablak.jTable25.getSelectedRow();
                int editedoszlop = ablak.jTable25.getSelectedColumn();
                for (int n = 0; n < ablak.jTable25.getRowCount(); n++) {
                    String sor;
                    try {
                        sor = ablak.jTable25.getValueAt(n, editedoszlop).toString();
                    } catch (Exception ex) {

                        continue;
                    }
                    if (sor.equals("") || sor == null) {
                        continue;

                    }

                    for (int i = 0; i < ablak.jTable25.getRowCount(); i++) {
                        try {
                            if (sor.equals(ablak.jTable25.getValueAt(i, editedoszlop).toString()) && n != i) {

                                //Custom button text
                                Object[] options = {"Igen, cseréljük!",
                                    "Ne, maradjon az eredeti!!"
                                };
                                int k = JOptionPane.showOptionDialog(ablak.jTable25,
                                        "Ezt a sort megadtad  " + (n + 1) + ". és " + (i + 1) + ". prioritásnak!",
                                        "Duplikációt találtunk!?",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[0]);

                                if (k == 0) {

                                    if (n == editedsor) {
                                        ablak.jTable25.setValueAt("", i, editedoszlop);
                                    } else {

                                        ablak.jTable25.setValueAt("", n, editedoszlop);

                                    }

                                } else {

                                    ablak.jTable25.setValueAt("", editedsor, editedoszlop);

                                }

                            }
                        } catch (Exception ex) {
                            continue;
                        }

                    }

                }

            }

        });

        for (i = 1; i < ablak.jTable25.getColumnCount(); i++) {

            TableColumn sorok = ablak.jTable25.getColumnModel().getColumn(i);
            sorok.setCellEditor(new DefaultCellEditor(comboBox));

        }

        TablaOszlopSzelesseg(ablak.jTable25);
        setdatetotableheader();
        getdata();

        ablak.jComboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                
                setdatetotableheader();

                try {
                    feltolttabla();
                    getdata();
                } catch (SQLException ex) {
                    Logger.getLogger(Prioritasok.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Prioritasok.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

//combo 3 action 
        ablak.jComboBox3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setdatetotableheader();
                try {
                    feltolttabla();
                    getdata();
                } catch (SQLException ex) {
                    Logger.getLogger(Prioritasok.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Prioritasok.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        pc.kinyir();

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

    public void setdatetotableheader() {

        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(ablak.jComboBox3.getSelectedItem().toString()));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(ablak.jComboBox2.getSelectedItem().toString()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 1; i < ablak.jTable25.getColumnCount(); i += 2) {

            ablak.jTable25.getColumnModel().getColumn(i).setHeaderValue(sdf.format(cal.getTime()) + " DE");
            ablak.jTable25.getColumnModel().getColumn(i + 1).setHeaderValue(sdf.format(cal.getTime()) + " DU");
            cal.add(Calendar.DATE, 1);

        }

        TablaOszlopSzelesseg(ablak.jTable25);

    }

    public void getdata() throws SQLException, ClassNotFoundException {

        String tol = ablak.jTable25.getColumnModel().getColumn(1).getHeaderValue().toString();
        String ig = ablak.jTable25.getColumnModel().getColumn(14).getHeaderValue().toString();
        String query = "select * from sorokprioja where datum >= '" + tol + "' and datum <= '" + ig + "'";
        planconnect pc = new planconnect();
        pc.lekerdez(query);
        pc.rs.last();
        int utsosor = pc.rs.getRow();
        pc.rs.beforeFirst();
        String[][] adatok = new String[utsosor][3];

        for (int i = 0; i < adatok.length; i++) {

            pc.rs.next();
            adatok[i][0] = pc.rs.getString(2); //prio
            adatok[i][1] = pc.rs.getString(3); //sor
            adatok[i][2] = pc.rs.getString(4); //dátum

        }

//kinullázzuk az adatokat a táblából
        for (int i = 1; i < ablak.jTable25.getColumnCount(); i++) {

            for (int n = 0; n < ablak.jTable25.getRowCount(); n++) {

                ablak.jTable25.setValueAt("", n, i);

            }

        }

        //pörgetjük a táblát      
        for (int c = 0; c < ablak.jTable25.getColumnCount(); c++) {
            //elkezdjük pörgetni az adatokat is
            for (int i = 0; i < adatok.length; i++) {
//ha egyezik az adatok dátuma akkor kiszedjük a priot
                if (adatok[i][2].toString().equals(ablak.jTable25.getColumnModel().getColumn(c).getHeaderValue().toString())) {

                    String prio = adatok[i][0].toString();

//pörgetjük a tábla prio oszlopát és ha egyezik a prioval beirjuk a sort
                    for (int r = 0; r < ablak.jTable25.getRowCount(); r++) {

                        if (prio.equals(ablak.jTable25.getValueAt(r, 0).toString())) {

                            ablak.jTable25.setValueAt(adatok[i][1], r, c);

                        }

                    }

                }
            }

        }

        pc.kinyir();
    }

    public void legordulokbeallit() {

        //a hétnek a száma
        int weeknumber = LocalDateTime.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        ablak.jComboBox2.setSelectedItem(String.valueOf(weeknumber));
//az év(ek) száma
        int[] yearnumber = new int[3];
        Calendar c = Calendar.getInstance();
        yearnumber[1] = c.get(Calendar.YEAR);
        c.add(Calendar.YEAR, -1);
        yearnumber[0] = c.get(Calendar.YEAR);
        c.add(Calendar.YEAR, 2);
        yearnumber[2] = c.get(Calendar.YEAR);

//beállítjuk a jcombo 3 ba
        for (int k = 0; k < yearnumber.length; k++) {

            ablak.jComboBox3.addItem(String.valueOf(yearnumber[k]));

        }

//beállítjuk az idént alapértelmezettként
        ablak.jComboBox3.setSelectedIndex(1);

    }

}
