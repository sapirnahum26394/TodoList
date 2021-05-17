package com.sapirn_moshet.ex3;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class TodoItemAdapter extends ArrayAdapter<TodoItem>
{
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param todoItems A List of AndroidFlavor objects to display in a list
     */
    //ArrayList<TodoItem> items;

    public TodoItemAdapter(Activity context, ArrayList<TodoItem> todoItems)
    {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, todoItems);
    }
    public void update(ArrayList<TodoItem> results){

        ArrayList newItems = new ArrayList<>();
        newItems.addAll(results);
        notifyDataSetChanged();
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null)
        {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            convertView = View.inflate(getContext(), R.layout.list_item, null);
        }
        // Get the {@link AndroidFlavor} object located at this position in the list
        TodoItem currentTodoItem = getItem(position);

        // Find the TextView in the list_item2.xmll layout with the ID version_name
        TextView txtTitle = convertView.findViewById(R.id.txtTitleID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        txtTitle.setText(currentTodoItem.getTitle());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtdescription = convertView.findViewById(R.id.txtDescID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        txtdescription.setText(currentTodoItem.getDescription());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtDate = convertView.findViewById(R.id.txtDateID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
       /*
        txtDate.setText(currentTodoItem.getDate());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtTime = convertView.findViewById(R.id.txtTimeID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        txtTime.setText(currentTodoItem.getTime());
        */

        long test = (currentTodoItem.getDateTime());
        //int test = (currentTodoItem.getDateTime());
        test++;
        Log.d("mylog", "value: " + test);
        Log.d("mylog", "test:"+String.valueOf(test));
        Log.d("mylog", "value of datTime:"+String.valueOf(currentTodoItem.getDateTime()));
       // String num = String.valueOf(currentTodoItem.getDateTime());
        //txtDate.setText(num .substring(0, 2) + "/" + num .substring(2,4) + "/" + num .substring(4,8));
       // String upToNCharacters = num.substring(0, Math.min(num.length(), 8));
       // txtDate.setText(upToNCharacters);
        // Find the TextView in the list_item2.xmll layout with the ID version_number

        TextView txtTime = convertView.findViewById(R.id.txtTimeID);
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        txtTime.setText("11:50");
        // so that it can be shown in the ListView
        return convertView;
    }

}

