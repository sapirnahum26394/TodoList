package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity  implements ListView.OnItemClickListener {
    private EditText edtInput;
    private ListView listView;
 //   private ArrayAdapter arrayAdapter;
//    private ArrayList<custom> arrayList;
    private ArrayList<AndroidFlavor> androidFlavors;;
    private String user_name;
    public static final String MY_DB_NAME = "TodosDB"; //test
    private SQLiteDatabase todos = null; //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        user_name = getIntent().getExtras().getString("user_name");
        setTitle("Todo list ("+user_name+")");
        listView = findViewById(R.id.listID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylog" , "fab was clicked...");
                Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });


        create_list();
        //test
        // Create an ArrayList of AndroidFlavor objects
//        androidFlavors = new ArrayList<AndroidFlavor>();
//        androidFlavors.add(new AndroidFlavor("Todo Title 0", "This is description of Todo0" ,"11/11/20","13:40"));
//        androidFlavors.add(new AndroidFlavor("Todo Title 1", "This is description of Todo1","20/11/20","11:20"));
//        androidFlavors.add(new AndroidFlavor("Todo Title 2", "This is description of Todo1","20/02/20","01:20"));
//        androidFlavors.add(new AndroidFlavor("Todo Title 3", "This is description of Todo1","20/05/20","06:20"));
//
//        // Create an AndroidFlavorAdapter, whose data source is a list of AndroidFlavors.
//        // The adapter knows how to create list item views for each item in the list.
//        AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, androidFlavors);
//
//        // Get a reference to the ListView, and attach the adapter to the listView.
//        listView = findViewById(R.id.listID);
//        listView.setAdapter(flavorAdapter);

        // add Item Click Listener
    //    listView.setOnItemClickListener(this);

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//    {
//        AndroidFlavor androidFlavor = androidFlavors.get(position);
//        //showSimpleAlert(androidFlavor.getVerIcon(), androidFlavor.getVerName() , androidFlavor.getVerAPILevel());
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.id.searchID, menu);
//
//        return true;
//    }
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
        if (id == R.id.fab_btn){
            Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
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

   /*
    @Override
    public void onClick(View v) {
        Log.d("mylog","click on the fab");
        switch (v.getId()) {
            case R.id.fab_btn:
//                Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
//                startActivity(intent);
                Log.d("mylog","click on the fab1");
                break;
        }
    }
    */

    private void create_list(){
        androidFlavors = new ArrayList<AndroidFlavor>();

        todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
        if(todos!=null){
            Cursor cursor = todos.rawQuery("SELECT * FROM todos WHERE username='"+user_name+"';", null);
            try {
                while (cursor.moveToNext()) {
                    androidFlavors.add(new AndroidFlavor(cursor.getInt(0),cursor.getString(2), cursor.getString(3) ,cursor.getString(4),cursor.getString(5)));
                }
            } finally {
                cursor.close();
            }
        }

        AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, androidFlavors);

        listView = findViewById(R.id.listID);
        listView.setAdapter(flavorAdapter);
//        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AndroidFlavor androidFlavor = androidFlavors.get(position);
                int itemId=androidFlavor.getID();
                todos.delete("todos","_id = "+itemId,null);
                create_list();
                return false;
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        AndroidFlavor androidFlavor = androidFlavors.get(position);
        showSimpleAlert(androidFlavor.getTitle(), androidFlavor.getDescription() );
    }

    public void showSimpleAlert(String verName, String verNum)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(verName);
        alertDialog.setMessage(verNum);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
}
