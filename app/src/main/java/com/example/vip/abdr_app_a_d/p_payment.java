package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class p_payment extends BaseActivityA implements View.OnClickListener {

    TextView name , card_number,ccv,exd,ct;
    ImageButton can;
    int PID;

    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_payment);

        PID=getIntent().getIntExtra("P_Id",0);
        name=(TextView) findViewById(R.id.name);
        card_number=(TextView)findViewById(R.id.cn);
        ccv=(TextView)findViewById(R.id.ccv);
        exd=(TextView)findViewById(R.id.ex);
        ct=(TextView)findViewById(R.id.ct);
        can=(ImageButton)findViewById(R.id.can);
        can.setOnClickListener(this);
        prgDialog=new ProgressDialog(this);

        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading card information...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        p_payment.BackGround b = new p_payment.BackGround();
        b.execute(PID+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.can:
                super.onBackPressed();
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            int P_ID = Integer.parseInt(params[0]);

            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/usercard.php");
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
            if (s.equals("false")){
                Toast.makeText(p_payment.this, "failed to retrieve card ", Toast.LENGTH_LONG).show();
            }else{
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    if(array.length()>0) {
                        JSONObject c = array.getJSONObject(0);
                        name.setText(c.getString("credit_card_holder_name"));
                        ccv.setText(c.getString("CCV"));
                        exd.setText(c.getString("credit_card_expriy_date"));
                        card_number.setText(c.getString("credit_card_number"));
                        ct.setText(c.getString("credit_card_type"));

                        name.setEnabled(false);
                        ccv.setEnabled(false);
                        exd.setEnabled(false);
                        ct.setEnabled(false);
                        card_number.setEnabled(false);
                    }
                    else{
                        name.setText("not specified until now");
                        ccv.setText("not specified until now");
                        exd.setText("not specified until now");
                        card_number.setText("not specified until now");
                        ct.setText("not specified until now");

                    }
                } catch (JSONException e) {
                    //Toast.makeText(p_payment.this, "payment card "+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
    }

}
