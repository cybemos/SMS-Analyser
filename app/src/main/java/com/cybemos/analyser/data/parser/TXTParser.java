package com.cybemos.analyser.data.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.statistics.ConcreteStatistics;
import com.cybemos.analyser.data.statistics.Statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class TXTParser implements Parser {

    private static final String TAG = "TXTParser";

    private static final String EXTENSION = "txt";

    private static final String TAG_FIRST_SMS_DATE_RECEIVED = TAG_FIRST_SMS_DATE+':'+TAG_RECEIVED;
    private static final String TAG_FIRST_SMS_DATE_SENT = TAG_FIRST_SMS_DATE+':'+TAG_SENT;
    private static final String TAG_NUMBER_OF_SMS_RECEIVED = TAG_NUMBER_OF_SMS+':'+TAG_RECEIVED;
    private static final String TAG_NUMBER_OF_SMS_SENT = TAG_NUMBER_OF_SMS+':'+TAG_SENT;
    private static final String TAG_AVERAGE_CHARACTERS_BY_SMS_RECEIVED = TAG_AVERAGE_CHARACTERS_BY_SMS+':'+TAG_RECEIVED;
    private static final String TAG_AVERAGE_CHARACTERS_BY_SMS_SENT = TAG_AVERAGE_CHARACTERS_BY_SMS+':'+TAG_SENT;
    private static final String TAG_TOTAL_WORDS_RECEIVED = TAG_TOTAL_WORDS+':'+TAG_RECEIVED;
    private static final String TAG_TOTAL_WORDS_SENT = TAG_TOTAL_WORDS+':'+TAG_SENT;
    private static final String TAG_TOTAL_CHARACTERS_RECEIVED = TAG_TOTAL_CHARACTERS+':'+TAG_RECEIVED;
    private static final String TAG_TOTAL_CHARACTERS_SENT = TAG_TOTAL_CHARACTERS+':'+TAG_SENT;
    private static final String TAG_SMS_BY_DAY_RECEIVED = TAG_SMS_BY_DAY+':'+TAG_RECEIVED;
    private static final String TAG_SMS_BY_DAY_SENT = TAG_SMS_BY_DAY+':'+TAG_SENT;
    private static final String TAG_SMS_BY_WEEK_RECEIVED = TAG_SMS_BY_WEEK+':'+TAG_RECEIVED;
    private static final String TAG_SMS_BY_WEEK_SENT = TAG_SMS_BY_WEEK+':'+TAG_SENT;
    private static final String TAG_SMS_BY_MONTH_RECEIVED = TAG_SMS_BY_MONTH+':'+TAG_RECEIVED;
    private static final String TAG_SMS_BY_MONTH_SENT = TAG_SMS_BY_MONTH+':'+TAG_SENT;
    private static final String TAG_SMS_BY_YEAR_RECEIVED = TAG_SMS_BY_YEAR+':'+TAG_RECEIVED;
    private static final String TAG_SMS_BY_YEAR_SENT = TAG_SMS_BY_YEAR+':'+TAG_SENT;

    @NonNull
    @Override
    public List<Extension> getExtensions() {
        List<Extension> list = new ArrayList<>();
        list.add(new Extension(EXTENSION, R.string.format_txt));
        return list;
    }

    @Override
    public boolean canHandle(@Nullable String extension) {
        return EXTENSION.equals(extension);
    }

    @Override
    public boolean save(@NonNull Statistics statistics, @NonNull File save) {
        boolean succeed;
        try {
            if (!save.exists()) {
                //noinspection ResultOfMethodCallIgnored
                save.createNewFile();
            }
            if (!save.isFile()) {
                throw new IOException("it must be a file");
            }
            FileWriter fileWriter = new FileWriter(save);

            fileWriter.write(TAG_NAME+'='+statistics.getName()+'\n');

            fileWriter.write(TAG_NUMBER+'='+statistics.getNumber()+'\n');

            fileWriter.write(TAG_DATE+'='+statistics.getDate().getTime()+'\n');

            fileWriter.write(TAG_FIRST_SMS_DATE_RECEIVED+'='
                    +statistics.getFirstDateSMS(Statistics.Request.RECEIVED).getTime()+'\n');
            fileWriter.write(TAG_FIRST_SMS_DATE_SENT+'='
                    +statistics.getFirstDateSMS(Statistics.Request.SENT).getTime()+'\n');

            fileWriter.write(TAG_NUMBER_OF_SMS_RECEIVED+'='
                    +statistics.getNumberOfSMS(Statistics.Request.RECEIVED)+'\n');
            fileWriter.write(TAG_NUMBER_OF_SMS_SENT+'='
                    +statistics.getNumberOfSMS(Statistics.Request.SENT)+'\n');

            fileWriter.write(TAG_AVERAGE_CHARACTERS_BY_SMS_RECEIVED+'='
                    +statistics.getAverageCharactersBySMS(Statistics.Request.RECEIVED)+'\n');
            fileWriter.write(TAG_AVERAGE_CHARACTERS_BY_SMS_SENT+'='
                    +statistics.getAverageCharactersBySMS(Statistics.Request.SENT)+'\n');

            fileWriter.write(TAG_TOTAL_WORDS_RECEIVED+'='
                    +statistics.getTotalWords(Statistics.Request.RECEIVED)+'\n');
            fileWriter.write(TAG_TOTAL_WORDS_SENT+'='
                    +statistics.getTotalWords(Statistics.Request.SENT)+'\n');

            fileWriter.write(TAG_TOTAL_CHARACTERS_RECEIVED+'='
                    +statistics.getTotalCharacters(Statistics.Request.RECEIVED)+'\n');
            fileWriter.write(TAG_TOTAL_CHARACTERS_SENT+'='
                    +statistics.getTotalCharacters(Statistics.Request.SENT)+'\n');

            fileWriter.write(TAG_SMS_BY_DAY_RECEIVED+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.DAY)+'\n');
            fileWriter.write(TAG_SMS_BY_DAY_SENT+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.DAY)+'\n');

            fileWriter.write(TAG_SMS_BY_WEEK_RECEIVED+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.WEEK)+'\n');
            fileWriter.write(TAG_SMS_BY_WEEK_SENT+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.WEEK)+'\n');

            fileWriter.write(TAG_SMS_BY_MONTH_RECEIVED+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.MONTH)+'\n');
            fileWriter.write(TAG_SMS_BY_MONTH_SENT+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.MONTH)+'\n');

            fileWriter.write(TAG_SMS_BY_YEAR_RECEIVED+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.YEAR)+'\n');
            fileWriter.write(TAG_SMS_BY_YEAR_SENT+'='
                    +statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.YEAR)+'\n');

            fileWriter.close();
            succeed = true;
        } catch (IOException e) {
            //noinspection HardCodedStringLiteral
            Log.e(TAG, "Exception occurred in writing");
            succeed = false;
        }
        return succeed;
    }

    @Nullable
    @Override
    public Statistics load(@NonNull File file) {
        ConcreteStatistics statistics;
        String line;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            statistics = new ConcreteStatistics();
            while ((line = br.readLine()) != null) {
                initValue(statistics, line);
            }
            fileReader.close();
            statistics.validate();
        } catch (IOException e) {
            //noinspection HardCodedStringLiteral
            Log.e(TAG, "Exception occurred in reading");
            statistics = null;
        } catch (IllegalStateException e) {
        //noinspection HardCodedStringLiteral
        Log.e(TAG, "stats not valid : "+e.getMessage());
        statistics = null;
    }
        return statistics;
    }

    private static void initValue(@NonNull ConcreteStatistics statistics, @NonNull String line) {
        String[] parts = line.split("=");
        if (parts.length == 2) {
            Date date;
            String name = parts[0];
            String value = parts[1];
            if (name != null && value != null) {
                try {
                    switch (name) {
                        case TAG_NAME:
                            statistics.setName(value);
                            break;
                        case TAG_DATE:
                            date = new Date(Long.valueOf(value));
                            statistics.setDate(date);
                            break;
                        case TAG_FIRST_SMS_DATE_RECEIVED:
                            date = new Date(Long.valueOf(value));
                            statistics.setFirstDateSMS(Statistics.Request.RECEIVED, date);
                            break;
                        case TAG_FIRST_SMS_DATE_SENT:
                            date = new Date(Long.valueOf(value));
                            statistics.setFirstDateSMS(Statistics.Request.SENT, date);
                            break;
                        case TAG_NUMBER_OF_SMS_RECEIVED:
                            statistics.setNumberOfSMS(Statistics.Request.RECEIVED, Integer.valueOf(value));
                            break;
                        case TAG_NUMBER_OF_SMS_SENT:
                            statistics.setNumberOfSMS(Statistics.Request.SENT, Integer.valueOf(value));
                            break;
                        case TAG_AVERAGE_CHARACTERS_BY_SMS_RECEIVED:
                            statistics.setAverageCharactersBySMS(Statistics.Request.RECEIVED, Double.valueOf(value));
                            break;
                        case TAG_AVERAGE_CHARACTERS_BY_SMS_SENT:
                            statistics.setAverageCharactersBySMS(Statistics.Request.SENT, Double.valueOf(value));
                            break;
                        case TAG_TOTAL_WORDS_RECEIVED:
                            statistics.setTotalWords(Statistics.Request.RECEIVED, Integer.valueOf(value));
                            break;
                        case TAG_TOTAL_WORDS_SENT:
                            statistics.setTotalWords(Statistics.Request.SENT, Integer.valueOf(value));
                            break;
                        case TAG_TOTAL_CHARACTERS_RECEIVED:
                            statistics.setTotalCharacters(Statistics.Request.RECEIVED, Integer.valueOf(value));
                            break;
                        case TAG_TOTAL_CHARACTERS_SENT:
                            statistics.setTotalCharacters(Statistics.Request.SENT, Integer.valueOf(value));
                            break;
                        case TAG_SMS_BY_DAY_RECEIVED:
                            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.DAY, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_DAY_SENT:
                            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.DAY, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_WEEK_RECEIVED:
                            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.WEEK, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_WEEK_SENT:
                            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.WEEK, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_MONTH_RECEIVED:
                            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.MONTH, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_MONTH_SENT:
                            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.MONTH, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_YEAR_RECEIVED:
                            statistics.setNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.YEAR, Double.valueOf(value));
                            break;
                        case TAG_SMS_BY_YEAR_SENT:
                            statistics.setNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.YEAR, Double.valueOf(value));
                            break;
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }
}
