package edu.sjsu.linhle01.blackroad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    private SQLiteDatabase db;
    private ArrayList<String> leaderboard = new ArrayList<>();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        display();
        lv = (ListView) findViewById(R.id.player);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                leaderboard );

        lv.setAdapter(arrayAdapter);

    }

    public void display()
    {
        String URL = "content://edu.sjsu.linhle01.blackroad.PlayerProvider";
        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                /*Toast.makeText(this,
                        c.getString(c.getColumnIndex(PlayerProvider._ID)) +
                                ", " +  c.getString(c.getColumnIndex( PlayerProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex( PlayerProvider.SCORE)),
                        Toast.LENGTH_SHORT).show();*/
                leaderboard.add(c.getString(c.getColumnIndex( PlayerProvider.NAME)) +
                        "      " + c.getString(c.getColumnIndex( PlayerProvider.SCORE)) + " points");
            } while (c.moveToNext());
        }

        //db.execSQL("DROP TABLE IF EXISTS player");
    }

    public void again(View view)
    {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}
