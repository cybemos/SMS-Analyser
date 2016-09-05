package com.cybemos.analyser.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;
import com.cybemos.analyser.data.Util;
import com.cybemos.analyser.data.parser.MyXMLParser;
import com.cybemos.analyser.data.parser.ParserController;
import com.cybemos.analyser.data.parser.TXTParser;
import com.cybemos.analyser.ui.dialogs.AnalyseDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonAnalyse = (Button) findViewById(R.id.button_analyse);
        buttonAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyseDialog dialog = new AnalyseDialog();
                dialog.show(getFragmentManager(), AnalyseDialog.TAG);
            }
        });

        ParserController controller = ParserController.getInstance();
        controller.removeAllParsers();
        controller.addParser(new MyXMLParser());
        controller.addParser(new TXTParser());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent send_act = new Intent(MainActivity.this, ShowFilesActivity.class);
                startActivity(send_act);
            }
        });
    }

    public void launchAnalyse(boolean allContacts) {
        Intent intent;
        if (allContacts) {
            intent = new Intent(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            startActivityForResult(intent, Constants.ACTIVITY_SELECT_CONTACT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ACTIVITY_SELECT_CONTACT) {
                Uri result = data.getData();
                String id = result.getLastPathSegment();
                String[] whereArgs = new String[] { String.valueOf(id) };
                Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", whereArgs, null);

                assert cursor != null;
                int phoneNumberIndex = cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number, name;
                if (cursor.moveToFirst()) {
                    number = cursor.getString(phoneNumberIndex);
                    name = cursor.getString(nameIndex);
                    cursor.close();
                    Intent activity = new Intent(MainActivity.this, ResultActivity.class);
                    activity.putExtra(Constants.EXTRA_CONTACT_ID, String.valueOf(id));
                    activity.putExtra(Constants.EXTRA_NUMBER, number);
                    activity.putExtra(Constants.EXTRA_NAME, name);
                    startActivity(activity);
                } else {
                    cursor.close();
                    View view = findViewById(R.id.activity_main);
                    Snackbar.make(view, R.string.error_pick_contact, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
