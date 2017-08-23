package com.cybemos.analyser.data.statistics;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class ConcreteStatistics implements Statistics {

    private final static long serialVersionUID = 0L;

    private static final int INDEX_RECEIVED = 0;
    private static final int INDEX_SENT = 1;

    private static final int INDEX_DAY = 0;
    private static final int INDEX_WEEK = 1;
    private static final int INDEX_MONTH = 2;
    private static final int INDEX_YEAR = 3;


    private static final int INDEX_MAX_RS = Math.max(INDEX_RECEIVED, INDEX_SENT) + 1;
    private static final int INDEX_MAX_TIME = Math.max(INDEX_DAY,
            Math.max(INDEX_WEEK,
                    Math.max(INDEX_MONTH, INDEX_YEAR))) + 1;

    private String name;
    private String number;
    private Date date;
    private final Date[] firstDateSMS;
    private final Integer[] numberOfSMS;
    private final Double[] averageCharactersBySMS;
    private final Integer[] totalWords;
    private final Integer[] totalCharacters;
    private final Double[][] numberOfSMSBy;

    public ConcreteStatistics() {
        name = number = "";
        firstDateSMS = new Date[INDEX_MAX_RS];
        numberOfSMS = new Integer[INDEX_MAX_RS];
        averageCharactersBySMS = new Double[INDEX_MAX_RS];
        totalWords = new Integer[INDEX_MAX_RS];
        totalCharacters = new Integer[INDEX_MAX_RS];
        numberOfSMSBy = new Double[INDEX_MAX_RS][INDEX_MAX_TIME];
    }

    /**
     * Update the name
     * @param name some name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Update the other phone number
     * @param number the other phone number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Update the date where those statistics were made
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Update the date of the first sms sent/received
     * @param request SENT or RECEIVED
     * @param firstDateSMS some date
     */
    public void setFirstDateSMS(@NonNull Request request, @NonNull Date firstDateSMS) {
        switch (request) {
            case RECEIVED:
                this.firstDateSMS[INDEX_RECEIVED] = firstDateSMS;
                break;
            case SENT:
                this.firstDateSMS[INDEX_SENT] = firstDateSMS;
                break;
        }
    }

    /**
     * Update the number of sms sent/received
     * @param request SENT or RECEIVED
     */
    public void setNumberOfSMS(@NonNull Request request, int numberOfSMS) {
        switch (request) {
            case RECEIVED:
                this.numberOfSMS[INDEX_RECEIVED] = numberOfSMS;
                break;
            case SENT:
                this.numberOfSMS[INDEX_SENT] = numberOfSMS;
                break;
        }
    }

    /**
     * Update the average characters by sms sent/received
     * @param request SENT or RECEIVED
     */
    public void setAverageCharactersBySMS(@NonNull Request request, double averageCharactersBySMS) {
        switch (request) {
            case RECEIVED:
                this.averageCharactersBySMS[INDEX_RECEIVED] = averageCharactersBySMS;
                break;
            case SENT:
                this.averageCharactersBySMS[INDEX_SENT] = averageCharactersBySMS;
                break;
        }
    }

    /**
     * Update the total words sent/received
     * @param request SENT or RECEIVED
     */
    public void setTotalWords(@NonNull Request request, int totalWords) {
        switch (request) {
            case RECEIVED:
                this.totalWords[INDEX_RECEIVED] = totalWords;
                break;
            case SENT:
                this.totalWords[INDEX_SENT] = totalWords;
                break;
        }
    }

    /**
     * Update the total characters
     * @param request SENT or RECEIVED
     */
    public void setTotalCharacters(@NonNull Request request, int totalCharacters) {
        switch (request) {
            case RECEIVED:
                this.totalCharacters[INDEX_RECEIVED] = totalCharacters;
                break;
            case SENT:
                this.totalCharacters[INDEX_SENT] = totalCharacters;
                break;
        }
    }

    /**
     * Update the number of sms by time
     * @param request SENT or RECEIVED
     */
    public void setNumberOfSMSBy(@NonNull Request request, @NonNull TIME time, double numberOfSMSBy) {
        switch (request) {
            case RECEIVED:
                switch (time) {
                    case DAY:
                        this.numberOfSMSBy[INDEX_RECEIVED][INDEX_DAY] = numberOfSMSBy;
                        break;
                    case WEEK:
                        this.numberOfSMSBy[INDEX_RECEIVED][INDEX_WEEK] = numberOfSMSBy;
                        break;
                    case MONTH:
                        this.numberOfSMSBy[INDEX_RECEIVED][INDEX_MONTH] = numberOfSMSBy;
                        break;
                    case YEAR:
                        this.numberOfSMSBy[INDEX_RECEIVED][INDEX_YEAR] = numberOfSMSBy;
                        break;
                }
                break;
            case SENT:
                switch (time) {
                    case DAY:
                        this.numberOfSMSBy[INDEX_SENT][INDEX_DAY] = numberOfSMSBy;
                        break;
                    case WEEK:
                        this.numberOfSMSBy[INDEX_SENT][INDEX_WEEK] = numberOfSMSBy;
                        break;
                    case MONTH:
                        this.numberOfSMSBy[INDEX_SENT][INDEX_MONTH] = numberOfSMSBy;
                        break;
                    case YEAR:
                        this.numberOfSMSBy[INDEX_SENT][INDEX_YEAR] = numberOfSMSBy;
                        break;
                }
                break;
        }
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getNumber() {
        return number;
    }

    @NonNull
    @Override
    public Date getDate() {
        return date;
    }

    @NonNull
    @Override
    public Date getFirstDateSMS(@NonNull Request request) {
        Date date = null;
        switch (request) {
            case RECEIVED:
                date = firstDateSMS[INDEX_RECEIVED];
                break;
            case SENT:
                date = firstDateSMS[INDEX_SENT];
                break;
            case ALL:
                Date d1 = firstDateSMS[INDEX_RECEIVED];
                Date d2 = firstDateSMS[INDEX_SENT];
                date = d1.before(d2) ? d1 : d2;
                break;
        }
        return date;
    }

    @Override
    public int getNumberOfSMS(@NonNull Request request) {
        int number = 0;
        switch (request) {
            case RECEIVED:
                number = numberOfSMS[INDEX_RECEIVED];
                break;
            case SENT:
                number = numberOfSMS[INDEX_SENT];
                break;
            case ALL:
                number = numberOfSMS[INDEX_RECEIVED] + numberOfSMS[INDEX_SENT];
                break;
        }
        return number;
    }

    @Override
    public double getAverageCharactersBySMS(@NonNull Request request) {
        double number = 0;
        switch (request) {
            case RECEIVED:
                number = averageCharactersBySMS[INDEX_RECEIVED];
                break;
            case SENT:
                number = averageCharactersBySMS[INDEX_SENT];
                break;
            case ALL:
                // TODO not yet implemented
                //number = averageCharactersBySMS[INDEX_RECEIVED] + averageCharactersBySMS[INDEX_SENT];;
                break;
        }
        return number;
    }
    

    @Override
    public int getTotalWords(@NonNull Request request) {
        int number = 0;
        switch (request) {
            case RECEIVED:
                number = totalWords[INDEX_RECEIVED];
                break;
            case SENT:
                number = totalWords[INDEX_SENT];
                break;
            case ALL:
                number = totalWords[INDEX_RECEIVED] + totalWords[INDEX_SENT];
                break;
        }
        return number;
    }

    @Override
    public int getTotalCharacters(@NonNull Request request) {
        int number = 0;
        switch (request) {
            case RECEIVED:
                number = totalCharacters[INDEX_RECEIVED];
                break;
            case SENT:
                number = totalCharacters[INDEX_SENT];
                break;
            case ALL:
                number = totalCharacters[INDEX_RECEIVED] + totalCharacters[INDEX_SENT];
                break;
        }
        return number;
    }

    @Override
    public double getNumberOfSMSBy(@NonNull Request request, TIME time) {
        double number = 0;
        int index = -1;
        switch (request) {
            case RECEIVED:
                index = INDEX_RECEIVED;
                break;
            case SENT:
                index = INDEX_SENT;
                break;
        }
        if (index != -1) {
            switch (time) {
                case DAY:
                    number = numberOfSMSBy[index][INDEX_DAY];
                    break;
                case WEEK:
                    number = numberOfSMSBy[index][INDEX_WEEK];
                    break;
                case MONTH:
                    number = numberOfSMSBy[index][INDEX_MONTH];
                    break;
                case YEAR:
                    number = numberOfSMSBy[index][INDEX_YEAR];
                    break;
            }
        }
        return number;
    }

    public void validate() throws IllegalStateException {
        if (name == null) {
            throw new IllegalStateException("name null");
        }
        if (number == null) {
            throw new IllegalStateException("number null");
        }
        if (date == null) {
            throw new IllegalStateException("date null");
        }
        if (firstDateSMS[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("first date received null");
        }
        if (firstDateSMS[INDEX_SENT] == null) {
            throw new IllegalStateException("first date sent null");
        }

        if (numberOfSMS[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("number of sms received null");
        }
        if (numberOfSMS[INDEX_SENT] == null) {
            throw new IllegalStateException("number of sms sent null");
        }
        if (averageCharactersBySMS[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("average characters received null");
        }
        if (averageCharactersBySMS[INDEX_SENT] == null) {
            throw new IllegalStateException("average characters sent null");
        }
        if (totalWords[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("total words received null");
        }
        if (totalWords[INDEX_SENT] == null) {
            throw new IllegalStateException("total words sent null");
        }
        if (totalCharacters[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("total characters received null");
        }
        if (totalCharacters[INDEX_SENT] == null) {
            throw new IllegalStateException("total characters sent null");
        }
        if (numberOfSMSBy[INDEX_RECEIVED] == null) {
            throw new IllegalStateException("number of sms table received null");
        }
        if (numberOfSMSBy[INDEX_SENT] == null) {
            throw new IllegalStateException("number of sms table sent null");
        }
        if (numberOfSMSBy[INDEX_RECEIVED][INDEX_DAY] == null) {
            throw new IllegalStateException("number of sms by day received null");
        }
        if (numberOfSMSBy[INDEX_RECEIVED][INDEX_WEEK] == null) {
            throw new IllegalStateException("number of sms by week received null");
        }
        if (numberOfSMSBy[INDEX_RECEIVED][INDEX_MONTH] == null) {
            throw new IllegalStateException("number of sms by month received null");
        }
        if (numberOfSMSBy[INDEX_RECEIVED][INDEX_YEAR] == null) {
            throw new IllegalStateException("number of sms by year received null");
        }
        if (numberOfSMSBy[INDEX_SENT][INDEX_DAY] == null) {
            throw new IllegalStateException("number of sms by day sent null");
        }
        if (numberOfSMSBy[INDEX_SENT][INDEX_WEEK] == null) {
            throw new IllegalStateException("number of sms by week sent null");
        }
        if (numberOfSMSBy[INDEX_SENT][INDEX_MONTH] == null) {
            throw new IllegalStateException("number of sms by month sent null");
        }
        if (numberOfSMSBy[INDEX_SENT][INDEX_YEAR] == null) {
            throw new IllegalStateException("number of sms by year sent null");
        }
    }

}
