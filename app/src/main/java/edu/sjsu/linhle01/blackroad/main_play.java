package edu.sjsu.linhle01.blackroad;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class main_play extends AppCompatActivity {

    private Button start, result, l, r, b;
    long countDown;
    private ImageView car;
    private int count;
    private TextView score;
    private TextView local;
    String location = "";

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_play);

        car = (ImageView) findViewById(R.id.car);
        l = (Button) findViewById(R.id.left_but);
        r = (Button) findViewById(R.id.right_but);
        score = (TextView) findViewById(R.id.score);
        local = (TextView) findViewById(R.id.location);
        b = (Button) findViewById(R.id.how);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);



    }


    public void left(View view)
    {
        TranslateAnimation animate = new TranslateAnimation(0, -view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        car.startAnimation(animate);
        count++;
        score.setText(Integer.toString(count));


    }

    public void right(View view)
    {
        TranslateAnimation animate = new TranslateAnimation(0, view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        car.startAnimation(animate);
        count++;
        score.setText(Integer.toString(count));
    }

    public void start_game(View view) {
        start = (Button) findViewById(R.id.start);
        final TextView st = (TextView) findViewById(R.id.display);
       /* Chronometer timer = (Chronometer) findViewById(R.id.chronometer);
        timeStart = Calendar.getInstance().getTime();
        timer.setFormat("%02d:%02d:%02d");
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();*/
        start.setVisibility(View.GONE);
        result = (Button) findViewById(R.id.result_but);
        result.setVisibility(View.GONE);

        location = pref.getString("location", "");
        local.setText("Welcome to "+location);

        l.setVisibility(View.VISIBLE);
        r.setVisibility(View.VISIBLE);

        new CountDownTimer(30000, 1000) { //Sets 30 second remaining

            public void onTick(long millisUntilFinished) {
                countDown = millisUntilFinished;
                st.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                st.setText("Finish");
                result.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                r.setVisibility(View.GONE);
            }
        }.start();
    }

    public void result(View view)
    {
        Intent intent = new Intent(this, Result.class);
        String name = pref.getString("name", "");

        String URL = "content://edu.sjsu.linhle01.blackroad.PlayerProvider";
        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");
        boolean check = true;

        if (c.moveToFirst()) {
            do{
                /*Toast.makeText(this,
                        c.getString(c.getColumnIndex(PlayerProvider._ID)) +
                                ", " +  c.getString(c.getColumnIndex( PlayerProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex( PlayerProvider.SCORE)),
                        Toast.LENGTH_SHORT).show();*/
                if(c.getString(c.getColumnIndex( PlayerProvider.NAME)).equals(name))
                {
                    check = false;
                    if(Integer.valueOf(c.getString(c.getColumnIndex( PlayerProvider.SCORE))) > count) {
                        count = Integer.valueOf(c.getString(c.getColumnIndex( PlayerProvider.SCORE)));
                    }

                }
            } while (c.moveToNext());
        }

        if(check == false){

        }

        ContentValues values = new ContentValues();
        values.put(PlayerProvider.NAME,name);

        values.put(PlayerProvider.SCORE, Integer.toString(count));

        Uri uri = getContentResolver().insert(
                PlayerProvider.CONTENT_URI, values);

        /*Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();*/



        startActivity(intent);

    }




}



