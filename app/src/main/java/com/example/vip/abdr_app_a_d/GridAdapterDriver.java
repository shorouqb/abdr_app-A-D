package com.example.vip.abdr_app_a_d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapterDriver extends BaseAdapter {
    String []names;
    String []phones;
    String []nationality;
    String [] image;
    long []id;
    String []car_color;
    String []car_brand;
    String []car_plate_number;
    int [] D_Id;

    private Context context;
    private LayoutInflater inflater;
    public GridAdapterDriver(Context c, long [] id, String []names, String []phones, String []nationality, String []img , String []car_color, String []car_brand, String []car_plate_number, int [] D_Id){

        this.context=c;
        this.names=names;
        this.id=id;
        this.phones=phones;
        this.image=img;
        this.nationality=nationality;
        this.car_color=car_color;
        this.car_brand=car_brand;
        this.car_plate_number=car_plate_number;
        this.D_Id=D_Id;

    }
    public GridAdapterDriver(Context c){

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
        return id[position];
    }

    public String getNationality(int position) {
        return nationality[position];
    }

    public String getPhones(int position) {
        return phones[position];
    }

    public String getCar_color(int position) {
        return car_color[position];
    }

    public String getCar_brand(int position) {
        return car_brand[position];
    }

    public String getCar_plate_number(int position) {
        return car_plate_number[position];
    }
    public int getD_ID(int position) {
        return D_Id[position];
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
            gridv=inflater.inflate(R.layout.drivers_row,null);
        }
        //get view
        TextView n=(TextView)gridv.findViewById(R.id.name);
        TextView i=(TextView)gridv.findViewById(R.id.id_number);
        //set data
        n.setText(names[position]);
        i.setText(id[position]+"");

        return gridv;
    }
}
