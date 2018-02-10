package com.example.vip.abdr_app_a_d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by v i p on 3/1/2017.
 */

public class GridAdapterpassengers extends BaseAdapter {

    String []name;
    String []link;
    long [] id;



    private Context context;
    private LayoutInflater inflater;
    public GridAdapterpassengers(Context c, long []id, String []name, String []link ){

        this.context=c;
        this.name=name;
        this.id=id;
        this.link=link;

    }

    @Override
    public int getCount() {
        return id.length;
    }

    @Override
    public String  getItem(int position) {
        return name[position];
    }
    public long  getid(int position) {
        return id[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridv=convertView;
        if (convertView==null){
            inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridv=inflater.inflate(R.layout.passrow,null);
        }
        //get view
        TextView n=(TextView)gridv.findViewById(R.id.name);
        TextView r=(TextView)gridv.findViewById(R.id.id);
        //set data
        n.setText(name[position]);
        r.setText(id[position]+"");

        return gridv;
    }

}
