package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class main_send_email extends BaseActivityA implements View.OnClickListener {


    ProgressDialog prgDialog;
    Spinner flag;
    ImageView send;
    ImageView clean;
    EditText message_editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_send_email);
        flag=(Spinner) findViewById(R.id.spinner_f);
        send =(ImageView) findViewById(R.id.send_btn);
        send.setOnClickListener( this);
        clean=(ImageView) findViewById(R.id.clean_btn);
        clean.setOnClickListener(this);
        message_editText =(EditText)findViewById(R.id.message_editText);
        prgDialog = new ProgressDialog(this);
    }
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.send_btn:
                if(flag.getSelectedItem().toString().equals("Select")) {
                    showAlert();
                }else {
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Sending emails...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    String flag_S = "";
                    if (flag.getSelectedItem().toString().equals("To drivers")){
                        flag_S = "d";}
                    else if (flag.getSelectedItem().toString().equals("To passengers")){
                        flag_S = "p";}
                    else{
                        flag_S=flag.getSelectedItem().toString();}
                    String mess = message_editText.getText().toString();
                    main_send_email.BackGround b = new main_send_email.BackGround();
                    b.execute(flag_S ,mess);
                }
                break;
            case R.id.clean_btn:
                message_editText.setText("");

        }
    }

    public void showAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Please you have to choose target users how will receive message ..");

        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    class BackGround extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {
            String message = params[1];
            String flag = params[0];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_sendemailforall.php");
                String urlParams = "message="+message+"&flag="+flag;

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
                Toast.makeText(main_send_email.this, "Sent", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(main_send_email.this, "Not Sent" , Toast.LENGTH_LONG).show();
            }
            main_send_email.this.onBackPressed();
        }
    }
}
