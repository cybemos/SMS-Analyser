package com.cybemos.analyser.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.analyser.ContactAnalyser;
import com.cybemos.analyser.data.analyser.IAnalyser;
import com.cybemos.analyser.ui.adapters.ResultAdapter;
import com.cybemos.analyser.data.analyser.AnalyseTask;
import com.cybemos.analyser.ui.dialogs.SaveFileDialog;
import com.cybemos.uilibrary.activities.AbstractChildActivity;

public class ResultActivity extends AbstractChildActivity {

    private static final String TAG = "ResultActivity";
    private ResultAdapter adapter;
    private IAnalyser analyser;
    private boolean jobFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.refresh);
        GridView lv = (GridView) findViewById(R.id.gridView);

        adapter = new ResultAdapter(this);
        lv.setAdapter(adapter);
        if (savedInstanceState != null) { // saved instance
            analyser = (IAnalyser)  savedInstanceState.get(Constants.EXTRA_ANALYSER);
            setTitle(savedInstanceState.getString(Constants.EXTRA_TITLE));
            adapter.set(analyser);
            adapter.notifyDataSetChanged();
            // if cancelled before job finished
            if (!analyser.jobFinished()) {
                reload(swipe);
            }
        } else { // new instance
            Bundle b = getIntent().getExtras();
            analyser = (b != null) ? (IAnalyser) b.getSerializable(Constants.EXTRA_ANALYSER) : null;
            if (analyser != null) {
                setTitle(getString(R.string.title_activity_result_contact, analyser.getName()));
            } else {
                String contact_id = (b != null) ? b.getString(Constants.EXTRA_CONTACT_ID, null) : null;
                String number = (b != null) ? b.getString(Constants.EXTRA_NUMBER, "No number") : "No number";
                String name = (b != null) ? b.getString(Constants.EXTRA_NAME, getString(R.string.all)) : getString(R.string.all);
                if (contact_id != null) {
                    this.setTitle(getString(R.string.title_activity_result_contact, name));
                    analyser = new ContactAnalyser(name, number);
                } else {
                    analyser = new ContactAnalyser(this);
                }
            }
            reload(swipe);
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload(swipe);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFileDialog dialog = new SaveFileDialog();
                Bundle b = new Bundle();
                b.putSerializable(Constants.EXTRA_ANALYSER, analyser);
                dialog.setArguments(b);
                dialog.show(getFragmentManager(), SaveFileDialog.TAG);
            }
        });
    }

    @Override
    protected void onPause() {
        if (!analyser.jobFinished()) {
            analyser.clear();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!analyser.jobFinished()) {
            SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.refresh);
            reload(swipe);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.EXTRA_TITLE, getTitle().toString());
        if (adapter.isWaiting()) {
            analyser.clear();
        }
        bundle.putSerializable(Constants.EXTRA_ANALYSER, analyser);
    }

    private void reload(final SwipeRefreshLayout swipe) {
        jobFinished = false;
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                if (jobFinished) {
                    swipe.setRefreshing(false);
                }
            }
        });

        AnalyseTask task = new AnalyseTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                adapter.setState(ResultAdapter.State.WAITING);
                adapter.notifyDataSetInvalidated();

            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                adapter.set(analyser);
                boolean finished = analyser.jobFinished();
                if (!finished) {
                    adapter.setState(ResultAdapter.State.WAITING);
                } else {
                    if (!analyser.hasError()) {
                        adapter.setState(ResultAdapter.State.OK);
                    } else {
                        adapter.setState(ResultAdapter.State.ERROR);
                    }
                }
                adapter.notifyDataSetChanged();
                jobFinished = true;
                swipe.setRefreshing(false);
                @StringRes int errorId;
                if (analyser.hasError() && (errorId = analyser.getLastError()) != -1) {
                    View view = findViewById(R.id.activity_result);
                    Snackbar.make(view, errorId, Snackbar.LENGTH_LONG)
                            .setAction(R.string.reload, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.refresh);
                                    reload(swipe);
                                }
                            })
                            .show();
                }
            }
        };
        task.execute(analyser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, Constants.ACTIVITY_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.ACTIVITY_SETTINGS:
                adapter.updateFormatValues();
                adapter.notifyDataSetChanged();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
