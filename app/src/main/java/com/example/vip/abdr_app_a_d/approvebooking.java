package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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

public class approvebooking extends BaseActivity implements View.OnClickListener{
    ProgressDialog prgDialog;
    int sid;
    ImageButton ap,nap,map;
    TextView time,date,location,family,luggage;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvebooking);
        sid=getIntent().getIntExtra("service_id",-1);
        ap=(ImageButton)findViewById(R.id.ap);
        ap.setOnClickListener(this);
        nap=(ImageButton)findViewById(R.id.nap);
        nap.setOnClickListener(this);
        map=(ImageButton)findViewById(R.id.loc);
        map.setOnClickListener(this);

        time=(TextView)findViewById(R.id.btime);
        date=(TextView)findViewById(R.id.bdate);
        location=(TextView)findViewById(R.id.blocation);
        family=(TextView)findViewById(R.id.bpnum);
        luggage=(TextView)findViewById(R.id.luggagenum);

        prgDialog = new ProgressDialog(this);
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Booking Information's...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        approvebooking.BackGround b=new approvebooking.BackGround(this);
        b.execute(sid+"");
    }

    @Override
    public void onClick(View v) {
        approvebooking.BackGround1 bb=null;
        switch (v.getId()) {
            case R.id.nap:
                //backgroud find another driver
                 bb=new approvebooking.BackGround1();
                bb.execute(sid+"" ,"no",new localuser(this).getLoggedInUser().ID,time.getText().toString(),
                        date.getText().toString(),luggage.getText().toString());
                startActivity(new Intent(approvebooking.this,mainbookings.class));
                break;
            case R.id.ap:
                //change booking state
                 bb=new approvebooking.BackGround1();
                bb.execute(sid+"","yes",new localuser(this).getLoggedInUser().ID,time.getText().toString(),
                        date.getText().toString(),luggage.getText().toString());
                new localuser(this).setsid(sid);
                startActivity(new Intent(approvebooking.this,mainbookings.class));
                break;
            case R.id.loc:
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        approvebooking a=new approvebooking();
        private BackGround(approvebooking a){
            this.a=a;
        }
        @Override
        protected String doInBackground(String... params) {
            int sid = Integer.parseInt(params[0]);

            String data="";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/bookingapproval.php");
                String urlParams = "sid="+sid;

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

            if (s.equals("false")) {
                Toast.makeText(approvebooking.this, "Error in booking..", Toast.LENGTH_LONG).show();
            }

            else{
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    JSONObject c=array.getJSONObject(0);
                    a.time.setText(c.getString("time"));
                    a.date.setText(c.getString("date"));
                    a.location.setText(c.getString("pickup_location"));
                    a.location.setMovementMethod(new ScrollingMovementMethod());
                    a.family.setText((c.getInt("familycount")+1)+" member/s");
                    a.luggage.setText(c.getInt("luggagecount")+" bag/s");
                    a.url="http://maps.google.com/?q="
                            +c.getDouble("Latitude")+","+c.getDouble("Longitude");



                } catch (JSONException e) {
                    //Toast.makeText(approvebooking.this, "login"+e.getMessage(), Toast.LENGTH_LONG);

                }

            }
        }
    }
    class BackGround1 extends AsyncTask<Object, String, String> {


        @Override
        protected String doInBackground(Object... params) {
            int sid = Integer.parseInt((String)params[0]);
            String ap=(String)params[1];
            int did=(int)params[2];
            String time=(String)params[3];
            String date=(String)params[4];
            String d=(String)params[5];
            int lcount=Integer.parseInt(d.replaceAll("[^0-9]", ""));
            String data="";
            URL url=null;
            String urlParams=null;
            int tmp;
            try {
                if (ap.equals("yes")) {
                     url = new URL("http://abdr.000webhostapp.com/updatebookingstate.php");
                     urlParams = "sid=" + sid+"&state=Approved";}
                else{
                     url = new URL("http://abdr.000webhostapp.com/changebookingdriver.php");
                     urlParams = "sid=" + sid+"&did="+did+"&time="+time+"&date="+date+"$lcount="+lcount;}
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;}
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
            if (s.equals("false")) {
                Toast.makeText(approvebooking.this, "Error in updating booking..", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(approvebooking.this, "Done..", Toast.LENGTH_LONG).show();
            }

        }
    }
}
