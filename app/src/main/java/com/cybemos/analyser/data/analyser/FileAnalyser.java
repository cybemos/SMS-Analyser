package com.cybemos.analyser.data.analyser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.cybemos.analyser.data.parser.ParserController;
import com.cybemos.analyser.data.statistics.Statistics;

import java.io.File;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class FileAnalyser implements IAnalyser {

    private final static long serialVersionUID = 0L;
    private final File file;
    private Statistics statistics;
    private boolean finished;
    @StringRes
    private int lastErrorId;

    public FileAnalyser(@NonNull File file) {
        this.file = file;
        statistics = null;
        finished = false;
        lastErrorId = -1;
    }

    @Override
    public void doTheJob() {
        statistics = null;
        ParserController controller = ParserController.getInstance();
        statistics = controller.load(file);
        if (statistics == null) {
            lastErrorId = controller.getLastErrorId();
        } else {
            lastErrorId = -1;
        }
        finished = true;
    }

    @Override
    public String getName() {
        //return (statistics != null) ? statistics.getName() : file.getName();
        return file.getName();
    }

    @Override
    public void clear() {
        statistics = null;
        finished = false;
    }

    @Override
    public boolean jobFinished() {
        return finished;
    }

    @Override
    public boolean hasError() {
        return finished && statistics == null;
    }

    @StringRes
    @Override
    public int getLastError() {
        return lastErrorId;
    }

    @Nullable
    @Override
    public Statistics toStatistics() {
        return statistics;
    }
}
