package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class bookinginfo extends BaseActivity implements View.OnClickListener{
    String ref;
    String state;
    int user;
    int id;
    String time;
    String date;
    String  location;
    int pos;
    Intent intent;
    TextView status,time_,date_,finfo,minfo,linfo;
    ImageButton sup,em;
    Button dri;


    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinginfo);



        sup = (ImageButton) findViewById(R.id.support);
        sup.setOnClickListener(this);

        em = (ImageButton) findViewById(R.id.email);
        em.setOnClickListener(this);

        dri = (Button) findViewById(R.id.driver);
        dri.setOnClickListener(this);
        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False

        prgDialog.onBackPressed();
        finfo = (TextView) findViewById(R.id.flightinfo);
        minfo = (TextView) findViewById(R.id.passengersinfo);
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
        user=i.getIntArrayExtra("P_ID")[pos];
        id=i.getIntArrayExtra("service_id")[pos];
        time=i.getStringArrayExtra("time")[pos];
        date=i.getStringArrayExtra("date")[pos];
        location=i.getStringArrayExtra("location")[pos];

        bookinginfo.BackGround2 bb=new bookinginfo.BackGround2(this);
        bb.execute(ref);
        status=(TextView)findViewById(R.id.status);
        time_=(TextView)findViewById(R.id.time);
        date_=(TextView)findViewById(R.id.date);

        status.setText(state);
        time_.setText(time);
        date_.setText(date);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.driver:
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                break;
            case R.id.support:
               Intent i=new Intent(bookinginfo.this,addsupport.class);
                 i.putExtra("ref",ref);
                 startActivity(i);
                break;
            case R.id.email:
                prgDialog = ProgressDialog.show(this, "Wait Please...", "Sending Booking Information's...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                bookinginfo.EmailBackGround bbb=new bookinginfo.EmailBackGround();
                bbb.execute(ref,new localuser(this).getLoggedInUser().ID+"");
                break;
        }

    }

    class BackGround2 extends AsyncTask<String, String, String> {
        private bookinginfo bi;
        public BackGround2(bookinginfo b){
           this.bi=b;
        }
        @Override
        protected String doInBackground(String... params) {
            String  refer = params[0];
            String data="";
            int tmp;

            try {//192.168.56.1
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
                Toast.makeText(bookinginfo.this, "Error in loading booking ", Toast.LENGTH_LONG).show();}
            else {
                JSONArray array;
                String info="";
                String info1;
                String info2="";
                JSONObject root;
                try {
                    //flight
                 root = new JSONObject(s);
                 array = root.getJSONArray("user_data");
                    if(array.length()>0){
                    info="Flight booking reference: "+array.getJSONObject(0).getString("booking_refrence")+"\n";
                    info+="Departing city: "+array.getJSONObject(0).getString("departure_city")+"\n";
                    info+="Arrival city: "+array.getJSONObject(0).getString("arrival_city")+"\n";
                    info+="Flight time: "+array.getJSONObject(0).getString("flight_time")+"\n";
                    info+="Flight date: "+array.getJSONObject(0).getString("flight_date")+"\n";
                        info2="Main Member name: " + array.getJSONObject(0).getString("first_name")+" "+array.getJSONObject(0).getString("last_name") + "\n\n";
                        finfo.setText(info);}
                    else{
                        finfo.setText("No flight found");}
                    //luggage
                    array = root.getJSONArray("user_data1");
                    if(array.length()>0){
                        info1="Total luggage count: "+array.length()+"\n\n";
                        for (int i=0;i<array.length();i++){
                            info1+="Luggage brand: "+array.getJSONObject(i).getString("L_brand")+"\n";
                            info1+="Luggage color: "+array.getJSONObject(i).getString("L_color")+"\n";
                            info1+="Luggage description: "+array.getJSONObject(i).getString("L_description")+"\n";
                            info1+="Fragile luggage: "+array.getJSONObject(i).getString("fragile")+"\n";
                            info1+="Wrap luggage: "+array.getJSONObject(i).getString("wrap")+"\n\n\n";}
                        linfo.setText(info1);}
                    else{
                        linfo.setText("No luggage found");
                    }//family
                    array = root.getJSONArray("user_data2");
                    if(array.length()>0) {
                        for (int i = 0; i < array.length(); i++) {
                            info2 += "Member name: " + array.getJSONObject(i).getString("name") + "\n\n";
                        }
                    }
                    minfo.setText(info2);
                } catch (JSONException e) {
                    //.makeText(bookinginfo.this, "booking info "+e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }


        }
    }


    class EmailBackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String r=params[0];
            int did = Integer.parseInt(params[1]);
            String data="";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/emailbooking.php");
                String urlParams = "P_Id= "+"&b_refernce="+r+"&change=no &user=d &d_Id"+did;

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
            prgDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(bookinginfo.this, "Sent", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(bookinginfo.this, "Not Sent", Toast.LENGTH_LONG).show();
            }
        }


    }
}
