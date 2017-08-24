package com.cybemos.analyser.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cybemos.analyser.data.exceptions.NumberAccessException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class Util {

    /**
     * Usefull to retrieve traces using regex.
     */
    private final static String TAG = "Util";
    /**
     * Setting of the application
     */
    private static Settings settings;
    /**
     * Context of the application.
     * Initialized by {@link com.cybemos.analyser.SMSAnalyserApplication}
     */
    public static Context context;

    @NonNull
    public static Settings getSettings() {
        if (settings == null) {
            settings = new Settings(context);
        }
        return settings;
    }

    /**
     * Sanitize a number
     * @param number number you want to sanitize
     * @return the sanitized number
     */
    public static String sanitizeNumber(@NonNull String number) {
        number = number.replace(" ", "");
        if (number.indexOf("0") == 0) {
            number = number.replaceFirst("^0", "+33");
        }
        //noinspection HardCodedStringLiteral
        Log.i(TAG, "number : "+number);
        return number;
    }

    /**
     * Get the extension of a file.
     * @param file some not null file
     * @return the extension of a file
     */
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

    /**
     * Return the number of the smartphone it is running on.
     * @param context context of the application
     * @return the number of the smartphone it is running on
     * @throws NumberAccessException if there is an error access
     */
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

    // TODO sync request, change that

    /**
     * Send a HTTP request to an URL
     * @param targetURL some url
     * @param data data to add
     * @return the response
     */
    public String request(String targetURL, String requestMethod, String contentType, String data) {
        String res = null;
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
            connection.setRequestProperty("Content-Language", "fr-FR");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            res = response.toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
