package com.cybemos.analyser.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.analyser.IAnalyser;
import com.cybemos.analyser.data.statistics.Statistics;
import com.cybemos.analyser.data.parser.Extension;
import com.cybemos.analyser.data.parser.ParserController;
import com.cybemos.analyser.ui.adapters.SimpleAdapter;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class SaveFileDialog extends DialogFragment {

    public final static String TAG = "SaveFileDialog";
    private IAnalyser analyser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { // saved instance
            analyser = (IAnalyser) savedInstanceState.getSerializable(Constants.EXTRA_ANALYSER);
        }/* else { // new instance

        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        // save some attributes
        bundle.putSerializable(Constants.EXTRA_ANALYSER, analyser);
    }

    @Override
    public void setArguments(Bundle bundle) {
        // init attributes
        analyser = (IAnalyser) bundle.getSerializable(Constants.EXTRA_ANALYSER);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final View v = activity.findViewById(R.id.activity_result);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        //noinspection HardCodedStringLiteral
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_save_file, null);
        // init the view
        final EditText tv = (EditText) view.findViewById(R.id.editText);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        final ParserController controller = ParserController.getInstance();
        List<Extension> extensions = controller.getExtensions();
        SimpleAdapter<Extension> adapter = new SimpleAdapter<>(activity, extensions);
        spinner.setAdapter(adapter);
        Extension extension = Util.getSettings().getDefaultExtension();
        int i = 0;
        for (Extension e : extensions) {
            if (e.equals(extension)) {
                spinner.setSelection(i);
            }
            i++;
        }
        builder.setView(view)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File outputDir = Environment.getExternalStorageDirectory();
                        outputDir = new File(outputDir, Constants.DIRECTORY + analyser.getName());
                        if (outputDir.mkdirs()) {
                            //noinspection HardCodedStringLiteral
                            Log.i(TAG, "directory "+outputDir+" created");
                        } else {
                            //noinspection HardCodedStringLiteral
                            Log.i(TAG, "directory "+outputDir+" already exist");
                        }
                        Extension extension = (Extension) spinner.getSelectedItem();
                        final File outputFile = new File(outputDir, tv.getText()+"."+extension.getExtension());
                        Statistics statistics = analyser.toStatistics();
                        if (statistics != null) {
                            if (controller.save(statistics, outputFile)) {
                                Snackbar.make(v, R.string.file_saved, Snackbar.LENGTH_LONG)
                                        .setAction(R.string.share, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                                //emailIntent.setType("text/plain");
                                                //noinspection HardCodedStringLiteral
                                                emailIntent.setType("image/png");
                                                //emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                                //        activity.getString(R.string.app_name));
                                                Uri path = Uri.fromFile(outputFile);
                                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                                activity.startActivity(
                                                        Intent.createChooser(emailIntent,
                                                                activity.getString(R.string.share))
                                                );
                                            }
                                        })
                                        .show();
                            } else {
                                Snackbar.make(v, R.string.file_not_saved, Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(v, R.string.file_not_saved, Snackbar.LENGTH_LONG).show();
                        }
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
        builder.setTitle(R.string.export);
        return builder.create();
    }

}
