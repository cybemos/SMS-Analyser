package com.cybemos.analyser.data.statistics;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface Statistics extends Serializable {

    @NonNull String getName();
    @NonNull String getNumber();
    @NonNull Date getDate();
    @NonNull Date getFirstDateSMS(@NonNull Request request);
    int getNumberOfSMS(@NonNull Request request);
    double getAverageCharactersBySMS(@NonNull Request request);
    int getTotalWords(@NonNull Request request);
    int getTotalCharacters(@NonNull Request request);
    double getNumberOfSMSBy(@NonNull Request request, TIME time);

    enum Request {
        SENT,
        RECEIVED,
        ALL
    }

    enum TIME {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

}
