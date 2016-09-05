package com.cybemos.analyser.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cybemos.analyser.data.exceptions.NumberAccessException;

import java.io.File;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Util {

    private final static String TAG = "Util";
    private static Settings settings;
    public static Context context;

    @NonNull
    public static Settings getSettings() {
        if (settings == null) {
            settings = new Settings(context);
        }
        return settings;
    }

    public static String sanitizeNumber(@NonNull String number) {
        number = number.replace(" ", "");
        if (number.indexOf("0") == 0) {
            number = number.replaceFirst("^0", "+33");
        }
        //noinspection HardCodedStringLiteral
        Log.i(TAG, "number : "+number);
        return number;
    }

    @Nullable
    public static String getExtension(@Nullable File file) {
        String extension = null;
        if (file != null) {
            String name = file.getName();
            int index = name.lastIndexOf('.');
            if (index >= 0) {
                extension = name.substring(index+1);
            }
        }
        return extension;
    }

    public static String getMyNumber(Context context) throws NumberAccessException {
        String number;
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            number = tMgr.getLine1Number();
        } catch (SecurityException e) {
            throw new NumberAccessException();
        }
        return number;
    }

}
