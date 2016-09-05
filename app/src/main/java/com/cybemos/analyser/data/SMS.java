package com.cybemos.analyser.data;

import java.io.Serializable;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SMS implements Serializable {

    private final static long serialVersionUID = 0L;
    private final String from;
    private final String to;
    private final String body;
    private final long date;

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

    @SuppressWarnings("All")
    @Override
    public String toString() {
        return "sms from ["+from+"] to ["+to+"] : "+body;
    }

}
