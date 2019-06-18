/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.ANIMATIONS.animation;
import PlannTool.CONNECTS.postgreconnect;
import static PlannTool.ablak.jTextField25;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class kiszallitasszal extends Thread {

    public void run() {

        String termekek = "";
        for (int i = 0; i < ablak.jList2.getModel().getSize(); i++) {

            if (ablak.jList2.isSelectedIndex(i)) {

                termekek += "'" + ablak.jList2.getModel().getElementAt(i).toString() + "',";

            }

        }

        if (termekek.equals("")) {

            infobox inf = new infobox();
            inf.infoBox("Nem választottál ki termékcsaládot!", "Hiba!");
            return;

        }

        termekek = termekek.substring(0, termekek.length() - 1);

//kiszedjük az időpontokat
        Date tol = ablak.jDateChooser9.getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String stol = df.format(tol);
        stol += " " + ablak.jTextField22.getText() + ":00";
        Date ig = ablak.jDateChooser8.getDate();
        df = new SimpleDateFormat("yyyy-MM-dd");
        String sig = df.format(ig);
        sig += " " + ablak.jTextField23.getText() + ":00";

        String query = "select partnumber.partnumber, sum(containers.quantity) , shipping_content.po , customer_address.city , customer_address.company FROM \"HBPackage\".containers \n"
                + "left join \"HBPackage\".partnumber on containers.partnumber_id=partnumber.id \n"
                + "left join \"HBPackage\".second_containers on second_containers.id = containers.second_ctn_id \n"
                + "left join \"HBPackage\".shipping_content on shipping_content.id = second_containers.shipping_content_id left join \"HBPackage\".shipping on shipping.id = shipping_content.shipping_id \n"
                + "left join \"HBPackage\".customer_address on customer_address.id = shipping.address_id \n"
                + "where containers.second_ctn_id in (SELECT id FROM \"HBPackage\".second_containers where shipping_id in\n"
                + "(SELECT \"HBPackage\".shipping.id FROM \"HBPackage\".shipping left join \"HBPackage\".customer_type on customer_type.id = shipping.customer_type \n"
                + "where targetdate between '" + stol + "' and '" + sig + "' \n"
                + "and customer_type.name in (" + termekek + ") )) \n"
                + "group by partnumber.partnumber , shipping_content.po , customer_address.city , customer_address.company";

        postgreconnect pc = new postgreconnect();
        try {
            pc.lekerdez(query);
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) ablak.jTable24.getModel();
            model.setRowCount(0);

            while (pc.rs.next()) {

                model.addRow(new Object[]{pc.rs.getString(1), pc.rs.getString(2), pc.rs.getString(3), pc.rs.getString(4), pc.rs.getString(5),});

            }

            ablak.jTable24.setModel(model);

        } catch (SQLException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ablak.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pc.kinyir();

        int total = 0;
        for (int i = 0; i < ablak.jTable24.getRowCount(); i++) {
            String value = (String) ablak.jTable24.getValueAt(i, 1);
            total += Integer.parseInt(value);
        }

        jTextField25.setText(Integer.toString(total));
        animation.rajzol = false;

    }

}
