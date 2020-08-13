/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import PlannTool.CONNECTS.connect;
import PlannTool.xmlfeldolg;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_StockInfo extends javax.swing.JFrame {

    /**
     * Creates new form CTB_StockInfo
     */
    String pn;

    public CTB_StockInfo(String pn) throws SQLException, ClassNotFoundException {
        this.pn = pn;
        initComponents();
        leker();
    }

    public void leker() throws SQLException, ClassNotFoundException {

        DefaultListModel model = new DefaultListModel();
        model.clear();

        URL url = null;
        xmlfeldolg xxx = new xmlfeldolg();
        try {
            url = new URL("http://143.116.140.120/rest/request.php?page=planning_shipment_plan_process_all&product=" + pn + "&format=xml");
            ArrayList<String> lista = new ArrayList();
            String nodelist = "planning_shipment_plan_process_all";
            lista.add("Part_Number");
            lista.add("SFDC_Location_Name");
            lista.add("Serial_Number");
            CTB.WipStockData = (Object[][]) xxx.xmlfeldolg(url, nodelist, lista);

            for (int i = 0; i < CTB.WipStockData.length; i++) {

                model.addElement(CTB.WipStockData[i][0].toString() + " " + CTB.WipStockData[i][1].toString() + " " + CTB.WipStockData[i][2].toString());

            }

        } catch (MalformedURLException ex) {
            
        }

//oracle készlet
        String query = "SELECT oracle_backup_subinv.item as partnumber , oracle_backup_subinv.subinv , oracle_backup_subinv.locator , oracle_backup_subinv.quantity , oracle_backup_subinv.exported FROM trax_mon.oracle_backup_subinv where item like '%" + pn + "%'";
        connect c = new connect(query);

        try {
            c.rs.last();
            int sor = c.rs.getRow();
            c.rs.beforeFirst();
            CTB.OraStockData = new Object[sor][3];

            int i = 0;
            while (c.rs.next()) {

                model.addElement(c.rs.getString(2) + " " + c.rs.getString(3) + "      " + c.rs.getString(4));

//                CTB.OraStockData[i][0] = c.rs.getString(2);
//                CTB.OraStockData[i][1] = c.rs.getString(3);
//                CTB.OraStockData[i][2] = c.rs.getString(4);
                i++;
            }
        } catch (SQLException ex) {
            
        }
        
        jList1.setModel(model);

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Készlet infó");

        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(CTB_StockInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CTB_StockInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CTB_StockInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CTB_StockInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new CTB_StockInfo().setVisible(true);
////            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
