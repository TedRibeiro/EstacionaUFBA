package com.matc89.estacionaufba;

import android.text.TextWatcher;
import android.widget.EditText;
import android.text.Editable;

/**
 * Created by icaroerasmo on 15/07/17.
 */

public abstract class Mask {
    public static String unmask(String s) {

        /*if(!s.matches("([a-zA-Z]{3}-|[a-z-A-Z]{0,3}|[a-zA-Z]{3}-[0-9]{1,4}|[a-zA-Z]{3}-[0-9]{4})")){
            return s.substring(0, s.length()-1);
        }*/

        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String str = Mask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for(char a : str.toCharArray()){

                    if(i < mask.length() && ((Character.isLetter(a) && Character.isLetter(mask.charAt(i))) ||
                            (Character.isDigit(a) && Character.isDigit(mask.charAt(i))))){
                        mascara += a;

                        if(i < mask.length()-1 && !Character.isLetter(mask.charAt(i+1)) &&
                                !Character.isDigit(mask.charAt(i+1))){
                            mascara += mask.charAt(i+1);
                            i++;
                        }

                    }else{
                        ediTxt.setText(mascara);
                        return;
                    }

                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}