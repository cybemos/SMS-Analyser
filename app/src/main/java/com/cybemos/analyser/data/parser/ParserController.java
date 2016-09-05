package com.cybemos.analyser.data.parser;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.cybemos.analyser.R;
import com.cybemos.analyser.data.statistics.Statistics;
import com.cybemos.analyser.data.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class ParserController {

    private static ParserController instance;
    private final ArrayList<Parser> parsers;
    @StringRes
    private int lastErrorId;

    private ParserController() {
        parsers = new ArrayList<>();
        lastErrorId = -1;
    }

    public static ParserController getInstance() {
        if (instance == null) {
            instance = new ParserController();
        }
        return instance;
    }

    @NonNull
    public synchronized List<Extension> getExtensions() {
        List<Extension> list = new ArrayList<>();
        for (Parser parser : parsers) {
            list.addAll(parser.getExtensions());
        }
        return list;
    }

    public synchronized void addParser(Parser parser) {
        parsers.add(parser);
    }

    public synchronized void removeAllParsers() {
        parsers.clear();
    }

    public synchronized boolean save(@NonNull Statistics statistics, @NonNull File save) {
        String extension = Util.getExtension(save);
        boolean succeed = false;
        for (Parser parser : parsers) {
            if (!succeed && parser.canHandle(extension)) {
                succeed = parser.save(statistics, save);
            }
        }
        return succeed;
    }

    public synchronized Statistics load(@NonNull File file) {
        lastErrorId = -1;
        Statistics statistics = null;
        String extension = Util.getExtension(file);
        for (Parser parser : parsers) {
            if (statistics == null && parser.canHandle(extension)) {
                statistics = parser.load(file);
                if (statistics == null) {
                    lastErrorId = R.string.error_loading_file;
                }
            }
        }
        if (lastErrorId == -1 && statistics == null) {
            lastErrorId = R.string.error_format;
        }
        return statistics;
    }

    @StringRes
    public int getLastErrorId() {
        return lastErrorId;
    }

}
