package com.matc89.estacionaufba.util;

import android.text.TextWatcher;
import android.widget.EditText;
import android.text.Editable;
import android.widget.Toast;

/**
 * Created by icaroerasmo on 15/07/17.
 */

public abstract class Mask {
    public static String unmask(String s) {

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

    public static TextWatcher insert(final int maxSize, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String str = Mask.unmask(s.toString());

                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                if(str.length() <= maxSize){
                    isUpdating = true;
                    ediTxt.setText(str);
                    ediTxt.setSelection(str.length());
                }else{
                    isUpdating = true;
                    ediTxt.setText(old);
                    ediTxt.setSelection(old.length());
                    Toast.makeText(ediTxt.getContext(), "Tamanho máximo de "+maxSize+" caracteres atingido",
                            Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

}