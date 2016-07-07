package com.softdesign.devintensive.utils.ValidationText;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextMail {


    public static TextWatcher insert(final EditText editText, final ImageView imageView) {
        return new TextWatcher() {


            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("^([a-zA-z\\d\\u002E\\u005F-]{3,})+@+([a-zA-Z\\d\\u002E\\u005F-]{2,})+(\\u002E+[a-z]{2,})$");
                Matcher m = p.matcher(editText.getText().toString());
                if (!m.find()) {
                    editText.setError("Некорректный email(Необходим email вида xxx@xx.xx)");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}

