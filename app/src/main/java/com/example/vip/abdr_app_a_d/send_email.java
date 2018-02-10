package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class send_email extends BaseActivityA implements View.OnClickListener{

    int ID;
    ImageView send;
    ImageView clean;
    EditText message_editText;
    ProgressDialog prgDialog;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        ID = getIntent().getIntExtra("ID",0);
        check=getIntent().getIntExtra("check",0);
        send =(ImageView) findViewById(R.id.send_btn);
        send.setOnClickListener( this);
        clean=(ImageView) findViewById(R.id.clean_btn);
        clean.setOnClickListener(this);
        message_editText =(EditText)findViewById(R.id.message_editText);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.send_btn:
                String mess;
                if(check == 1){
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Sending Email To User...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    mess = message_editText.getText().toString();
                    send_email.BackGroundU b = new send_email.BackGroundU();
                    b.execute(ID+"",mess);
                }else if(check == 2){
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Sending Email To Driver...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    mess = message_editText.getText().toString();
                    send_email.BackGroundD b = new send_email.BackGroundD();
                    b.execute(ID+"",mess);}
                else if(check == 3){
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Sending Email To Admin...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    mess = message_editText.getText().toString();
                    send_email.BackGroundA b = new send_email.BackGroundA();
                    b.execute(ID+"",mess);}

                break;
            case R.id.clean_btn:
                message_editText.setText("");

        }

    }
    class BackGroundA extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {
            int A_ID = Integer.parseInt(params[0]);
            String message = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_adminemailsend.php");
                String urlParams = "A_ID="+A_ID+"&message="+message;

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
            if(s.equals("false")){
                Toast.makeText(send_email.this, "Not sent", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(send_email.this, "sent", Toast.LENGTH_LONG).show();
            }
            send_email.this.onBackPressed();
        }
    }

    class BackGroundU extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {
            int P_ID = Integer.parseInt(params[0]);
            String message = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_useremailsend.php");
                String urlParams = "P_ID="+P_ID+"&message="+message;

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
            if(s.equals("false")){
                Toast.makeText(send_email.this, "Not sent", Toast.LENGTH_LONG).show();
            }
            send_email.this.onBackPressed();
        }
    }

    class BackGroundD extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {
            int D_ID = Integer.parseInt(params[0]);
            String message = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_driveremailsend.php");
                String urlParams = "d_id="+D_ID+"&message="+message;

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
            if(s.equals("true")){
                Toast.makeText(send_email.this, "Sent", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(send_email.this, "Not sent", Toast.LENGTH_LONG).show();
            }
            send_email.this.onBackPressed();
        }
    }




}
