/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import PlannTool.ablak;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author gabor_hanacsek
 */
public class xmlfeldolg {

    //public URL url;
    //private Object RowData[][];
    public Object xmlfeldolg(URL url, String nodelist, ArrayList<String> lista) {

        URLConnection connection = null;
        try {
            connection = url.openConnection();

        } catch (IOException ex) {
            Logger.getLogger(ablak.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ablak.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(connection.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(ablak.class
                    .getName()).log(Level.SEVERE, null, ex);
            
           
            


        } catch (SAXException ex) {
            Logger.getLogger(ablak.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName(nodelist);
        Object RowData[][] = new String[nList.getLength()][lista.size()];

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = (Node) nList.item(temp);
            Element eElement = (Element) nNode;
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                for (int i = 0; i < lista.size(); i++) {

                    RowData[temp][i] = new String(eElement
                            .getElementsByTagName(lista.get(i)).item(0)
                            .getTextContent());
                    //System.out.println("d");

                }

            }

        }

        return RowData;
    }

    public TableModel totablestatus(TableModel model, Object[][] obi) {
        String pn = "";
        Boolean irtunke = false;
        for (int k = 0; k < model.getRowCount(); k++) {
            irtunke = false;
            if (model.getValueAt(k, 0) != null) {
                pn = model.getValueAt(k, 0).toString().toUpperCase();
            } else {

                continue;

            }
            for (int i = 0; i < obi.length; i++) {

                if (pn.equals(obi[i][0])) {

                    irtunke = true;
                    for (int j = 0; j < model.getColumnCount() - 1; j++) {
                        model.setValueAt(obi[i][j], k, j + 1);

                    }

                }

            }

            if (irtunke == false) {

                model.setValueAt("Nincs ilyen SN", k, 1);

            }
        }
        return model;
    }

    public TableModel totable(DefaultTableModel model, Object[][] obi) {

        String[] adat = new String[model.getColumnCount()];

        
        
        for (int i = 0; i < obi.length; i++) {
            model.addRow(new Object[model.getColumnCount()]);
            for (int j = 0; j < model.getColumnCount(); j++) {

                model.setValueAt(obi[i][j], i, j);

            }

        }

        return model;
    }

}
