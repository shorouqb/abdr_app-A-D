package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class bookings extends BaseActivityA implements View.OnClickListener{

    Spinner order;
    Spinner filter;
    ImageButton search;

    String []ref;
    String []state;
    int [] driver;
    int [] users;
    int [] sid;
    String  [] time;
    String  [] date;
    double []fee;
    GridView gv;
    GridAdapterbooking_a ga=new GridAdapterbooking_a(this);

    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        order=(Spinner) findViewById(R.id.spinner_o);
        filter=(Spinner) findViewById(R.id.spinner_s);

        search = (ImageButton) findViewById(R.id.Search_btn);
        search.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Bookings...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        bookings.BackGround b = new bookings.BackGround();
        b.execute("All", "descending");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Search_btn:
                if(order.getSelectedItem().toString().equals("Select") || filter.getSelectedItem().toString().equals("Select")) {
                    showAlert();
                }else {
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Bookings...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    String order_S= order.getSelectedItem().toString();
                    String filter_S= filter.getSelectedItem().toString();
                    bookings.BackGround b = new bookings.BackGround();
                    b.execute(filter_S,order_S);
                }
                break;
        }
    }
    public void showAlert(){

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Please you have to choose all (Fiters and Order) ..");

        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String status = params[0];
            String order = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_AllBookings.php");
                String urlParams = "status="+status+"&order="+order;

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
                Toast.makeText(bookings.this, "No bookings", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    JSONObject root = new JSONObject(s);

                    JSONArray array = root.getJSONArray("user_data");
                    sid=new int[array.length()];
                    ref =new String[array.length()];
                    state =new String[array.length()];
                    driver=new int[array.length()];
                    users=new int[array.length()];
                    time=new String [array.length()];
                    date=new String [array.length()];
                    fee=new double[array.length()];

                    if(array.length()>0){
                    for (int i=0;i<array.length();i++){
                        sid[i]=array.getJSONObject(i).getInt("service_id");
                        ref[i] =array.getJSONObject(i).getString("booking_reference");
                        state[i] =array.getJSONObject(i).getString("status");
                        driver[i]=array.getJSONObject(i).getInt("D_ID");
                        time[i]=array.getJSONObject(i).getString("time");
                        date[i]=array.getJSONObject(i).getString("date");
                        fee[i]=array.getJSONObject(i).getDouble("service_fee");
                        users[i]=array.getJSONObject(i).getInt("P_ID");
                    }

                   }
                    else{
                    Toast.makeText(bookings.this, "No bookings according to your filters", Toast.LENGTH_LONG).show();
                    }
                    gv=(GridView)findViewById(R.id.bookings_grid);
                    ga=new GridAdapterbooking_a(bookings.this,sid,ref,state,driver,time,date,fee);
                    gv.setAdapter(ga);
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Intent i=new Intent(bookings.this,booking_info.class);
                            i.putExtra("service_id",sid);
                            i.putExtra("booking_reference",ref);
                            i.putExtra("status",state);
                            i.putExtra("D_ID",driver);
                            i.putExtra("P_ID",users);
                            i.putExtra("time",time);
                            i.putExtra("date",date);
                            i.putExtra("service_fee",fee);
                            i.putExtra("pos",position);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    //Toast.makeText(bookings.this, "booking load"+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }


}
