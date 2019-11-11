/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.CTB_CALC;

import static PlannTool.CTB_CALC.CTB.jTable1;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabor_hanacsek
 */
public class CTB_NEW_Variables {
//------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------//  
//                               Táblák
//a CTB tábla modellje , bal oldali , a po kalkulációnál állítjuk be először            jtable1
    public static DefaultTableModel ctbmodel = new DefaultTableModel();
//a demand tábla modellje , a fullcalc állítjuk be                              jtable4
    public static DefaultTableModel demandmodel = new DefaultTableModel();
//oh tábla modellje , a fullcalc állítjuk be                                 jtable2
    public static DefaultTableModel ohmodel = new DefaultTableModel();
//workorders tábla modellje , a fullcalc nál állítjuk be                         jtable5
    public static DefaultTableModel womodel = new DefaultTableModel();
//a kikalkulált bom tábla modellje , a bom kalkulációnál állítjuk be                    jtable7
    public static DefaultTableModel calcbommodel = new DefaultTableModel();
//az indented bom tábla modellje a bom calc nál vesszük  fel                            jtable6
    public static DefaultTableModel indentedbommodel = new DefaultTableModel();
//az allocation tábla modellje a bomcalcnál vesszük fel                             jtable3
    public static DefaultTableModel allocmodel = new DefaultTableModel();
//a lost tábla modellje , a bomcalcnál vesszük fel                             jtable12      
    public static DefaultTableModel lostmodel = new DefaultTableModel();
//a horizontal modellje , a bomcalcnál vesszük fel                        jtable13
    public static DefaultTableModel horizontalmodel = new DefaultTableModel();
//a terv tábla változója   , akkor állítjuk be amikor leütjük az entert a táblán  jtable11
    public static DefaultTableModel tervtablemodel = new DefaultTableModel();
//a topshortmodel változója, a topshorttradeban állítjuk be           jtable9
    public static DefaultTableModel topshortmodel = new DefaultTableModel();
//ship tábla modellje a control panelnál állítjuk be
    public static DefaultTableModel shipmodel = new DefaultTableModel();

//----------------------------------------------------------------------------------------//
//                             globális változók   
//itt tároljuk a tervtábla adatait
    public static String[][] adatok;

}
