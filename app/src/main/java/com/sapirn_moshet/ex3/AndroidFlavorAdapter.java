package com.sapirn_moshet.ex3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class AndroidFlavorAdapter extends ArrayAdapter<AndroidFlavor>
{
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param androidFlavors A List of AndroidFlavor objects to display in a list
     */
    public AndroidFlavorAdapter(Activity context, ArrayList<AndroidFlavor> androidFlavors)
    {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
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
        AndroidFlavor currentAndroidFlavor = getItem(position);

        // Find the TextView in the list_item2.xmll layout with the ID version_name
        TextView txtTitle = convertView.findViewById(R.id.txtTitleID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        txtTitle.setText(currentAndroidFlavor.getTitle());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtdescription = convertView.findViewById(R.id.txtDescID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        txtdescription.setText(currentAndroidFlavor.getDescription());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtDate = convertView.findViewById(R.id.txtDateID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        txtDate.setText(currentAndroidFlavor.getDate());

        // Find the TextView in the list_item2.xmll layout with the ID version_number
        TextView txtTime = convertView.findViewById(R.id.txtTimeID);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        txtTime.setText(currentAndroidFlavor.getTime());
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }

}
