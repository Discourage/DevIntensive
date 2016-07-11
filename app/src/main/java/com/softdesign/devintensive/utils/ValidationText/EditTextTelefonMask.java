package com.softdesign.devintensive.utils.ValidationText;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

public abstract class EditTextTelefonMask {
    private static final String mask11 = "# (###) ### ## ##";
    private static final String mask12 = "## (###) ### ## ##";

    public static String unmask(String s) {
        return s.replaceAll("[^+0-9]*", "");
    }

    public static TextWatcher insert(final EditText editText, final ImageView imageView) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {}


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                String userPhoneString = EditTextTelefonMask.unmask(s.toString());
                String mask;
                String defaultMask = getDefaultMask(userPhoneString);


                switch (userPhoneString.length()) {
                    case 12:
                        mask = mask12;
                        break;
                    default:
                        mask = defaultMask;
                        break;
                }

                if (userPhoneString.length() == 0 || userPhoneString.length() <= 10 || userPhoneString.length() >= 21) {
                    editText.setError("Номер телефона должен содержать от 11 до 20 знаков");
                }

                String mascara = "";
                if (isUpdating) {
                    old = userPhoneString;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && userPhoneString.length() > old.length()) || (m != '#' && userPhoneString.length() < old.length() && userPhoneString.length() != i)) {
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += userPhoneString.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }

        };
    }

    private static String getDefaultMask(String str) {
        String defaultMask = mask11;
        if (str.length() > 11) {
            defaultMask = mask12;
        }
        return defaultMask;
    }

}

