package hr.ferit.iveselin.mechanicgarageandroid.utils;

import android.text.TextUtils;

public class StringUtils {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
