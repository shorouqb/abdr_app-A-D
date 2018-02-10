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
import android.widget.GridView;
import android.widget.ImageButton;
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

public class Show_drivers extends BaseActivityA implements View.OnClickListener{

    String []names;
    String []images;
    String []phones;
    String []nationality;
    long []id;
    String [] car_color;
    String [] car_brand;
    String [] car_plate_number;
    int [] D_Id;
    GridView gv;
    ImageButton add;
    ProgressDialog prgDialog;
    GridAdapterDriver ga=new GridAdapterDriver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_drivers);
        prgDialog = new ProgressDialog(this);
add=(ImageButton)findViewById(R.id.add);
        add.setOnClickListener(this);
    }

    protected void onStart(){
        super.onStart();
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Drivers Profiles...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);

        Show_drivers.BackGround b = new Show_drivers.BackGround();
        b.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                startActivity(new Intent(this,Add_driver.class));
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_showdrivers.php");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
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
            String err=null;
            if(s.equals("false")){
                Toast.makeText(Show_drivers.this, "No drivers", Toast.LENGTH_LONG).show();
            }

            else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");

                    if(array.length()>0){
                        names=new String[array.length()];
                        phones=new String[array.length()];
                        nationality=new String[array.length()];
                        images=new String[array.length()];
                        id=new long[array.length()];
                        car_color=new String[array.length()];
                        car_brand=new String[array.length()];
                        car_plate_number=new String[array.length()];
                        D_Id=new int[array.length()];
                        for (int i=0;i<array.length();i++){
                        names[i]=array.getJSONObject(i).getString("name");
                        id[i]=array.getJSONObject(i).getInt("id_number");
                        phones[i]=array.getJSONObject(i).getString("phone");
                        images[i]=array.getJSONObject(i).getString("photo");
                        nationality[i]=array.getJSONObject(i).getString("nationality");
                        car_color[i]=array.getJSONObject(i).getString("car_color");
                        car_brand[i]=array.getJSONObject(i).getString("car_brand");
                        car_plate_number[i]=array.getJSONObject(i).getString("plate_number");
                        D_Id[i]=array.getJSONObject(i).getInt("D_ID");
                        }
                        gv=(GridView)findViewById(R.id.drivers_grid);
                        ga=new GridAdapterDriver(Show_drivers.this,id,names,phones,nationality,images,car_color,car_brand,car_plate_number,D_Id);
                        gv.setAdapter(ga);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Show_drivers.this);
                                builder.setTitle(ga.getItem(position)+" Information :");
                                WebView wv = new WebView(Show_drivers.this);
                                String data="<div align='center'> <img src='"+images[position]+"' style='width:200px; height:150px;' /></div>";
                                wv.loadData(data, "text/html", "utf-8");
                                wv.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        return true;}});
                                builder.setView(wv);
                                builder.setCancelable(true);
                                builder.setMessage("Driver name: "+ga.getItem(position)
                                        + "\nId : " + ga.getId(position)
                                        + "\nNationality : " + ga.getNationality(position)
                                        + "\nPhone : " + ga.getPhones(position)
                                        + "\nCar color : " + ga.getCar_color(position)
                                        + "\nCar brand : " + ga.getCar_brand(position)
                                        + "\nPlate number : " + ga.getCar_plate_number(position));
                                builder.setPositiveButton("Send email", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent n = new Intent(Show_drivers.this,send_email.class);
                                        n.putExtra("ID" , ga.getD_ID(position) );
                                        n.putExtra("check" , 2 );
                                        startActivity(n);
                                    }
                                });
                                builder.setNegativeButton("call", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ga.getPhones(position), null)));
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }

                    else {
                        Toast.makeText(Show_drivers.this, "No drivers ", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //Toast.makeText(Show_drivers.this, "Driver load "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }


}
