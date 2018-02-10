package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class payment extends  BaseActivity implements View.OnClickListener{
    double dprice,lprice,tprice,wprice;
    int sid;
    TextView pd;
    ImageButton back,done;
    ProgressDialog prgDialog;
    EditText rans,amount;
    Spinner res;
    String ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dprice=getIntent().getDoubleExtra("dprice",-1);
        lprice=getIntent().getDoubleExtra("lprice",-1);
        tprice=getIntent().getDoubleExtra("tprice",-1);
        wprice=getIntent().getDoubleExtra("wprice",-1);
        sid=getIntent().getIntExtra("sid",-1);
        ref=getIntent().getStringExtra("ref");
        pd=(TextView)findViewById(R.id.tv1);
        pd.setText("Luggage Price: "+lprice+" SAR\n\n" +
                "Distance Price: "+dprice+" SAR\n\n" +
                "Wrapping Price: X"+((int)wprice/30)+" bag "+" "+wprice+" SAR\n\n" +
                "Total Price: "+tprice+" SAR\n");

        back = (ImageButton)findViewById(R.id.back_btn);
        back.setOnClickListener(this);
        done = (ImageButton)findViewById(R.id.done_btn);
        done.setOnClickListener(this);
        res=(Spinner)findViewById(R.id.res);
        rans=(EditText)findViewById(R.id.et);
        amount=(EditText)findViewById(R.id.et1);
        res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position==0){
                    rans.setEnabled(false);
                    amount.setEnabled(false);
                }
                else{
                    rans.setEnabled(true);
                    amount.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        prgDialog = new ProgressDialog(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:
                prgDialog = ProgressDialog.show(this, "Wait Please...", "Making Booking Payment...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                BackGround b=new BackGround();
                String x;double a;
                if (res.getSelectedItemPosition() == 0){
                    x=res.getItemAtPosition(1).toString();
                    a=0.0;
                }
                else{
                    x=res.getSelectedItem().toString();
                    a=Double.parseDouble(amount.getText().toString());
                }
                b.execute(a,x+" : "+rans.getText().toString(),ref);
                startActivity(new Intent(payment.this, current_booking.class));

                break;

            case R.id.back_btn:
                startActivity(new Intent(payment.this, current_booking.class));
                break;

        }
    }
    class BackGround extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... params) {
            double amount = (double) params[0];
            String why = (String) params[1];
            String ref = (String) params[2];

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/driverpayment.php");
                String urlParams = "amount=" + amount+"&why="+why + "&ref=" + ref;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
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
            if (s.equals("false")) {
                Toast.makeText(payment.this, "Failed to edit", Toast.LENGTH_LONG).show();
            }
            else{
                BackGround1 bb=new BackGround1();
                bb.execute("Done",sid);
            }
        }
    }
    class BackGround1 extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... params) {
            String state = (String) params[0];
            int sid = (int) params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/updatebookingstate.php");
                String urlParams = "state=" + state + "&sid=" + sid;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
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
                Toast.makeText(payment.this, "failed to edit", Toast.LENGTH_LONG).show();
            }
           else {
                Toast.makeText(payment.this, "Booking is Done Thank You", Toast.LENGTH_LONG).show();
            }
        }
    }
}
