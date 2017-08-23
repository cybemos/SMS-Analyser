package com.cybemos.analyser.data.analyser;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;

/**
 * Launch the task of some analysers
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class AnalyseTask extends AsyncTask<IAnalyser, Long, Boolean> {

    private static AnalyseTask instance;

    @Override
    @CallSuper
    protected void onPreExecute() {
        if (instance != null) {
            instance.cancel(true);
        }
        instance = this;
    }

    @Override
    @CallSuper
    protected void onPostExecute(Boolean result) {
        instance = null;
    }

    @Override
    protected Boolean doInBackground(IAnalyser... params) {
        boolean succeed = false;
        for (IAnalyser request : params) {
            if (isCancelled()) break;
            request.doTheJob();
            succeed = !request.hasError();
        }
        return succeed;
    }

}
