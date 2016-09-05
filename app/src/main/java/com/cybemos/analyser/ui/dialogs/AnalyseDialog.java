package com.cybemos.analyser.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cybemos.analyser.R;
import com.cybemos.analyser.ui.activities.MainActivity;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class AnalyseDialog extends DialogFragment {

    public final static String TAG = "AnalyseDialog";

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { // saved instance

        } else { // new instance

        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        // save some attributes
    }

    @Override
    public void setArguments(Bundle bundle) {
        // init attributes
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        //noinspection HardCodedStringLiteral
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_analyse, null);
        // init the view
        final MainActivity mainActivity = (MainActivity) activity;

        builder.setView(view)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
        builder.setTitle(R.string.analyse);

        final Dialog dialog = builder.create();

        Button buttonAll = (Button) view.findViewById(R.id.button_all);
        Button buttonSelectContact = (Button) view.findViewById(R.id.button_select_contact);

        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.launchAnalyse(true);
                dialog.dismiss();
            }
        });
        buttonSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.launchAnalyse(false);
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
