
package com.example.vip.abdr_app_a_d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class userinfo extends BaseActivity implements View.OnClickListener {
    EditText username,Pass,fname,id,nationality,city,phone,email;
    localuser LU;
    adminDriver p;
    String img="null",msg;
    int p_id;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ImageView addimg;
    ImageButton im,editdb,car;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        username = (EditText)findViewById(R.id.username);
        Pass = (EditText)findViewById(R.id.password);
        fname = (EditText)findViewById(R.id.name);
        id = (EditText)findViewById(R.id.idno);
        nationality = (EditText)findViewById(R.id.nationality);
        city = (EditText)findViewById(R.id.city);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);


        im = (ImageButton)findViewById(R.id.edit);
        im.setOnClickListener(this);

        car = (ImageButton)findViewById(R.id.carinfo);
        car.setOnClickListener(this);

        addimg = (ImageView)findViewById(R.id.img);
        addimg.setOnClickListener(this);;


        editdb = (ImageButton)findViewById(R.id.editdb);
        editdb.setOnClickListener(this);
        editdb.setVisibility(View.GONE);

        username.setEnabled(false);
        Pass.setEnabled(false);
        fname.setEnabled(false);
        id.setEnabled(false);
        nationality.setEnabled(false);
        city.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);

        LU=new localuser(this);


        prgDialog = new ProgressDialog(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(LU.getIfLoggedInUser()) {
            p=LU.getLoggedInUser();
            //prgDialog.cancel();
            prgDialog = ProgressDialog.show(userinfo.this, "Wait Please...", "Loading Your Profile...", false, true);
            prgDialog.setCancelable(false);
            prgDialog.setCanceledOnTouchOutside(false);
            BackGround1 bb= new BackGround1(this);
            bb.execute(p.ID);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edit:
                username.setEnabled(true);
                Pass.setEnabled(true);

                phone.setEnabled(true);
                email.setEnabled(true);

                im.setVisibility(View.GONE);
                editdb.setVisibility(View.VISIBLE);
                break;

            case R.id.editdb:
                prgDialog = ProgressDialog.show(this, "Wait Please...", "Updating Your Profile...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                userinfo.BackGround b = new userinfo.BackGround();
                b.execute(email.getText().toString(),phone.getText().toString(), username.getText().toString(),
                        Pass.getText().toString(),p_id+"");

                editdb.setVisibility(View.GONE);
                im.setVisibility(View.VISIBLE);
                username.setEnabled(false);
                Pass.setEnabled(false);
                phone.setEnabled(false);
                email.setEnabled(false);
                break;
            case R.id.img:
            AlertDialog.Builder builder = new AlertDialog.Builder(userinfo.this);
            builder.setTitle("Image");
            ImageView image = new ImageView(userinfo.this);
            if(!img.equals("null")&&!img.equals("")){
                WebView wv = new WebView(userinfo.this);
                String data="<div align='center'> <img src='"+img+"' style='width:200px; height:150px;' /></div>";
                wv.loadData(data, "text/html", "utf-8");
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                builder.setView(wv);
            }
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setLayout(200, 400);
            dialog.show();
            break;
            case R.id.carinfo:
                builder = new AlertDialog.Builder(userinfo.this);
                builder.setTitle("Car Information");
                builder.setMessage(msg);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                 dialog = builder.create();
                dialog.getWindow().setLayout(200, 400);
                dialog.show();
                break;

        }
    }


    class BackGround extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... params) {
            long phone=  Long.parseLong((String)params[1]);
            String email = (String)params[0];
            String username = (String)params[2];
            String password = (String)params[3];
            int did=Integer.parseInt((String)params[4]);


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/editdriver.php");
                String urlParams = "phone="+phone+"&email="+email+"&username="+username+"&password="+password+"&D_ID="+did;

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
                Toast.makeText(userinfo.this, "Failed to edit", Toast.LENGTH_LONG).show();
            }
            else{

                Toast.makeText(userinfo.this, "Profile Updated", Toast.LENGTH_LONG).show();

        }

        }
    }

    class BackGround1 extends AsyncTask<Object, String, String> {
        userinfo u;
       public BackGround1(userinfo u){
           this.u=u;
       }
        @Override
        protected String doInBackground(Object... params) {
            int did=(int)params[0];


            String data = "";
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
                Toast.makeText(userinfo.this, "Failed to load", Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    JSONObject c=array.getJSONObject(0);
                    u.username.setText(c.getString("username"));
                    u.Pass.setText(c.getString("password"));
                    u.fname.setText(c.getString("name"));
                    u.id.setText(c.getString("id_number"));
                    u.nationality.setText(c.getString("nationality"));
                    u.city.setText(c.getString("city"));
                    u.phone.setText(c.getInt("phone")+"");
                    u.email.setText(c.getString("email"));
                    u.msg="Car Brand: "+c.getString("car_brand")+"\n"+"Car Color: "+c.getString("car_color")
                            +"\n"+"Car Plate: "+c.getString("plate_number");
                    u.img=c.getString("photo");
                } catch (JSONException e) {
                    //Toast.makeText(userinfo.this, "user load "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

        }
    }

}

