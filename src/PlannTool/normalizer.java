/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlannTool;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 *
 * @author gabor_hanacsek
 */
public class normalizer {

    public static String removeAccents(String text) {
        return text == null ? null
                : Normalizer.normalize(text, Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
