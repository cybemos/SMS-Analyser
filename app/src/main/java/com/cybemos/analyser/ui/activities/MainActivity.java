package com.cybemos.analyser.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

import com.cybemos.analyser.Constants;
import com.cybemos.analyser.R;

public class MainActivity extends AppCompatActivity {

    public static final int ANIMATION_DURATION = 200;
    private static final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private Animator.AnimatorListener animatorListener;
    private AnimatorSet currentAnimation;
    private boolean mMenuVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animatorListener = new AnimListener();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonAnalyse = (Button) findViewById(R.id.button_analyse);
        buttonAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        Button buttonAll = (Button) findViewById(R.id.button_all);
        Button buttonSelectContact = (Button) findViewById(R.id.button_select_contact);

        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMenu();
                launchAnalyse(true);
            }
        });
        buttonSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMenu();
                launchAnalyse(false);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMenuVisible) {
                    hideMenu();
                }
                Intent send_act = new Intent(MainActivity.this, ShowFilesActivity.class);
                startActivity(send_act);
            }
        });

        View view = findViewById(R.id.menu_expanded);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMenu();
            }
        });

        boolean show = (savedInstanceState != null) && savedInstanceState.getBoolean(Constants.EXTRA_MENU_SHOWN, false);
        if (show) {
            showMenuWithoutAnimation();
        } else {
            hideMenuWithoutAnimation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(Constants.EXTRA_MENU_SHOWN, mMenuVisible);
    }

    protected AnimatorSet generateAnimation(boolean show, View... views) {
        View center = findViewById(R.id.center);
        View menu = findViewById(R.id.menu_expanded);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator old;
        ObjectAnimator transX, transY, fade;
        float centerX = center.getX();
        float centerY = center.getY();
        if (show) {
            old = ObjectAnimator.ofFloat(menu, "alpha", 0f, 1f);
        } else {
            old = ObjectAnimator.ofFloat(menu, "alpha", 1f, 0f);
        }
        old.setInterpolator(interpolator);
        old.setDuration(ANIMATION_DURATION);
        animatorSet.play(old);
        for (View view : views) {
            if (show) {
                transX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, centerX - view.getX() - view.getWidth() / 2, 0);
                transY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, centerY - view.getY() - view.getHeight() / 2, 0);
                fade = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            } else {
                transX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, centerX - view.getX() - view.getWidth() / 2);
                transY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, centerY - view.getY() - view.getHeight() / 2);
                fade = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            }
            transX.setInterpolator(interpolator);
            transY.setInterpolator(interpolator);
            fade.setInterpolator(interpolator);
            transX.setDuration(ANIMATION_DURATION);
            transY.setDuration(ANIMATION_DURATION);
            fade.setDuration(ANIMATION_DURATION);
            animatorSet.play(transX).with(transY).with(fade).with(old);
            old = fade;
        }
        if (!show) {
            animatorSet.addListener(animatorListener);
        }
        return animatorSet;
    }

    public void showMenu() {
        if (currentAnimation != null && currentAnimation.isRunning()) {
            currentAnimation.end();
        }
        View view = findViewById(R.id.menu_expanded);
        view.setVisibility(View.VISIBLE);
        View view1 = findViewById(R.id.button_all);
        View view2 = findViewById(R.id.button_select_contact);
        currentAnimation = generateAnimation(true, view1, view2);
        currentAnimation.start();
        mMenuVisible = true;
    }

    protected void showMenuWithoutAnimation() {
        View view = findViewById(R.id.menu_expanded);
        view.setVisibility(View.VISIBLE);
        mMenuVisible = true;
    }

    protected void hideMenuWithoutAnimation() {
        View view = findViewById(R.id.menu_expanded);
        view.setVisibility(View.GONE);
        mMenuVisible = false;
    }

    public void hideMenu() {
        View view1 = findViewById(R.id.button_all);
        View view2 = findViewById(R.id.button_select_contact);
        if (currentAnimation != null && currentAnimation.isRunning()) {
            currentAnimation.end();
        }
        currentAnimation = generateAnimation(false, view1, view2);
        currentAnimation.start();
        mMenuVisible = false;
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

    private class AnimListener implements Animator.AnimatorListener {

        private float lastPosX1;
        private float lastPosX2;
        private float lastPosY1;
        private float lastPosY2;

        @Override
        public void onAnimationStart(Animator animator) {
            View view1 = findViewById(R.id.button_all);
            View view2 = findViewById(R.id.button_select_contact);
            lastPosX1 = view1.getX();
            lastPosX2 = view2.getX();
            lastPosY1 = view1.getY();
            lastPosY2 = view2.getY();
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            hideMenuWithoutAnimation();
            View view1 = findViewById(R.id.button_all);
            View view2 = findViewById(R.id.button_select_contact);
            view1.setX(lastPosX1);
            view2.setX(lastPosX2);
            view1.setY(lastPosY1);
            view2.setY(lastPosY2);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            hideMenuWithoutAnimation();
            View view1 = findViewById(R.id.button_all);
            View view2 = findViewById(R.id.button_select_contact);
            view1.setX(lastPosX1);
            view2.setX(lastPosX2);
            view1.setY(lastPosY1);
            view2.setY(lastPosY2);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }
}
