package com.example.vip.abdr_app_a_d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class GridAdapterPassengerA extends BaseAdapter {
    String []f_names;
    String []l_names;
    String []phones;
    String []nationality;
    String []city;
    String []emails;
    String [] image;
    long []id_number;
    int [] P_Id;

    private Context context;
    private LayoutInflater inflater;
    public GridAdapterPassengerA(Context c, long [] id, String []f_names, String []l_names, String []phones, String []nationality, String []img , String []city, String []emails, int [] P_Id){

        this.context=c;
        this.f_names=f_names;
        this.l_names=l_names;
        this.id_number=id;
        this.phones=phones;
        this.image=img;
        this.nationality=nationality;
        this.P_Id=P_Id;
        this.city=city;
        this.emails=emails;

    }
    public GridAdapterPassengerA(Context c){

        this.context=c;

    }
    public int getCount() {
        return f_names.length;
    }

    @Override
    public String getItem(int position) {
        return f_names[position] +" "+ l_names[position];
    }

    public long getId(int position) {
        return id_number[position];
    }

    public String getNationality(int position) {
        return nationality[position];
    }

    public String getPhones(int position) {
        return phones[position];
    }

    public String getCity(int position) {
        return city[position];
    }

    public String getEmails(int position) {
        return emails[position];
    }

    public int getP_ID(int position) {
        return P_Id[position];
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
            gridv=inflater.inflate(R.layout.passengerrow,null);
        }
        //get view
        TextView n=(TextView)gridv.findViewById(R.id.name);
        TextView i=(TextView)gridv.findViewById(R.id.id_number);
        //set data
        n.setText(f_names[position]+ " " +l_names[position]);
        i.setText(id_number[position]+"");

        return gridv;
    }
}