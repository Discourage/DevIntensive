package com.softdesign.devintensive.utils.ValidationText;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EditTextUri {

    public static TextWatcher insert(final EditText editText, final ImageView imageView) {
        return new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("http");
                Matcher m = p.matcher(editText.getText().toString());
                if (m.find()) {
                    editText.setError("Ссылка не должна содержать 'http' ");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
}

