package com.example.vip.abdr_app_a_d;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class luggageAdapter extends ArrayAdapter<Luggage_info> {

    ArrayList<Luggage_info> array;

    private Activity context;
    //private LayoutInflater inflater;
    public luggageAdapter(Activity context,int r, ArrayList<Luggage_info> objects) {
        super(context,r, objects);
        this.context= context;
        this.array=objects;
    }

    @Override
    public int getCount() {
        return array.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridv=convertView;
        if (convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            gridv = inflater.inflate(R.layout.lugrow, parent,false);
        }
        //get view
        final Luggage_info li=array.get(position);
        TextView n=(TextView)gridv.findViewById(R.id.number);
        ImageView r=(ImageView)gridv.findViewById(R.id.wev);


        //set data
        n.setText(li.getBarnumber()+"");
        r.setImageBitmap(li.getImage());
        return gridv;
    }

}
