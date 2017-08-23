package com.cybemos.analyser.data;

import java.io.Serializable;

/**
 * Represents a SMS
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SMS implements Serializable {

    /**
     * Usefull to save the object
     */
    private final static long serialVersionUID = 0L;
    /**
     * the sender of the SMS
     */
    private final String from;
    /**
     * the receiver of the SMS
     */
    private final String to;
    /**
     * the body of the SMS
     */
    private final String body;
    /**
     * the send date of the SMS
     */
    private final long date;

    /**
     * Initialize the class with all paramters.
     * @param from the sender of the SMS
     * @param to the receiver of the SMS
     * @param body the body of the SMS
     * @param date the send date of the SMS
     */
    public SMS(String from, String to, String body, String date) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.date = Long.valueOf(date);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "sms from ["+from+"] to ["+to+"] : "+body;
    }

}
