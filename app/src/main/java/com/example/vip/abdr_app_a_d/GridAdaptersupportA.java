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

public class GridAdaptersupportA extends BaseAdapter {

    String []titles;
    String []ref;
    String []desc;
    String []answer;
    String []state;
    String []from;
    int []id;


    private Context context;
    private LayoutInflater inflater;
    public GridAdaptersupportA(Context c, String []titles, String []state, String []ref, String []desc, String []answer, String[]from, int []id){

        this.context=c;
        this.titles=titles;
        this.ref=ref;
        this.desc=desc;
        this.id=id;
        this.answer=answer;
        this.state=state;
        this.from=from;

    }
    public GridAdaptersupportA(Context c){

        this.context=c;

    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public String  getItem(int position) {
        return titles[position];
    }

    public int  getid(int position) {
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
            gridv=inflater.inflate(R.layout.supportrow,null);
        }
        //get view
        TextView n=(TextView)gridv.findViewById(R.id.stat);
        TextView r=(TextView)gridv.findViewById(R.id.title);


        //set data
        n.setText(state[position]);
        r.setText(titles[position]);

        return gridv;
    }

}
