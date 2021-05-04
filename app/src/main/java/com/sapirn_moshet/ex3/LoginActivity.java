package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MY_DB_NAME = "TodosDB";

    private SQLiteDatabase users = null;
    private Button loginBtn;
    private EditText userName, userPass;
    private ImageView userImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userImg = findViewById(R.id.userImgID);
        userName = findViewById(R.id.userID);
        userPass = findViewById(R.id.passID);
        loginBtn = findViewById(R.id.btnLoginID);

        loginBtn.setOnClickListener(this);

        createDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginID:
                if(checkInputs()) {
                    if (isExist()) {
                        Toast.makeText(this, "user exist", Toast.LENGTH_SHORT).show();
                        Log.d("mylog", " ---> user exist");
                        if (!checkPass()) {
                            Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            Log.d("mylog", " ---> Password is incorrect");

                        } else {
                            Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();
                            Log.d("mylog", " ---> User logged in");
                        }
                    } else {
                        Toast.makeText(this, "user not exist", Toast.LENGTH_SHORT).show();
                        Log.d("mylog", " ---> user not exist");

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
        Cursor cr=users.rawQuery(sql,null);
        cr.moveToFirst();
        Log.d("mylog",user_pass);
        Log.d("mylog",cr.getString(cr.getColumnIndex("password")));

        if ( cr.getString(cr.getColumnIndex("password")).equals(user_pass))
            return true;
        return false;

    }

    private boolean checkInputs() {
        String user_Name = userName.getText().toString();
        String user_pass = userPass.getText().toString();
        if (user_Name.equals("")){
            Toast.makeText(this, "Enter User Name!", Toast.LENGTH_SHORT).show();
            Log.d("mylog"," ---> Enter User Name!");
            return false;
        }
        if (user_pass.equals("")){
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            Log.d("mylog"," ---> Enter Password!");
            return false;
        }
        return true;
    }


    public void createDB()
    {
        try
        {
            users = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            users.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
        loginBtn.setEnabled(true);
    }
    private boolean isExist(){
        String user_Name = userName.getText().toString();
        String sql = "SELECT * FROM users WHERE username='"+user_Name+"'";
        Cursor cr=users.rawQuery(sql,null);
        if ( cr.getCount() == 0 )
            return false;
        return true;
    }
//
//    public void deleteDB()
//    {
//        // Delete database
//        deleteDatabase(MY_DB_NAME);
//
//        btnAddContact.setEnabled(false);
//        btnDelContact.setEnabled(false);
//        btnShowContacts.setEnabled(false);
//        btnDelDB.setEnabled(false);
//        btnCreateDB.setEnabled(true);
//
//        edtShow.setText("");
//        edtName.setText("");
//        edtId.setText("");
//        edtEmail.setText("");
//    }
//
    public void addUser()
    {

        // Get the contact name and email entered
        String user_Name = userName.getText().toString();
        String user_pass = userPass.getText().toString();

        // Execute SQL statement to insert new data
        String sql = "INSERT INTO users (username, password) VALUES ('" + user_Name + "', '" + user_pass + "');";
        users.execSQL(sql);
        Log.d("mylog"," ---> "+user_Name + " was insert!");

        Toast.makeText(this, user_Name + " was insert!", Toast.LENGTH_SHORT).show();
    }

//
//    public void showContacts()
//    {
//        // A Cursor provides read and write access to database results
//        String sql = "SELECT * FROM contacts";
//        Cursor cursor = contactsDB.rawQuery(sql, null);
//
//        // Get the index for the column name provided
//        int idColumn = cursor.getColumnIndex("id");
//        int nameColumn = cursor.getColumnIndex("name");
//        int emailColumn = cursor.getColumnIndex("email");
//
//        String contactList = "";
//
//        // Move to the first row of results & Verify that we have results
//        if (cursor.moveToFirst()) {
//            do {
//                // Get the results and store them in a String
//                String id = cursor.getString(idColumn);
//                String name = cursor.getString(nameColumn);
//                String email = cursor.getString(emailColumn);
//
//                contactList = contactList + id + ", " + name + ", " + email + "\n";
//
//                // Keep getting results as long as they exist
//            } while (cursor.moveToNext());
//
//            edtShow.setText(contactList);
//
//        } else {
//
//            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
//            edtShow.setText("");
//        }
//    }
//
//    public void deleteContact()
//    {
//        // Get the id to delete
//        String id = edtId.getText().toString();
//
//        // Delete matching id in database
//        String sql = "DELETE FROM contacts WHERE id = " + id + ";";
//        contactsDB.execSQL(sql);
//        Toast.makeText(this, id + " was delete!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        contactsDB.close();
//        super.onDestroy();
//    }
}