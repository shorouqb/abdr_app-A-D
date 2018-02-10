package com.example.vip.abdr_app_a_d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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

public class familymain extends BaseActivityA {
    ImageView add;

    String []names=null;
    String []images;
    String []rel;
    GridView gv;
    long []id;
    int PID;
    ProgressDialog prgDialog;
    GridAdapterfamily ga=new GridAdapterfamily(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familymain);

        PID=getIntent().getIntExtra("P_Id",0);
        prgDialog = new ProgressDialog(this);


    }
    @Override
    protected void onStart(){
        super.onStart();
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Your Family Profiles...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        familymain.BackGround b = new familymain.BackGround();
        b.execute(PID+"");


    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int P_ID = Integer.parseInt(params[0]);
            String data="";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/showfamily.php");
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
            prgDialog.dismiss();
            String err=null;
            if(s.equals("false")){
                Toast.makeText(familymain.this, "No family", Toast.LENGTH_LONG).show();
            }


            else {

                try {
                    JSONObject root = new JSONObject(s);

                    JSONArray array = root.getJSONArray("user_data");
                    if (array.length()>0) {
                        names = new String[array.length()];
                        rel = new String[array.length()];
                        images = new String[array.length()];
                        id = new long[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            names[i] = array.getJSONObject(i).getString("name");
                            id[i] = array.getJSONObject(i).getInt("id_number");
                            rel[i] = array.getJSONObject(i).getString("relationship");
                            images[i] = array.getJSONObject(i).getString("id_card_img");

                        }

                        gv = (GridView) findViewById(R.id.familygrid);
                        ga = new GridAdapterfamily(familymain.this, id, names, rel, images);
                        gv.setAdapter(ga);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(familymain.this);

                                builder.setTitle("Delete family member");

                                WebView wv = new WebView(familymain.this);
                                String data = "<div align='center'> <img src='" + images[position] + "' style='width:200px; height:150px;' /></div>";
                                wv.loadData(data, "text/html", "utf-8");
                                wv.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);

                                        return true;
                                    }
                                });

                                builder.setView(wv);
                                builder.setMessage(ga.getItem(position) + " id card :");

                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();


                            }
                        });
                    }
                    else{
                        Toast.makeText(familymain.this, "No family in this account", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //Toast.makeText(familymain.this, "family load "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }
}
