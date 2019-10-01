/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_CellClass {

    int eng = 0;
    String value = "";
    Double engtime = 0.0;
    int szin = 0;
    String ah = "";
    String pktomig = "";
    int all = 0;

    public Tc_CellClass(String value, int eng, double engtime, int szin, String ah, String pktomig, int all) {

        this.value = value;
        this.eng = eng;
        this.engtime = engtime;
        this.szin = szin;
        this.ah = ah;
        this.pktomig = pktomig;
        this.all = all;

    }

}
