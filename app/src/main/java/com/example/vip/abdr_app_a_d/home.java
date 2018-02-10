package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class home extends BaseActivityA {

    TextView username,Pass,name,city,phone,email;
    int A_ID;

    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        username = (TextView)findViewById(R.id.username);
        username.setEnabled(false);
        Pass = (TextView)findViewById(R.id.password);
        Pass.setEnabled(false);
        name = (TextView)findViewById(R.id.name);
        name.setEnabled(false);
        city = (TextView)findViewById(R.id.city);
        city.setEnabled(false);
        phone = (TextView)findViewById(R.id.phone);
        phone.setEnabled(false);
        email = (TextView)findViewById(R.id.email);
        email.setEnabled(false);

        A_ID=new localuser(this).getLoggedInUser().ID;
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Your Profile...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        home.BackGround b = new home.BackGround();
        b.execute(A_ID+"");
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            int A_ID = Integer.parseInt(params[0]);

            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admininfo.php");
                String urlParams = "A_ID="+A_ID;
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
            if (s.equals("false")){
                Toast.makeText(home.this, "no admin info", Toast.LENGTH_LONG).show();
            }else{
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    JSONObject c = array.getJSONObject(0);
                    name.setText(c.getString("name"));
                    city.setText(c.getString("city"));
                    phone.setText(c.getString("phone"));
                    username.setText(c.getString("username"));
                    Pass.setText(c.getString("password"));
                    email.setText(c.getString("email"));


                } catch (JSONException e) {
                    //Toast.makeText(home.this, "Admin info lode "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
    }


}
