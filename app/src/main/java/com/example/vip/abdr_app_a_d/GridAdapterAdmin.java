package com.example.vip.abdr_app_a_d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Sarah on 23/04/2017.
 */

public class GridAdapterAdmin extends BaseAdapter {

    String []names;
    String []emails;
    String []phones;
    String []cities;
    int [] A_Id;

    private Context context;
    private LayoutInflater inflater;
    public GridAdapterAdmin(Context c, int [] id, String []names, String []phones,String []emails,String []cities){

        this.context=c;
        this.names=names;
        this.A_Id=id;
        this.phones=phones;
        this.emails=emails;
        this.cities=cities;

    }
    public GridAdapterAdmin(Context c){

        this.context=c;

    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int position) {
        return names[position];
    }

    public long getId(int position) {
        return A_Id[position];
    }

    public String getEmails(int position) {
        return emails[position];
    }

    public String getPhones(int position) {
        return phones[position];
    }

    public String getCities(int position) {
        return cities[position];
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
            gridv=inflater.inflate(R.layout.adminrow,null);
        }
        //get view
        TextView n=(TextView)gridv.findViewById(R.id.name);
        //set data
        n.setText(names[position]);

        return gridv;
    }
}
