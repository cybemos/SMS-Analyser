package com.cybemos.analyser.data.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cybemos.analyser.data.statistics.Statistics;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface Parser {

    /**
     * @return all extensions handled by this parser
     */
    @NonNull List<Extension> getExtensions();

    /**
     * @param extension some extension or null
     * @return true if the parser can handle the extension else false
     */
    boolean canHandle(@Nullable String extension);

    /**
     * save into a file statistics
     * @return true if succeed else false
     */
    boolean save(@NonNull Statistics statistics, @NonNull File save);

    /**
     * Load statistics from a file
     * @param file input file
     * @return the statistics loaded or null if failed
     */
    @Nullable Statistics load(@NonNull File file);

    String TAG_RECEIVED = "received";
    String TAG_SENT = "sent";

    String TAG_NAME = "name";
    String TAG_NUMBER = "number";
    String TAG_DATE = "date";

    String TAG_FIRST_SMS_DATE = "date_first_sms";
    String TAG_NUMBER_OF_SMS = "number_of_sms";
    String TAG_AVERAGE_CHARACTERS_BY_SMS = "average_characters_by_sms";
    String TAG_TOTAL_WORDS = "total_words";
    String TAG_TOTAL_CHARACTERS = "total_characters";
    String TAG_SMS_BY_DAY = "sms_by_day";
    String TAG_SMS_BY_WEEK = "sms_by_week";
    String TAG_SMS_BY_MONTH = "sms_by_month";
    String TAG_SMS_BY_YEAR = "sms_by_year";

}
