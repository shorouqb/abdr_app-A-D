package com.example.vip.abdr_app_a_d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class Show_admins extends BaseActivityA implements View.OnClickListener{

    String []names;
    String []emails;
    String []phones;
    String []cities;
    int [] A_Id;
    GridView gv;
    ImageButton add;
    ProgressDialog prgDialog;
    GridAdapterAdmin ga=new GridAdapterAdmin(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_admins);
        prgDialog = new ProgressDialog(this);
        add=(ImageButton)findViewById(R.id.add);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                startActivity(new Intent(this,Add_admin.class));
                break;
        }
    }
    protected void onStart(){
        super.onStart();
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Admins Profiles...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);

        Show_admins.BackGround b = new Show_admins.BackGround();
        b.execute();
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_getadmins.php");

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
                Toast.makeText(Show_admins.this, "No drivers", Toast.LENGTH_LONG).show();
            }

            else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");

                    if(array.length()>0) {
                        names=new String[array.length()];
                        phones=new String[array.length()];
                        cities=new String[array.length()];
                        emails=new String[array.length()];
                        A_Id=new int[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            names[i] = array.getJSONObject(i).getString("name");
                            phones[i] = array.getJSONObject(i).getString("phone");
                            emails[i] = array.getJSONObject(i).getString("email");
                            cities[i] = array.getJSONObject(i).getString("city");
                            A_Id[i] = array.getJSONObject(i).getInt("A_ID");
                        }
                        gv=(GridView)findViewById(R.id.admins_grid);
                        ga=new GridAdapterAdmin(Show_admins.this,A_Id,names,phones,emails,cities);
                        gv.setAdapter(ga);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Show_admins.this);
                                builder.setTitle(ga.getItem(position)+" Information :");
                                builder.setCancelable(true);
                                builder.setMessage("Admin name: "+ga.getItem(position)
                                        + "\ncity : " + ga.getCities(position)
                                        + "\nphone : " + ga.getPhones(position));
                                builder.setPositiveButton("send email", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent n = new Intent(Show_admins.this,send_email.class);
                                        n.putExtra("ID" , ga.getId(position) );
                                        n.putExtra("check" , 3 );
                                        startActivity(n);}});
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
                        Toast.makeText(Show_admins.this, "No admins ", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //Toast.makeText(Show_admins.this, "admin load "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }

}
