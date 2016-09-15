package com.cybemos.analyser.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.parser.Extension;
import com.cybemos.analyser.data.parser.ParserController;

import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Settings {

    private static final String TAG = "Settings";
    private final Context context;
    @ColorInt
    private final int default_color_received;
    @ColorInt
    private final int default_color_sent;

    public final static int PIE_GRAPH = 1;
    public final static int BAR_GRAPH = 2;

    public final static int FORMAT_REAL_VALUES = 1;
    public final static int FORMAT_PERCENTAGE = 2;

    public Settings(Context context) {
        this.context = context;
        default_color_received = ContextCompat.getColor(context, R.color.color_received);
        default_color_sent = ContextCompat.getColor(context, R.color.color_sent);
    }

    public void reset() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().clear().apply();
    }

    @ColorInt
    public int getColor_received() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.color_picker_received_id);
        return sharedPref.getInt(key, default_color_received);
    }


    @ColorInt
    public int getColor_sent() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.color_picker_sent_id);
        return sharedPref.getInt(key, default_color_sent);
    }

    public int getChosenGraph() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.graph_type_id);
        String value = sharedPref.getString(key, Integer.toString(PIE_GRAPH));
        int intVal;
        try {
            intVal = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            intVal = PIE_GRAPH;
            //noinspection HardCodedStringLiteral
            Log.e(TAG, value+" can't be converted to an integer");
        }
        return intVal;
    }

    public int getChosenFormat() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.format_graph_id);
        String value = sharedPref.getString(key, Integer.toString(FORMAT_REAL_VALUES));
        int intVal;
        try {
            intVal = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            intVal = FORMAT_REAL_VALUES;
            //noinspection HardCodedStringLiteral
            Log.e(TAG, value+" can't be converted to an integer");
        }
        return intVal;
    }

    public int getNumberOfFractionDigits() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.preferences_nb_fraction_digits_id);
        String value = sharedPref.getString(key, "0");
        int intVal;
        try {
            intVal = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            intVal = 0;
            //noinspection HardCodedStringLiteral
            Log.e(TAG, value+" can't be converted to an integer");
        }
        return intVal;
    }

    public Extension getDefaultExtension() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        ParserController controller = ParserController.getInstance();
        List<Extension> extensions = controller.getExtensions();
        String defaultFormat = extensions.get(0).getExtension();
        String key = context.getString(R.string.preferences_format_export_id);
        String value = sharedPref.getString(key, defaultFormat);
        Extension extension = null;
        for (Extension e : extensions) {
            if (e.getExtension().equals(value)) {
                extension = e;
            }
        }
        return extension;
    }

}
