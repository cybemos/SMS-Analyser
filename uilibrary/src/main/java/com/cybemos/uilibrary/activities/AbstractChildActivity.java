package com.cybemos.uilibrary.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 * @version 1.0
 */
public abstract class AbstractChildActivity extends AppCompatActivity {

    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

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
