package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class Show_passengers extends BaseActivityA {

    String []f_names;
    String []l_names;
    String []phones;
    String []nationality;
    String []city;
    String []emails;
    String [] images;
    long []id_number;
    int [] P_Id;
    GridView gv;
    ProgressDialog prgDialog;
    GridAdapterPassengerA ga=new GridAdapterPassengerA(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_passengers);

        prgDialog = new ProgressDialog(this);
    }
    protected void onStart(){
        super.onStart();
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Passengers Profiles...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);

        Show_passengers.BackGround b = new Show_passengers.BackGround();
        b.execute();
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_Allpassengers.php");

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
                Toast.makeText(Show_passengers.this, "No passengers", Toast.LENGTH_LONG).show();
            }

            else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");

                    if(array.length()>0) {
                        f_names=new String[array.length()];
                        l_names=new String[array.length()];
                        phones=new String[array.length()];
                        nationality=new String[array.length()];
                        images=new String[array.length()];
                        id_number=new long[array.length()];
                        city=new String[array.length()];
                        emails=new String[array.length()];
                        P_Id=new int[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            f_names[i] = array.getJSONObject(i).getString("first_name");
                            l_names[i] = array.getJSONObject(i).getString("last_name");
                            id_number[i] = array.getJSONObject(i).getInt("id_number");
                            phones[i] = array.getJSONObject(i).getString("phone");
                            images[i] = array.getJSONObject(i).getString("id_card_img");
                            nationality[i] = array.getJSONObject(i).getString("nationality");
                            city[i] = array.getJSONObject(i).getString("city");
                            emails[i] = array.getJSONObject(i).getString("email");
                            P_Id[i] = array.getJSONObject(i).getInt("P_ID");
                        }
                        gv=(GridView)findViewById(R.id.passengers_grid);
                        ga=new GridAdapterPassengerA(Show_passengers.this,id_number,f_names,l_names,phones,nationality,images,city,emails,P_Id);
                        gv.setAdapter(ga);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                Intent n = new Intent(Show_passengers.this,passenger_info.class);
                                n.putExtra("nameF",f_names );
                                n.putExtra("nameL",l_names);
                                n.putExtra("id_number",id_number);
                                n.putExtra("phones",phones);
                                n.putExtra("images",images);
                                n.putExtra("nationality",nationality);
                                n.putExtra("city",city);
                                n.putExtra("emails",emails);
                                n.putExtra("P_Id",P_Id);
                                n.putExtra("pos",position);
                                startActivity(n);
                            }
                        });
                    }
                    else {
                        Toast.makeText(Show_passengers.this, "No passengers ", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //Toast.makeText(Show_passengers.this, "passengers load "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }


}
