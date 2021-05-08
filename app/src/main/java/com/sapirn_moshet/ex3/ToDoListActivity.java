package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ToDoListActivity extends AppCompatActivity {
    private String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        user_name = getIntent().getExtras().getString("user_name");
        setTitle("Todo list ("+user_name+")");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mybutton) {
            SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("IS_LOGGED_IN",false);
            editor.putString("LAST_LOGGED_IN", " ");
            editor.apply();
            Intent intent = new Intent(ToDoListActivity.this, LoginActivity.class);
            intent.putExtra("user_name",sharedPref.getString("LAST_LOGGED_IN"," "));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

}