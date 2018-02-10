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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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

public class current_booking extends BaseActivity implements  View.OnClickListener {
    TextView stat, msg, fight, scan, pay, arrive, pick, line, st;
    ImageButton arr, pic, bscan, payment, fli;
    Button contact, loc;
    AlertDialog.Builder builder;AlertDialog dialog;
    String flightinfo, phone, loca, status,bref;
    int sid;
    ProgressDialog prgDialog;
    String[] name;
    String[] link;
    long[] id;
    GridAdapterpassengers ga;
    GridView fgv;
    double dprice,lprice,tprice,wprice;
    int[]lid;
    int lcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_booking);
        st = (TextView) findViewById(R.id.tv1);

        stat = (TextView) findViewById(R.id.textViewstatus);
        msg = (TextView) findViewById(R.id.tv0);
        fight = (TextView) findViewById(R.id.tv4);
        scan = (TextView) findViewById(R.id.tv5);
        pay = (TextView) findViewById(R.id.tv6);
        arrive = (TextView) findViewById(R.id.tv3);
        pick = (TextView) findViewById(R.id.tv2);
        line = (TextView) findViewById(R.id.tv);

        arr = (ImageButton) findViewById(R.id.arrive_btn);
        pic = (ImageButton) findViewById(R.id.pickup_btn);
        bscan = (ImageButton) findViewById(R.id.scan_btn);
        payment = (ImageButton) findViewById(R.id.pay_btn);
        fli = (ImageButton) findViewById(R.id.flight_btn);

        arr.setOnClickListener(this);
        pic.setOnClickListener(this);
        bscan.setOnClickListener(this);
        payment.setOnClickListener(this);
        fli.setOnClickListener(this);


        contact = (Button) findViewById(R.id.contact_btn);
        loc = (Button) findViewById(R.id.loc_btn);

        contact.setOnClickListener(this);
        loc.setOnClickListener(this);

        fgv = (GridView) findViewById(R.id.familygrid);




    }
    @Override
    public void onStart(){
    super.onStart();
        prgDialog = new ProgressDialog(this);
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Your Booking...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);

        //background
        BackGround b = new BackGround(this);
        b.execute();
        if (status!=null){
            if (status.equals("Scanned")) {
                bscan.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pay_btn:
                Intent i=new Intent(current_booking.this, payment.class);
                i.putExtra("lprice",lprice);
                i.putExtra("tprice",tprice);
                i.putExtra("wprice",wprice);
                i.putExtra("dprice",dprice);
                i.putExtra("sid",sid);
                i.putExtra("ref",bref);
                startActivity(i);
                break;

            case R.id.scan_btn:
                Bundle b=new Bundle();
                b.putIntArray("lid",lid);
                Intent ii=new Intent(current_booking.this, scan.class);
                ii.putExtras(b);
                ii.putExtra("sid",sid);
                startActivity(ii);
                break;

            case R.id.arrive_btn:
                builder = new AlertDialog.Builder(current_booking.this);

                builder.setTitle("Changing Booking Status");
                builder.setMessage("Have You Arrived at the Airport ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        prgDialog = ProgressDialog.show(current_booking.this, "Wait Please...", "Updating Current Bookings...", false, true);
                        prgDialog.setCancelable(false);
                        prgDialog.setCanceledOnTouchOutside(false);
                        BackGround1 b=new BackGround1(current_booking.this);
                        b.execute("Arrived",sid);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();

                break;
            case R.id.pickup_btn:
                builder = new AlertDialog.Builder(current_booking.this);

                builder.setTitle("Changing Booking Status");
                builder.setMessage("Have You Picked-up Passengers Luggage ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        prgDialog = ProgressDialog.show(current_booking.this, "Wait Please...", "Updating Current Bookings...", false, true);
                        prgDialog.setCancelable(false);
                        prgDialog.setCanceledOnTouchOutside(false);
                        BackGround1 bb=new BackGround1(current_booking.this);
                        bb.execute("Pickedup",sid);

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                 dialog = builder.create();
                dialog.show();


                break;
            case R.id.contact_btn:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
                break;

            case R.id.loc_btn:
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loca));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                break;
            case R.id.flight_btn:
                 builder = new AlertDialog.Builder(current_booking.this);

                builder.setTitle("Passenger Flight Information");
                builder.setMessage(flightinfo);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                 dialog = builder.create();
                dialog.show();
                break;
        }
    }

    class BackGround extends AsyncTask<Object, String, String> {
        current_booking cb;

        public BackGround(current_booking c) {
            this.cb = c;
        }

        @Override
        protected String doInBackground(Object... params) {
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/currentbookingdriver.php");
                String urlParams = "D_ID=" + new localuser(current_booking.this).getLoggedInUser().ID;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            prgDialog.dismiss();
            if (s.equals("false")) {
                Toast.makeText(current_booking.this, "Failed to load", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject root = new JSONObject(s);
                    final JSONArray array = root.getJSONArray("user_data");
                    if (array.length() > 0) {
                        cb.msg.setVisibility(View.GONE);
                        cb.flightinfo = "Passenger Flight Information";
                        cb.flightinfo = "Flight booking reference: " + array.getJSONObject(0).getString("booking_refrence") + "\n";
                        cb.flightinfo += "Departing city: " + array.getJSONObject(0).getString("departure_city") + "\n";
                        cb.flightinfo += "Arrival city: " + array.getJSONObject(0).getString("arrival_city") + "\n";
                        cb.flightinfo += "Flight time: " + array.getJSONObject(0).getString("flight_time") + "\n";
                        cb.flightinfo += "Flight date: " + array.getJSONObject(0).getString("flight_date") + "\n";
                        cb.phone = array.getJSONObject(0).getString("phone");
                        cb.loca = "http://maps.google.com/?q="
                                + array.getJSONObject(0).getString("Latitude") + "," + array.getJSONObject(0).getString("Longitude");
                        cb.status = array.getJSONObject(0).getString("status");
                        cb.stat.setText(status);
                        if (cb.status.equals("Scanned")) {
                            bscan.setEnabled(false);
                        }
                        cb.sid=array.getJSONObject(0).getInt("service_id");
                        cb.bref=array.getJSONObject(0).getString("booking_refrence");
                        cb.dprice=array.getJSONObject(0).getDouble("distance_price");
                        cb.lprice=array.getJSONObject(0).getDouble("luggage_price");
                        cb.tprice=array.getJSONObject(0).getDouble("service_fee");
                        cb.wprice=array.getJSONObject(0).getDouble("wrapping_price");
                        JSONArray array2 = root.getJSONArray("user_data2");
                        if (array2.length() > 0) {
                            cb.lid=new int[array2.length()];
                            for (int i = 0; i < array2.length(); i++) {
                                cb.lid[i]= array2.getJSONObject(i).getInt("L_ID");
                            }
                            }
                        else{
                            cb.bscan.setEnabled(false);
                        }
                        JSONArray array1 = root.getJSONArray("user_data1");
                        cb.id = new long[array1.length() + 1];
                        cb.name = new String[array1.length() + 1];
                        cb.link = new String[array1.length() + 1];
                        if (array1.length() > 0) {
                            for (int i = 0; i < array1.length(); i++) {
                                cb.id[i] = array1.getJSONObject(i).getLong("id_number");
                                cb.name[i] = array1.getJSONObject(i).getString("name");
                                cb.link[i] = array1.getJSONObject(i).getString("id_card_img");}}
                            cb.id[array1.length()] = array.getJSONObject(0).getLong("id_number");
                            cb.name[array1.length()] = array.getJSONObject(0).getString("first_name") + " " + array.getJSONObject(0).getString("last_name");
                            cb.link[array1.length()] = array.getJSONObject(0).getString("id_card_img");
                        fgv = (GridView) findViewById(R.id.familygrid);
                        ga = new GridAdapterpassengers(current_booking.this, id, name, link);
                        fgv.setAdapter(ga);
                        fgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(current_booking.this);
                                builder.setTitle("Id image");
                                if (!cb.link[position].equals("null") && !cb.link[position].equals("")) {
                                    WebView wv = new WebView(current_booking.this);
                                    String data = "<div align='center'> <img src='" + cb.link[position] + "' style='width:200px; height:150px;' /></div>";
                                    wv.loadData(data, "text/html", "utf-8");
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);
                                            return true;}
                                    });
                                    builder.setView(wv);}
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();} });
                                AlertDialog dialog = builder.create();
                                dialog.getWindow().setLayout(200, 400);
                                dialog.show();}
                        });
                    } else {
                        cb.stat.setVisibility(View.GONE);
                        cb.st.setVisibility(View.GONE);
                        cb.fight.setVisibility(View.GONE);
                        cb.scan.setVisibility(View.GONE);
                        cb.pay.setVisibility(View.GONE);
                        cb.arrive.setVisibility(View.GONE);
                        cb.pick.setVisibility(View.GONE);
                        cb.line.setVisibility(View.GONE);
                        cb.arr.setVisibility(View.GONE);
                        cb.pic.setVisibility(View.GONE);
                        cb.bscan.setVisibility(View.GONE);
                        cb.payment.setVisibility(View.GONE);
                        cb.fli.setVisibility(View.GONE);
                        cb.contact.setVisibility(View.GONE);
                        cb.loc.setVisibility(View.GONE);
                        cb.fgv.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    //Toast.makeText(current_booking.this, "current booking info " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

        }
    }


    class BackGround1 extends AsyncTask<Object, String, String> {
        String state;
        current_booking cb;

        public BackGround1(current_booking c) {
            this.cb = c;
        }
        @Override
        protected String doInBackground(Object... params) {
             state = (String) params[0];
            int sid = (int) params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/updatebookingstate.php");
                String urlParams = "state=" + state + "&sid=" + sid;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            prgDialog.dismiss();
            if (s.equals("false")) {
                Toast.makeText(current_booking.this, "Failed to edit", Toast.LENGTH_LONG).show();
            }
            else{
                cb.status=state;
                stat.setText(cb.status);
            }

        }
    }

}