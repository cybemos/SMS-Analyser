package com.cybemos.analyser.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.ui.activities.ShowFilesActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public class DialogConfirmDeleteFiles extends DialogFragment {

    public static final String TAG = "DialogConfirmDeleteFiles";
    private ArrayList<File> files;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { // saved instance
            //noinspection unchecked
            files = (ArrayList<File>) savedInstanceState.getSerializable(Constants.EXTRA_FILES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        // save some attributes
        bundle.putSerializable(Constants.EXTRA_FILES, files);
    }

    @Override
    public void setArguments(Bundle bundle) {
        // init attributes
        //noinspection unchecked
        files = (ArrayList<File>) bundle.getSerializable(Constants.EXTRA_FILES);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ShowFilesActivity activity = (ShowFilesActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        Resources res = getResources();
        int number = files.size();
        String title, content;
        if (number == 1) {
            File file = files.get(0);
            title = res.getQuantityString(R.plurals.delete_file_confirm_title, number, file.getName());
            content = res.getQuantityString(R.plurals.delete_file_confirm, number, file.getName());
        } else {
            title = res.getQuantityString(R.plurals.delete_file_confirm_title, number, number);
            content = res.getQuantityString(R.plurals.delete_file_confirm, number, number);
        }


        builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShowFilesActivity activity = (ShowFilesActivity) getActivity();
                        activity.deleteSelectedFiles();
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

}
