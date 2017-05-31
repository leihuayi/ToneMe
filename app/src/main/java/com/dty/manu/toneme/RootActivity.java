package com.dty.manu.toneme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Random;

/**
 * Created by Sarah on 23/05/2017.
 */

public class RootActivity extends AppCompatActivity {

    int onStartCount = 0;
    static Random rand = new Random();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }
    
    public static int getRawIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "raw", context.getPackageName());
    }

    public static int getIdIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "id", context.getPackageName());
    }

    public static String randLetter() {
        String[] chars = {"a","b","c","d","e","f","g"};
        return (chars[rand.nextInt(7)]);
    }

    public void setClickNoteListener(final String expectedNote) {
        //Hide text
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean keyboardPref = sharedPref.getBoolean("pref_notes_keyboard", true);

        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        for (int i = 0; i < letters.length; i++) {
            String letter = letters[i];

            RelativeLayout button_note = (RelativeLayout) findViewById(getIdIdentifier(this, "note_" + letter));

            if(! keyboardPref) {
                button_note.getChildAt(0).setBackgroundColor(Color.WHITE);
            }

            //Listener
            button_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noteText = v.getResources().getResourceName(v.getId()); //The name of the note we clicked on "A", "C", ..
                    noteText = noteText.substring(noteText.length()-1);

                    ViewGroup vg = (ViewGroup) v;
                    final TextView text_button= (TextView) vg.getChildAt(0); //The text of the button we clicked on

                    if(expectedNote.equals(noteText)) {
                        /** Highlight correct **/
                        text_button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorSuccess, null));

                        /** Go to next **/
                        finish();
                        startActivity(getIntent());
                    }
                    else {
                        /** Highlight wrong **/
                        text_button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorError, null));

                        /** Delay and put back button **/
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /** Go to next **/
                                if(keyboardPref){
                                    text_button.setBackground(getResources().getDrawable(R.drawable.button));
                                }
                                else {
                                    text_button.setBackgroundColor(Color.WHITE);
                                }
                            }
                        }, 700);
                    }
                }
            });
        }


    }

    public void skip(final String expectedNote) {
        /** Highlight correct **/
        RelativeLayout button_note = (RelativeLayout) findViewById(getIdIdentifier(this, "note_" + expectedNote));
        TextView text_note = (TextView) button_note.getChildAt(0);
        text_note.setBackgroundColor(Color.parseColor("#cddc39"));

        /** Delay **/
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /** Go to next **/
                finish();
                startActivity(getIntent());
            }
        }, 700);
    }

}
