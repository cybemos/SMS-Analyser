package com.cybemos.uilibrary.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Abstract Activity which show a Home button and handle the associed event.
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public abstract class AbstractChildActivity extends AppCompatActivity {

    /**
     * Show the Home button
     * Must be called in {@link #onCreate(Bundle)} method after some initialization by it's son.
     */
    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Handle the Home button event
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean consume;
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                consume = true;
                break;
            default:
                consume = super.onOptionsItemSelected(item);
                break;
        }
        return consume;
    }

}
