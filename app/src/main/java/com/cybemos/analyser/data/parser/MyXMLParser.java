package com.cybemos.analyser.data.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.statistics.ConcreteStatistics;
import com.cybemos.analyser.data.statistics.Statistics;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class MyXMLParser implements Parser {

    private static final String TAG = "MyXMLParser";

    private static final String EXTENSION = "xml";

    private static final String ATTRIBUTE_VALUE = "value";

    private static final String TAG_GENERAL = "statistics";

    public MyXMLParser() {}

    @NonNull
    @Override
    public List<Extension> getExtensions() {
        List<Extension> list = new ArrayList<>();
        list.add(new Extension(EXTENSION, R.string.format_xml));
        return list;
    }

    @Override
    public boolean canHandle(@Nullable String extension) {
        return EXTENSION.equals(extension);
    }

    @Override
    public boolean save(@NonNull Statistics statistics, @NonNull File save) {
        boolean succeed = true;
        XmlSerializer serializer = Xml.newSerializer();
        try {
            if (!save.exists()) {
                //noinspection ResultOfMethodCallIgnored
                save.createNewFile();
            }
            if (!save.isFile()) {
                throw new IOException("it must be a file");
            }
            FileWriter writer = new FileWriter(save);
            serializer.setOutput(writer);
            //noinspection HardCodedStringLiteral
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", TAG_GENERAL);

            serializer.startTag("", TAG_NAME);
            serializer.attribute("", ATTRIBUTE_VALUE, statistics.getName());
            serializer.endTag("", TAG_NAME);

            serializer.startTag("", TAG_NUMBER);
            serializer.attribute("", ATTRIBUTE_VALUE, statistics.getNumber());
            serializer.endTag("", TAG_NUMBER);

            serializer.startTag("", TAG_DATE);
            serializer.attribute("", ATTRIBUTE_VALUE, Long.toString(statistics.getDate().getTime()));
            serializer.text(statistics.getDate().toString());
            serializer.endTag("", TAG_DATE);

            serializer.startTag("", TAG_FIRST_SMS_DATE);
            serializer.startTag("", TAG_RECEIVED);
            Date date = statistics.getFirstDateSMS(Statistics.Request.RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE, Long.toString(date.getTime()));
            serializer.text(date.toString());
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            date = statistics.getFirstDateSMS(Statistics.Request.SENT);
            serializer.attribute("", ATTRIBUTE_VALUE, Long.toString(date.getTime()));
            serializer.text(date.toString());
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_FIRST_SMS_DATE);

            serializer.startTag("", TAG_NUMBER_OF_SMS);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getNumberOfSMS(Statistics.Request.RECEIVED)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getNumberOfSMS(Statistics.Request.SENT)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_NUMBER_OF_SMS);

            serializer.startTag("", TAG_AVERAGE_CHARACTERS_BY_SMS);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Double.toString(statistics.getAverageCharactersBySMS(Statistics.Request.RECEIVED)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Double.toString(statistics.getAverageCharactersBySMS(Statistics.Request.SENT)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_AVERAGE_CHARACTERS_BY_SMS);

            serializer.startTag("", TAG_TOTAL_WORDS);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getTotalWords(Statistics.Request.RECEIVED)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getTotalWords(Statistics.Request.SENT)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_TOTAL_WORDS);

            serializer.startTag("", TAG_TOTAL_CHARACTERS);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getTotalCharacters(Statistics.Request.RECEIVED)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE,
                    Integer.toString(statistics.getTotalCharacters(Statistics.Request.SENT)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_TOTAL_CHARACTERS);

            serializer.startTag("", TAG_SMS_BY_DAY);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.DAY)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.DAY)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_SMS_BY_DAY);

            serializer.startTag("", TAG_SMS_BY_WEEK);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.WEEK)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.WEEK)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_SMS_BY_WEEK);

            serializer.startTag("", TAG_SMS_BY_MONTH);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.MONTH)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.MONTH)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_SMS_BY_MONTH);

            serializer.startTag("", TAG_SMS_BY_YEAR);
            serializer.startTag("", TAG_RECEIVED);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.RECEIVED, Statistics.TIME.YEAR)));
            serializer.endTag("", TAG_RECEIVED);
            serializer.startTag("", TAG_SENT);
            serializer.attribute("", ATTRIBUTE_VALUE, Double.toString(
                    statistics.getNumberOfSMSBy(Statistics.Request.SENT, Statistics.TIME.YEAR)));
            serializer.endTag("", TAG_SENT);
            serializer.endTag("", TAG_SMS_BY_YEAR);

            serializer.endTag("", TAG_GENERAL);
            serializer.endDocument();
            writer.close();

        } catch (Exception e) {
            //noinspection HardCodedStringLiteral
            Log.e(TAG, "Exception occurred in writing");
            succeed = false;
        }
        return succeed;
    }

    @Nullable
    @Override
    public Statistics load(@NonNull File file) {
        ConcreteStatistics statistics = null;
        XmlPullParserFactory xmlFactoryObject;
        XmlPullParser parser;
        int event;
        String name, value, tag = null;
        Date date;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            parser = xmlFactoryObject.newPullParser();
            InputStream stream = new FileInputStream(file);
            parser.setInput(stream, null);
            statistics = new ConcreteStatistics();
            event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case TAG_FIRST_SMS_DATE:
                            case TAG_NUMBER_OF_SMS:
                            case TAG_AVERAGE_CHARACTERS_BY_SMS:
                            case TAG_TOTAL_WORDS:
                            case TAG_TOTAL_CHARACTERS:
                            case TAG_SMS_BY_DAY:
                            case TAG_SMS_BY_WEEK:
                            case TAG_SMS_BY_MONTH:
                            case TAG_SMS_BY_YEAR:
                                tag = name;
                                break;
                            case TAG_NAME:
                                value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
                                statistics.setName(value);
                                break;
                            case TAG_NUMBER:
                                value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
                                statistics.setNumber(value);
                                break;
                            case TAG_DATE:
                                value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
                                try {
                                    date = new Date(Long.valueOf(value));
                                    statistics.setDate(date);
                                } catch (NumberFormatException e) {
                                    //noinspection HardCodedStringLiteral
                                    Log.e(TAG, "Can't get the long value of "+value);
                                }
                                break;
                            case TAG_RECEIVED:
                                value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
                                if (tag != null) {
                                    saveIn(statistics, tag, Statistics.Request.RECEIVED, value);
                                }
                                break;
                            case TAG_SENT:
                                value = parser.getAttributeValue(null, ATTRIBUTE_VALUE);
                                if (tag != null) {
                                    saveIn(statistics, tag, Statistics.Request.SENT, value);
                                }
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            stream.close();
            statistics.validate();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            //noinspection HardCodedStringLiteral
            Log.e(TAG, "stats not valid : "+e.getMessage());
            statistics = null;
        }
        return statistics;
    }

    private static void saveIn(@NonNull ConcreteStatistics statistics, @NonNull String tag,
                               @NonNull Statistics.Request request, @Nullable String value) {
        try {
            switch (tag) {
                case TAG_FIRST_SMS_DATE:
                    Date date = new Date(Long.valueOf(value));
                    statistics.setFirstDateSMS(request, date);
                    break;
                case TAG_NUMBER_OF_SMS:
                    statistics.setNumberOfSMS(request, Integer.valueOf(value));
                    break;
                case TAG_AVERAGE_CHARACTERS_BY_SMS:
                    statistics.setAverageCharactersBySMS(request, Double.valueOf(value));
                    break;
                case TAG_TOTAL_WORDS:
                    statistics.setTotalWords(request, Integer.valueOf(value));
                    break;
                case TAG_TOTAL_CHARACTERS:
                    statistics.setTotalCharacters(request, Integer.valueOf(value));
                    break;
                case TAG_SMS_BY_DAY:
                    statistics.setNumberOfSMSBy(request, Statistics.TIME.DAY, Double.valueOf(value));
                    break;
                case TAG_SMS_BY_WEEK:
                    statistics.setNumberOfSMSBy(request, Statistics.TIME.WEEK, Double.valueOf(value));
                    break;
                case TAG_SMS_BY_MONTH:
                    statistics.setNumberOfSMSBy(request, Statistics.TIME.MONTH, Double.valueOf(value));
                    break;
                case TAG_SMS_BY_YEAR:
                    statistics.setNumberOfSMSBy(request, Statistics.TIME.YEAR, Double.valueOf(value));
                    break;
            }
        } catch (NumberFormatException e) {
            //noinspection HardCodedStringLiteral
            Log.e(TAG, "Can't get the value of " + value+" : "+e.getMessage());
        }
    }
}
