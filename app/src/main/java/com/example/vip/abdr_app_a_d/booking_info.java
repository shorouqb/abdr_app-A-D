package com.example.vip.abdr_app_a_d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class booking_info extends BaseActivityA implements View.OnClickListener{
        String ref;
        String state;
        String P_phone;
        int driver;
        int P_ID;
        int id;
        String time;
        String date;
        double fee;
        int pos;
        TextView status,time_,date_,fees,finfo,minfo,linfo,tv,uinfo;
        ImageButton rat,bar,sup,contact,em;
        Button dri;
        String msg="";
        Intent intent;
        ProgressDialog prgDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_booking_info);
    rat = (ImageButton) findViewById(R.id.rating);
    rat.setOnClickListener(this);

    bar = (ImageButton) findViewById(R.id.barcode);
    bar.setOnClickListener(this);

    sup = (ImageButton) findViewById(R.id.support);
    sup.setOnClickListener(this);

    contact = (ImageButton) findViewById(R.id.contact);
    contact.setOnClickListener(this);

    em = (ImageButton) findViewById(R.id.email);
    em.setOnClickListener(this);

    dri = (Button) findViewById(R.id.driver);
    dri.setOnClickListener(this);
    prgDialog = new ProgressDialog(this);
    // Set Cancelable as False

    prgDialog.onBackPressed();
    finfo = (TextView) findViewById(R.id.flightinfo);
    minfo = (TextView) findViewById(R.id.passengersinfo);
    uinfo = (TextView) findViewById(R.id.userinfo);
    linfo = (TextView) findViewById(R.id.luggageinfo);
}
    @Override
    protected void onStart(){
        super.onStart();
        // info from query
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Booking...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        Intent i = getIntent();

        pos=i.getExtras().getInt("pos");
        ref=i.getStringArrayExtra("booking_reference")[pos];
        state=i.getStringArrayExtra("status")[pos];
        driver=i.getIntArrayExtra("D_ID")[pos];
        P_ID=i.getIntArrayExtra("P_ID")[pos];
        id=i.getIntArrayExtra("service_id")[pos];
        time=i.getStringArrayExtra("time")[pos];
        date=i.getStringArrayExtra("date")[pos];
        fee=i.getDoubleArrayExtra("service_fee")[pos];

        booking_info.BackGround b=new booking_info.BackGround();
        b.execute(""+P_ID);

        booking_info.BackGround2 bb=new booking_info.BackGround2(this);
        bb.execute(ref);
        status=(TextView)findViewById(R.id.status);
        time_=(TextView)findViewById(R.id.time);
        date_=(TextView)findViewById(R.id.date);
        fees=(TextView)findViewById(R.id.fee);

        status.setText(state);
        time_.setText(time);
        date_.setText(date);
        fees.setText(fee+" SAR");
        tv = (TextView)findViewById(R.id.priceD);
        tv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rating:

                if(state.equals("Rated")){
                    booking_info.BackGround3 b = new booking_info.BackGround3();
                    b.execute(""+id);
                }
                else if(!state.equals("Done")){

                    Toast.makeText(booking_info.this,"This booking not rated until now", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.driver:
                prgDialog = ProgressDialog.show(this, "Wait Please...", "Getting Driver Information's...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                booking_info.BackGround1 bb=new booking_info.BackGround1();
                bb.execute(driver+"");
                break;
            case R.id.barcode:
                if(!state.equals("Done")&&!state.equals("Rated")&&!state.equals("Scanned")){
                    Toast.makeText(booking_info.this, "You cannot see barcode's yet .. booking is not done", Toast.LENGTH_LONG).show();
                }
                else {
                    intent = new Intent(booking_info.this, barcodes.class);
                    intent.putExtra("ref", ref);
                    intent.putExtra("P_ID", P_ID);
                    startActivity(intent);
                }
                break;
            case R.id.support:
                intent = new Intent(booking_info.this, booking_support_info.class);
                intent.putExtra("ref", ref);
                startActivity(intent);

                break;
            case R.id.contact:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", P_phone, null)));

                break;
            case R.id.email:
                intent= new Intent(booking_info.this,send_email.class);
                intent.putExtra("ID" , P_ID );
                intent.putExtra("check" , 1 );
                startActivity(intent);
                break;
            case R.id.priceD:
                AlertDialog.Builder builder = new AlertDialog.Builder(booking_info.this);
                builder.setMessage(msg);
                builder.setTitle("Price Details");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int P_ID = Integer.parseInt(params[0]);
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/passengerinfo.php");
                String urlParams = "P_ID="+P_ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;
            prgDialog.dismiss();
            if(s.equals("false")){
                Toast.makeText(booking_info.this, "no driver found", Toast.LENGTH_LONG).show();
            }


            else {

                JSONObject root = null;
                try {
                    root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    JSONObject c=array.getJSONObject(0);
                    P_phone=c.getString("phone");
                    uinfo.setText("Name: "+c.getString("first_name")+" " +c.getString("last_name")+"\n"+"Nationality: "+c.getString("nationality")
                            +"\n"+"Id Nimber: "+c.getString("id_number")+"\n"+"Phone: "+P_phone
                            +"\n"+"Email: "+c.getString("email"));

                } catch (JSONException e) {
                    //Toast.makeText(booking_info.this, "driver info "+e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }


        }


    }
    class BackGround1 extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        int did = Integer.parseInt(params[0]);
        String data="";
        int tmp;

        try {
            URL url = new URL("http://abdr.000webhostapp.com/driverinfo.php");
            String urlParams = "D_ID="+did;

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(urlParams.getBytes());
            os.flush();
            os.close();

            InputStream is = httpURLConnection.getInputStream();
            while((tmp=is.read())!=-1){
                data+= (char)tmp;
            }

            is.close();
            httpURLConnection.disconnect();

            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        String err=null;
        prgDialog.dismiss();
        if(s.equals("false")){
            Toast.makeText(booking_info.this, "no driver found", Toast.LENGTH_LONG).show();
        }


        else {

            JSONObject root = null;
            try {
                root = new JSONObject(s);
                JSONArray array = root.getJSONArray("user_data");
                JSONObject c=array.getJSONObject(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(booking_info.this);
                final String phone= ""+c.getLong("phone");
                builder.setMessage("Driver Name: "+c.getString("name")+"\n"+"Driver Nationality: "+c.getString("nationality")
                        +"\n"+"Car Brand: "+c.getString("car_brand")+"\n"+"Car Color: "+c.getString("car_color")
                        +"\n"+"Car Plate: "+c.getString("plate_number"));
                builder.setTitle("Driver informations");
                WebView wv = new WebView(booking_info.this);
                String data="<div align='center'> <img src='"+c.getString("photo")+"' style='width:100px; height:100px;' /></div>";
                wv.loadData(data, "text/html", "utf-8");
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                builder.setView(wv);
                builder.setPositiveButton("call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone , null)));
                    }
                });

                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (JSONException e) {
                //Toast.makeText(booking_info.this, "driver info "+e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }


    }


}
    class BackGround2 extends AsyncTask<String, String, String> {
    private booking_info bi;
    public BackGround2(booking_info b){
        this.bi=b;
    }
    @Override
    protected String doInBackground(String... params) {
        String  refer = params[0];
        String data="";
        int tmp;

        try {
            URL url = new URL("http://abdr.000webhostapp.com/showbookinginfo.php");
            String urlParams = "ref="+refer;

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(urlParams.getBytes());
            os.flush();
            os.close();

            InputStream is = httpURLConnection.getInputStream();
            while((tmp=is.read())!=-1){
                data+= (char)tmp;
            }

            is.close();
            httpURLConnection.disconnect();

            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        String err=null;
        prgDialog.dismiss();
        if(s.equals("false")){
            Toast.makeText(booking_info.this, "Error in loading booking ", Toast.LENGTH_LONG).show();
        }


        else {
            JSONArray array;
            String info="";
            String info1;
            String info2="";
            String info3;
            JSONObject root;
            try {
                //flight
                root = new JSONObject(s);
                array = root.getJSONArray("user_data");
                if(array.length()>0){

                    for (int i=0;i<array.length();i++){

                        info="Flight booking reference: "+array.getJSONObject(i).getString("booking_refrence")+"\n";
                        info+="Departing city: "+array.getJSONObject(i).getString("departure_city")+"\n";
                        info+="Arrival city: "+array.getJSONObject(i).getString("arrival_city")+"\n";
                        info+="Flight time: "+array.getJSONObject(i).getString("flight_time")+"\n";
                        info+="Flight date: "+array.getJSONObject(i).getString("flight_date")+"\n";
                    }
                    finfo.setText(info);
                }
                else{
                    finfo.setText("No flight found");
                }
                //luggage
                array = root.getJSONArray("user_data1");
                if(array.length()>0){


                    info1="Total luggage count: "+array.length()+"\n\n";
                    for (int i=0;i<array.length();i++){

                        info1+="Luggage brand: "+array.getJSONObject(i).getString("L_brand")+"\n";
                        info1+="Luggage color: "+array.getJSONObject(i).getString("L_color")+"\n";
                        info1+="Luggage description: "+array.getJSONObject(i).getString("L_description")+"\n";
                        info1+="Fragile luggage: "+array.getJSONObject(i).getString("fragile")+"\n";
                        info1+="Wrap luggage: "+array.getJSONObject(i).getString("wrap")+"\n\n";
                    }
                    linfo.setText(info1);
                }
                else{
                    linfo.setText("No luggage found");
                }
                //family
                array = root.getJSONArray("user_data2");
                if(array.length()>0){


                    for (int i=0;i<array.length();i++){

                        info2 +="Member name: "+array.getJSONObject(i).getString("name")+"\n\n";

                    }
                    minfo.setText(info2);
                }
                else{
                    minfo.setText("No family found");
                }

                //price
                array = root.getJSONArray("user_data3");
                if(array.length()>0){


                    info3="Price Details: \n\n";
                    for (int i=0;i<array.length();i++){

                        info3+="Luggage Price: "+array.getJSONObject(i).getString("luggage_price")+" SAR\n";
                        info3+="Distance Price: "+array.getJSONObject(i).getString("distance_price")+" SAR\n";
                        info3+="Wrapping Price: X"+((int)Double.parseDouble(array.getJSONObject(i).getString("wrapping_price"))/30)+" bag "+" "+array.getJSONObject(i).getString("wrapping_price")+" SAR\n";
                        info3+="Extra Price: "+array.getJSONObject(i).getString("extra_price")+" SAR\n";
                        info3+="Reason For Extra Price: "+array.getJSONObject(i).getString("why_extra")+"\n\n";
                        info3+="Total Price: "+fee+" SAR\n";
                    }
                    bi.msg=info3;
                }
                else{
                    info3="No Price";
                    bi.msg=info3;
                }


            } catch (JSONException e) {
                Toast.makeText(booking_info.this, "Booking info "+e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }


    }
}
    class BackGround3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int S_ID = Integer.parseInt(params[0]);
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/getrating.php");
                String urlParams = "S_ID="+S_ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;
            prgDialog.dismiss();
            if(s.equals("false")){
                Toast.makeText(booking_info.this, "Not rating", Toast.LENGTH_LONG).show();
            }


            else {

                JSONObject root = null;
                try {
                    root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    JSONObject c=array.getJSONObject(0);

                    final AlertDialog.Builder popDialog = new AlertDialog.Builder(booking_info.this);

                    final RatingBar rating = new RatingBar(booking_info.this);
                    rating.setEnabled(false);
                    rating.setNumStars(5);
                    rating.setRating(((float)c.getDouble("rating")));
                    popDialog.setIcon(R.drawable.rate);
                    popDialog.setTitle("Service Rating ");
                    popDialog.setMessage("user review : " + c.getString("review"));
                    popDialog.setView(rating);
                    popDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            });
                    AlertDialog p = popDialog.create();
                    p.show();
                    p.getWindow().setLayout(800, 700);


                } catch (JSONException e) {
                    //Toast.makeText(booking_info.this, "driver info "+e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }


        }


    }


}
