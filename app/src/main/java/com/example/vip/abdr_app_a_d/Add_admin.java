package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Add_admin extends BaseActivityA implements View.OnClickListener {

    ImageView reg,cans;
    EditText username,Pass,name,phone,email;
            Spinner city;

    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        username = (EditText)findViewById(R.id.username);
        Pass = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        city = (Spinner)findViewById(R.id.city);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);
        reg = (ImageView) findViewById(R.id.btnregister);
        reg.setOnClickListener(this);

        cans= (ImageView)findViewById(R.id.cans);
        cans.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnregister:

        if (name.getText().toString().equals("")||city.getSelectedItem().toString().equals("City:")||
                email.getText().toString().equals("")
                ||phone.getText().toString().equals("")||username.getText().toString().equals("")||
                Pass.getText().toString().equals("")) {
            Toast.makeText(Add_admin.this, "Fill the missing field..", Toast.LENGTH_LONG).show();

        }
        else{
            int atpos = email.getText().toString().indexOf("@");
            int dotpos = email.getText().toString().lastIndexOf(".");
            if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= email.getText().toString().length()) {
                Toast.makeText(Add_admin.this, "Not a valid e-mail address..", Toast.LENGTH_LONG).show();
            }
            else if (!phone.getText().toString().startsWith("5")||phone.getText().toString().length()!=9){
                Toast.makeText(Add_admin.this, "Phone format is wrong..", Toast.LENGTH_LONG).show();
            }

            else{

                prgDialog = ProgressDialog.show(this, "Wait Please...", "Creating Admin Account...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                Add_admin.BackGround1 b = new Add_admin.BackGround1();
                b.execute(email.getText().toString(),username.getText().toString(),"a");
            }
        }

        break;

        case R.id.cans:
        startActivity(new Intent(this,home.class));
        break;
    }
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[5];
            String city = params[1];
            long phone=Long.parseLong(params[3]);
            String email = params[2];
            String username = params[4];
            String data="";
            int tmp;
            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_addadmin.php");
                String urlParams = "name="+name+"&city="+city+ "&phone="+phone+"&email="+email+
                        "&username="+username+"&password="+password;
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
            if (s.equals("false")){
                Toast.makeText(Add_admin.this, "Failed to add admin..", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Add_admin.this, "Admin added..", Toast.LENGTH_LONG).show();
                Intent I = new Intent(Add_admin.this, Show_admins.class);
                startActivity(I);
            }
        }
    }

    class BackGround1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String email = params[0];
            String username = params[1];
            String flag = params[2];


            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/checkuseremail.php");
                String urlParams = "email="+email+"&username="+username+"&flag="+flag;
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
            if (s.equals("email")){
                Toast.makeText(Add_admin.this, "Failed to add admin: duplicated email..", Toast.LENGTH_LONG).show();
            }
            else if (s.equals("user")){
                Toast.makeText(Add_admin.this, "Failed to add admin: duplicated username.. ", Toast.LENGTH_LONG).show();
            }
            else {
                Add_admin.BackGround b = new Add_admin.BackGround();
                b.execute(name.getText().toString(),city.getSelectedItem().toString()
                        , email.getText().toString(),phone.getText().toString(), username.getText().toString(),
                        Pass.getText().toString());
            }
        }
    }
}
