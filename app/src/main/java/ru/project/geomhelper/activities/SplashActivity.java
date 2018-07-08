package ru.project.geomhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.project.geomhelper.sqlite.DB;

import static ru.project.geomhelper.sqlite.OpenHelper.NUM_COLUMN_WELCOME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB db = new DB(getApplicationContext());
        if (db.getInt(NUM_COLUMN_WELCOME) == 0) {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
