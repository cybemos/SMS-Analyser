package com.cybemos.analyser.ui.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.exceptions.FileAccessException;
import com.cybemos.analyser.data.analyser.FileAnalyser;
import com.cybemos.analyser.ui.activities.ResultActivity;
import com.cybemos.uilibrary.adapters.SelectableItemsAdapter;

/**
 * @version 1.0
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 */

public class FileAdapter extends SelectableItemsAdapter {

    private final File baseDirectory;
    private File directory;
    private File[] sons;
    private final Activity activity;

    public FileAdapter(@NonNull Activity activity, @NonNull File directory) throws FileAccessException {
        super(R.id.checkBox);
        this.activity = activity;
        baseDirectory = this.directory = directory;
        sons = directory.listFiles();
        if (sons == null) {
            throw new FileAccessException();
        }
        activity.setTitle(directory.getName());
    }

    @NonNull
    public ArrayList<File> getSelectedFiles() {
        ArrayList<File> files = new ArrayList<>();
        for (Integer position : mCheckedItems) {
            files.add(sons[position]);
        }
        return files;
    }

    public void setSelectedFiles(List<File> files) {
        int index;
        if (files != null) {
            for (File f : files) {
                if (f != null) {
                    index = 0;
                    for (File son : sons) {
                        if (f.equals(son)) {
                            addSelectedItem(index);
                        }
                        index++;
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return sons.length;
    }

    @Override
    public Object getItem(int position) {
        return sons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getViewAt(int position, View convertView, ViewGroup parent) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(activity);
        if(convertView == null) {
            view = inflater.inflate(R.layout.item_file, parent, false);
        } else {
            view = convertView;
        }

        File f = sons[position];

        TextView tv = (TextView) view.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);


        String str = (f != null) ? f.getName() : "null";
        tv.setText(str);
        if (f != null && f.isDirectory()) {
            imageView.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            imageView.setImageDrawable(null);
        }
        setClickListener(new ClickListener() {
            @Override
            public void onClick(View view, int index) {
                try {
                    clickOn(index);
                } catch (FileAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void clickOn(int position) throws FileAccessException {
        if (position >= 0 || position < sons.length) {
            File f = sons[position];
            if (f != null) {
                if (f.isDirectory()) {
                    directory = f;
                    sons = directory.listFiles();
                    if (sons == null) {
                        throw new FileAccessException();
                    }
                    notifyDataSetInvalidated();
                    activity.setTitle(directory.getName());
                } else if (f.isFile()) {
                    FileAnalyser analyser = new FileAnalyser(f);
                    Intent intent = new Intent(activity, ResultActivity.class);
                    intent.putExtra(Constants.EXTRA_ANALYSER, analyser);
                    activity.startActivity(intent);
                }
            }
        }
    }

    public void deleteSelectedFiles() {
        for (Integer position : mCheckedItems) {
            deleteFile(sons[position]);
        }
        sons = directory.listFiles();
        setMode(MODE.MODE_BASE);
        notifyDataSetInvalidated();
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File son : files) {
                        deleteFile(son);
                    }
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            } else if (file.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    @SuppressWarnings("unused")
    public void moveToHome() {
        directory = baseDirectory;
        sons = directory.listFiles();
        notifyDataSetInvalidated();
        activity.setTitle(directory.getName());
    }

    public boolean moveUp() {
        boolean succeed = false;
        if (!baseDirectory.equals(directory)) {
            directory = directory.getParentFile();
            sons = directory.listFiles();
            succeed = true;
            notifyDataSetInvalidated();
            activity.setTitle(directory.getName());
        }
        return succeed;
    }

}
