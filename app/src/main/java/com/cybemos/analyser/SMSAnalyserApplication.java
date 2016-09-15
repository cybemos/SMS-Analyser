package com.cybemos.analyser;

import android.app.Application;

import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.parser.MyXMLParser;
import com.cybemos.analyser.data.parser.ParserController;
import com.cybemos.analyser.data.parser.TXTParser;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SMSAnalyserApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Util.context = this;
        ParserController controller = ParserController.getInstance();
        controller.removeAllParsers();
        controller.addParser(new MyXMLParser());
        controller.addParser(new TXTParser());
    }

}
