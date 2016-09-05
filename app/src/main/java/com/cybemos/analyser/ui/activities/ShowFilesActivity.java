package com.cybemos.analyser.ui.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.exceptions.FileAccessException;
import com.cybemos.analyser.ui.adapters.FileAdapter;
import com.cybemos.analyser.ui.dialogs.DialogConfirmDeleteFiles;
import com.cybemos.uilibrary.adapters.SelectableItemsAdapter;
import com.cybemos.uilibrary.activities.AbstractChildActivity;

import java.io.File;
import java.util.ArrayList;

public class ShowFilesActivity extends AbstractChildActivity implements SelectableItemsAdapter.OnSelectionChangeListener {

    private FileAdapter adapter;
    private Menu mMenu;
    private int mBaseNumberOfItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        File outputDir = Environment.getExternalStorageDirectory();
        outputDir = new File(outputDir, Constants.DIRECTORY);

        ListView lv = (ListView) findViewById(R.id.listView2);
        View emptyView = findViewById(R.id.empty_view);
        lv.setEmptyView(emptyView);
        try {
            adapter = new FileAdapter(this, outputDir);
        if (savedInstanceState != null) { // saved instance
            int m = savedInstanceState.getInt(Constants.EXTRA_SELECTION_MODE);
            adapter.setMode(SelectableItemsAdapter.MODE.getMode(m));
            //noinspection unchecked
            ArrayList<File> selectedFiles = (ArrayList<File>) savedInstanceState.getSerializable(Constants.EXTRA_FILES);
            adapter.setSelectedFiles(selectedFiles);
        }
        adapter.setSelectionChangeListener(this);
        lv.setAdapter(adapter);
        } catch (FileAccessException e) {
            View view = findViewById(R.id.activity_show_files);
            Snackbar.make(view, R.string.cant_show_files, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(Constants.EXTRA_SELECTION_MODE, SelectableItemsAdapter.MODE.getValue(adapter.getMode()));
        ArrayList<File> selectedFiles = adapter.getSelectedFiles();
        bundle.putSerializable(Constants.EXTRA_FILES, selectedFiles);
    }

    @Override
    public void onBackPressed() {
        if (adapter.getMode() == SelectableItemsAdapter.MODE.MODE_SELECTION) {
            adapter.setMode(SelectableItemsAdapter.MODE.MODE_BASE);
            adapter.notifyDataSetInvalidated();
        } else if (!adapter.moveUp()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (adapter == null) {
                finish();
            } else if (adapter.getMode() == SelectableItemsAdapter.MODE.MODE_SELECTION) {
                adapter.setMode(SelectableItemsAdapter.MODE.MODE_BASE);
                adapter.notifyDataSetInvalidated();
            } else if (!adapter.moveUp()) {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_files, menu);
        mMenu = menu;
        mBaseNumberOfItems = mMenu.size();
        if (adapter != null && adapter.getMode() == SelectableItemsAdapter.MODE.MODE_SELECTION) {
            addDeleteButtonAtMenu();
        }
        return true;
    }

    @Override
    public void modeChanged(@NonNull SelectableItemsAdapter.MODE oldMode, @NonNull SelectableItemsAdapter.MODE newMode) {
        if (newMode == SelectableItemsAdapter.MODE.MODE_SELECTION) {
            addDeleteButtonAtMenu();
        } else {
            mMenu.removeItem(mBaseNumberOfItems);
        }
    }

    @Override
    public void selectionChanged(int nbSelected) {
        if (mMenu.size() > 0) {
            MenuItem item = mMenu.getItem(0);
            boolean enabled = nbSelected > 0;
            if (enabled) {
                item.setIcon(R.drawable.ic_delete_white_24dp);
            } else {
                item.setIcon(R.drawable.ic_delete_gray_24dp);
            }
            item.setEnabled(enabled);
        }
    }

    public void deleteSelectedFiles() {
        if (adapter != null) {
            adapter.deleteSelectedFiles();
        }
    }

    private void addDeleteButtonAtMenu() {
        MenuItem item = mMenu.add(R.string.delete);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setTitle(R.string.delete);
        if (adapter != null && adapter.getSelectedFiles().size() == mBaseNumberOfItems) {
            item.setIcon(R.drawable.ic_delete_gray_24dp);
            item.setEnabled(false);
        } else {
            item.setIcon(R.drawable.ic_delete_white_24dp);
        }
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ArrayList<File> files = adapter.getSelectedFiles();
                DialogConfirmDeleteFiles dialog = new DialogConfirmDeleteFiles();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.EXTRA_FILES, files);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), DialogConfirmDeleteFiles.TAG);
                return true;
            }
        });
    }

}
