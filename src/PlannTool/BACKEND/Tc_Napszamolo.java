/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool.BACKEND;


import java.util.Date;
import org.joda.time.Days;
import org.joda.time.LocalDate;




/**
 *
 * @author gabor_hanacsek
 */
public class Tc_Napszamolo {

    public int daysBetweenUsingJoda(Date d1, Date d2){
    return Days.daysBetween(
           new LocalDate(d1.getTime()), 
           new LocalDate(d2.getTime())).getDays();
 }

    

}
