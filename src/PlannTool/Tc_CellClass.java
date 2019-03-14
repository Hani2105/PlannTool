/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

/**
 *
 * @author gabor_hanacsek
 */
public class Tc_CellClass {

    int eng = 0;
    String value = "";
    Double engtime = 0.0;

    public Tc_CellClass(String value, int eng, double engtime) {

        this.value = value;
        this.eng = eng;
        this.engtime = engtime;

    }

}
