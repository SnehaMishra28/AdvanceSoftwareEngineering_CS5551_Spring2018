package com.example.snehamishra.smartshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by CJ on 3/4/2018.
 */

public class ItemAdapter extends ArrayAdapter
{
    // holds context
    private final Context context;

    // holds list of values
    private final ArrayList<ItemData> values;

    // Constructor
    public ItemAdapter(Context context, ArrayList<ItemData> values)
    {
        super(context, R.layout.item_layout, values);
        this.context = context;
        this.values = values;
    } // end constructor

    // Sets items to correct view element
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // inflates layout
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // gets the layout
        View rowView = inflater.inflate(R.layout.item_layout, parent, false);

        // finds the correct element and assigns data
        TextView nameView = rowView.findViewById(R.id.txt_name);
        nameView.setText(values.get(position).getName());

        TextView quantityView = rowView.findViewById(R.id.txt_creator);
        quantityView.setText(String.valueOf("By: " + values.get(position).getCreator()));

        TextView storeView = rowView.findViewById(R.id.txt_store);
        storeView.setText(String.valueOf(", For: $" + values.get(position).getPrice()));

        ImageView iv = rowView.findViewById(R.id.img_item);
        String image = values.get(position).getImage();

        if(!image.equals("") && !image.equals(null))
        {
            try
            {
                Picasso.with(context)
                        .load(image)
                        .resize(100, 100)
                        .into(iv);
            }
            catch(Exception e)
            {
                System.out.println(e.getStackTrace());
            }

        } // end if

        // returns completed view
        return rowView;
    } // end getView
} // end class ItemAdapter
