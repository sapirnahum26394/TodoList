package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {
    private EditText edtInput;
    private ListView listView;
    private SearchView searchview;
    private FloatingActionButton fab;
    private TodoItemAdapter todoAdapter;
    private ArrayList<TodoItem> todoItems;
    private String user_name;
    public static final String MY_DB_NAME = "TodosDB"; //test
    private SQLiteDatabase todos = null; //test
    private boolean isLongClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        user_name = sharedPref.getString("LAST_LOGGED_IN"," ");

        setTitle("Todo list ("+user_name+")");
        createDB();
        create_list();
        listView = findViewById(R.id.listID);
        searchview = findViewById(R.id.searchID);
        fab = (FloatingActionButton) findViewById(R.id.fab_btn);

        fab.setOnClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        searchview.setOnQueryTextListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createDB() {
        try
        {
            todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            String sql = "CREATE TABLE IF NOT EXISTS todos (_id INTEGER primary key, username VARCHAR, title VARCHAR,description VARCHAR, date VARCHAR, time VARCHAR);";
            todos.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }

    }

    private void create_list(){
        todoItems = new ArrayList<TodoItem>();
        Log.d("my_log", String.valueOf(todos));
        if(todos!=null){
            Cursor cursor = todos.rawQuery("SELECT * FROM todos WHERE username='"+user_name+"';", null);
            try {
                while (cursor.moveToNext()) {
                    todoItems.add(new TodoItem(cursor.getInt(0),cursor.getString(2), cursor.getString(3) ,cursor.getString(4),cursor.getString(5)));
                }
            } finally {
                cursor.close();
            }
        }
        todoAdapter = new TodoItemAdapter(this, todoItems);
        listView = findViewById(R.id.listID);
        listView.setAdapter(todoAdapter);

    }

    public void showDelDialog(int itemId) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_delete);
        dialog.setTitle("Delete item");
        dialog.setMessage("Do you want to delete this Todo item ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                todos.delete("todos","_id = "+itemId,null);
                Toast.makeText(ToDoListActivity.this, "Todo was DELETED", Toast.LENGTH_LONG).show();
                create_list();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d("mylog", ">>>> NO");
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnLogoutID) {
            SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("IS_LOGGED_IN", false);
            editor.putString("LAST_LOGGED_IN", " ");
            editor.apply();
            finish();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent =null;
        Log.d("tets", String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.fab_btn:
                intent = new Intent(ToDoListActivity.this, EditorActivity.class);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isLongClick){
            Log.d("mylog", "position"+position + "," + todoItems.get(position).getTitle());
            Intent i = new Intent(ToDoListActivity.this, EditorActivity.class);
            i.putExtra("txtheadID", "UPDATE Todo (id=");
            i.putExtra("id", todoItems.get(position).getID());
            i.putExtra("title", todoItems.get(position).getTitle());
            i.putExtra("description", todoItems.get(position).getDescription());
            i.putExtra("date", todoItems.get(position).getDate());
            i.putExtra("time", todoItems.get(position).getTime());
            i.putExtra("btn", "UPDATE");
            startActivity(i);
        }
        isLongClick = false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isLongClick = true;
        TodoItem todoItem = todoItems.get(position);
        int itemId= todoItem.getID();
        showDelDialog(itemId);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList todoItemResults = new ArrayList<TodoItem>();
        for (TodoItem x: todoItems) {
            if (x.getDescription().contains(newText) || x.getTitle().contains(newText)) {
                todoItemResults.add(x);
            }
        }
        todoAdapter = new TodoItemAdapter(this, todoItemResults);
        listView.setAdapter(todoAdapter);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        create_list();
    }
}

