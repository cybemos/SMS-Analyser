package com.cybemos.analyser;

/**
 * All constants of the application
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public interface Constants {

    /**
     * index of the adress in the contact access cursor
     */
    int ADDRESS = 2;
    /**
     * index of the date in the contact access cursor
     */
    int DATE_SENT = 4;
    /**
     * index of the body of the message in the contact access cursor
     */
    int BODY = 12;

    /**
     * Extra title : String
     */
    String EXTRA_TITLE = "com.cybemos.analyser.EXTRA_TITLE";
    /**
     * Extra analyser : {@link com.cybemos.analyser.data.analyser.IAnalyser}
     */
    String EXTRA_ANALYSER = "com.cybemos.analyser.EXTRA_ANALYSER";
    /**
     * Extra number : String
     */
    String EXTRA_NUMBER = "com.cybemos.analyser.EXTRA_NUMBER";
    /**
     * Extra name : String
     */
    String EXTRA_NAME = "com.cybemos.analyser.EXTRA_NAME";
    /**
     * Extra contact id : String
     */
    String EXTRA_CONTACT_ID = "com.cybemos.analyser.EXTRA_CONTACT_ID";
    /**
     * Extra files : {@link java.util.ArrayList<java.io.File>}
     */
    String EXTRA_FILES = "com.cybemos.analyser.EXTRA_FILES";
    /**
     * Extra selection mode : int
     */
    String EXTRA_SELECTION_MODE = "com.cybemos.analyser.EXTRA_SELECTION_MODE";
    /**
     * Extra menu shown : boolean
     */
    String EXTRA_MENU_SHOWN = "com.cybemos.analyser.EXTRA_MENU_SHOWN";

    /**
     * Directory in which the data will be exported
     */
    String DIRECTORY = "com.cybemos.analyser/";

    //String INBOX = "content://sms/inbox";
    //String SENT = "content://sms/sent";
    //String DRAFT = "content://sms/draft";

    /**
     * Id of the activity which select a contact
     */
    int ACTIVITY_SELECT_CONTACT = 1;
    /**
     * Id of the activity {@link com.cybemos.analyser.ui.activities.SettingsActivity}
     */
    int ACTIVITY_SETTINGS = 2;

}
