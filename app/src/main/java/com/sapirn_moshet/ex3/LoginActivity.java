package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MY_DB_NAME = "TodosDB";

    private SQLiteDatabase Todos = null;
    private Button loginBtn;
    private EditText userName, userPass;
    private ImageView userImg;

    private void saveLoggedUser(String user_name) {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_LOGGED_IN",true);
        editor.putString("LAST_LOGGED_IN", user_name);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userImg = findViewById(R.id.userImgID);
        userName = findViewById(R.id.userID);
        userPass = findViewById(R.id.passID);
        loginBtn = findViewById(R.id.btnLoginID);
        setTitle("Todo login");
        createDB();
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("IS_LOGGED_IN",false)){
            if(isExist(sharedPref.getString("LAST_LOGGED_IN"," "))) {
                Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
                startActivity(intent);
            }
        }
        loginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginID:
                if(checkInputs()) {
                    if (isExist(userName.getText().toString())) {
                        if (!checkPass()) {
                            Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        } else {
                            saveLoggedUser(userName.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        saveLoggedUser(userName.getText().toString());
                        addUser();
                    }
                }
                break;
        }
    }

    private boolean checkPass() {
        String user_Name = userName.getText().toString();
        String user_pass = userPass.getText().toString();
        String sql = "SELECT * FROM users WHERE username='"+user_Name+"'";
        Cursor cr = Todos.rawQuery(sql,null);
        cr.moveToFirst();
        if ( cr.getString(cr.getColumnIndex("password")).equals(user_pass))
            return true;
        return false;

    }

    private boolean checkInputs() {
        String user_Name = userName.getText().toString();
        String user_pass = userPass.getText().toString();
        if (user_Name.equals("")){
            Toast.makeText(this, "Enter User Name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (user_pass.equals("")){
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createDB() {
        try
        {
            Todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            Todos.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }

        loginBtn.setEnabled(true);
    }

    private boolean isExist(String user_Name){
        String sql = "SELECT * FROM users WHERE username='"+user_Name+"'";
        Cursor cr=Todos.rawQuery(sql,null);
        if ( cr.getCount() == 0 )
            return false;
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem aboutMenu = menu.add("About");
        MenuItem exitMenu = menu.add("Exit");

        aboutMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showAboutDialog();
                return true;
            }
        });

        exitMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showExitDialog();
                return true;
            }
        });


        return true;
    }

    public void showAboutDialog() {
        String aboutApp = "Todo App (" + getString(R.string.app_name) + ")\n" +
                "By Sapir Nahum & Moshe Tendler, 01/06/2021.";

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("About App");
        dialog.setMessage(aboutApp);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showExitDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setTitle("Exit App");
        dialog.setMessage("Do you really want to exit Todo app ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish(); // close this activity
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

    public void addUser() {

        // Get the contact name and email entered
        String user_Name = userName.getText().toString();
        String user_pass = userPass.getText().toString();

        // Execute SQL statement to insert new data
        String sql = "INSERT INTO users (username, password) VALUES ('" + user_Name + "', '" + user_pass + "');";
        Todos.execSQL(sql);
        Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText("");
        userPass.setText("");
    }

    @Override
    protected void onDestroy()
    {
        Todos.close();
        super.onDestroy();
    }
}