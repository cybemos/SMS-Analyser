package com.cybemos.analyser.data.analyser;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.exceptions.ContactAccessException;
import com.cybemos.analyser.data.SMS;
import com.cybemos.analyser.data.exceptions.NumberAccessException;
import com.cybemos.analyser.data.statistics.Statistics;
import com.cybemos.analyser.data.Util;

import java.io.Serializable;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class AnalyserRequest implements Serializable {


    private static final String TAG = "AnalyserRequest";

    private static final String INBOX = "content://sms/inbox";
    private static final String SENT = "content://sms/sent";

    private final static long serialVersionUID = 0L;
    private final Statistics.Request request;
    private final String whereCond;
    private final String[] whereArgs;
    private SMSAnalyser analyser;
    private boolean clear;
    private boolean finished;
    @StringRes
    private int lastErrorId;

    AnalyserRequest(Statistics.Request request) {
        this(request, null, null);
    }

    AnalyserRequest(Statistics.Request request, @Nullable String whereCond, @Nullable String[] whereArgs) {
        this.request = request;
        this.whereArgs = whereArgs;
        this.whereCond = whereCond;
        finished = clear = false;
        lastErrorId = -1;
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized SMSAnalyser getGeneratedAnalyser() {
        return (isFinished()) ? analyser : null;
    }

    public synchronized void clear() {
        if (analyser != null) {
            analyser = null;
            clear = false;
            finished = false;
        } else {
            clear = true;
            finished = false;
        }
    }

    /**
     * This method depends of the number of sms the device have and can take a lot of time.
     * If you want to cancel it during work, please call {@link #clear()}.
     */
    @SuppressWarnings("HardCodedStringLiteral")
    @WorkerThread
    public void generateAnalyser() {
        analyser = new SMSAnalyser();
        String uri;
        switch (request) {
            case SENT:
                uri = SENT;
                break;
            case RECEIVED:
                uri = INBOX;
                break;
            default:
                throw new IllegalStateException();
        }
        String myNumber;
        try {
            myNumber = Util.getMyNumber(Util.context);
        } catch (NumberAccessException e) {
            Log.e(TAG, "NumberAccessException occurred");
            myNumber = "this_phone";
        }
        Cursor cursor;
        try {
            cursor = getCursor(uri);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            String to, from;
            do {
                switch (request) {
                    case SENT:
                        from = myNumber;
                        to = cursor.getString(Constants.ADDRESS);
                        break;
                    case RECEIVED:
                        from = cursor.getString(Constants.ADDRESS);
                        to = myNumber;
                        break;
                    default:
                        throw new IllegalStateException();
                }
                SMS sms = new SMS(from, to,
                        cursor.getString(Constants.BODY), cursor.getString(Constants.DATE_SENT));
                synchronized (this) {
                    if (analyser != null) {
                        analyser.add(sms);
                    }
                }
            } while (analyser != null && cursor.moveToNext());
        }
        cursor.close();
        synchronized (this) {
            finished = true;
            if (clear) {
                clear();
            }
        }

            lastErrorId = -1;
        } catch (ContactAccessException e) {
            Log.e(TAG, "ContactAccessException occurred");
            finished = true;
            analyser = null;
            lastErrorId = R.string.error_analyse_contact;
        }
    }

    @NonNull
    private Cursor getCursor(String uri) throws ContactAccessException {
        Cursor cursor;
        try {
            cursor = Util.context.getContentResolver().query(Uri.parse(uri), null, whereCond, whereArgs, null);
        } catch (SecurityException e) {
            throw new ContactAccessException();
        }
        assert cursor != null;
        return cursor;
    }

    @StringRes
    public int getLastErrorId() {
        return lastErrorId;
    }

}
