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

public class ToDoListActivity extends AppCompatActivity  implements ListView.OnItemClickListener {
    private EditText edtInput;
    private ListView listView;
    private SearchView searchview;
    private TodoItemAdapter todoAdapter;
    private ArrayList<TodoItem> todoItems;
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
        searchview = findViewById(R.id.searchID);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("mylog", "position"+position + "," + todoItems.get(position).getTitle());

                Intent i = new Intent(ToDoListActivity.this, EditorActivity.class);
                i.putExtra("txtheadID", "UPDATE Todo (id=");
                i.putExtra("id", todoItems.get(position).getID()-1);
                i.putExtra("title", todoItems.get(position).getTitle());
                i.putExtra("description", todoItems.get(position).getDescription());
                i.putExtra("date", todoItems.get(position).getDate());
                i.putExtra("time", todoItems.get(position).getTime());
                i.putExtra("btn", "UPDATE");

                startActivity(i);
//                Object listItem = listView.getItemAtPosition(position);
            }
        });
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("mylog", String.valueOf(ToDoListActivity.this.todoAdapter.getFilter()));
                ToDoListActivity.this.todoAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("mylog", "new text is:"+newText);
                Log.d("mylog", String.valueOf(ToDoListActivity.this.todoAdapter.getFilter()));
                ArrayList todoItemResults = new ArrayList<TodoItem>();
                for (TodoItem x: todoItems) {
                    if (x.getDescription().contains(newText) || x.getTitle().contains(newText) || x.getTime().contains(newText) || x.getDate().contains(newText)) {
                        todoItemResults.add(x);
                        Log.d("mylog", "x is:" + x.getTitle());
                    }
                }

                ((TodoItemAdapter)listView.getAdapter()).update(todoItemResults);


               // todoItems.addAll(todoItemResults);

//                todoAdapter.notifyDataSetChanged();
                // listView = TodoItemAdapter.this.update(todoItemResults);

//                ToDoListActivity.this.todoAdapter.getFilter().filter(newText);
                return false;
            }
        });

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
        todoItems = new ArrayList<TodoItem>();
        todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = todoItems.get(position);
                int itemId= todoItem.getID();
                showDelDialog(itemId);
//                todos.delete("todos","_id = "+itemId,null);
//                create_list();
                return false;
            }
        });
    }



    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Log.d("mylog", "position"+position);
        TodoItem todoItem = todoItems.get(position);
        showSimpleAlert(todoItem.getTitle(), todoItem.getDescription() );
        //  showExitDialog();
    }



    public void showSimpleAlert(String verName, String verNum)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(verName);
        alertDialog.setMessage(verNum);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public void showDelDialog(int itemId)
    {
        Log.d("mylog", ">>>> showDelDialog()");

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
                Log.d("mylog", ">>>> YES");
                Toast.makeText(ToDoListActivity.this, "YES Clicked!", Toast.LENGTH_LONG).show();
                todos.delete("todos","_id = "+itemId,null);
                create_list();
               // finish(); // close this activity
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d("mylog", ">>>> NO");
                Toast.makeText(ToDoListActivity.this, "NO Clicked!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

}

