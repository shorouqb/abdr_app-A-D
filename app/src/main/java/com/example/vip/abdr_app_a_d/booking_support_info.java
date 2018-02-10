package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import java.util.concurrent.ExecutionException;

public class booking_support_info extends BaseActivityA implements View.OnClickListener {

    String ref;
    TextView bref;
    TextView dcom;
    TextView pcom;
    ProgressDialog prgDialog;
    ImageView panswer ,danswer;
    int d_com_pos;
    int p_com_pos;
    int []c_id;
    String[]flag;
    String[]comtitle;
    String[]complains;
    String[]complains_s;
    String[]answers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_support_info);
        Intent i = getIntent();
        ref=i.getStringExtra("ref");

        bref=(TextView)findViewById(R.id.bookref);
        dcom=(TextView)findViewById(R.id.dcom);
        pcom=(TextView)findViewById(R.id.pcom);
        panswer=(ImageView)findViewById(R.id.panswer);
        danswer=(ImageView)findViewById(R.id.danswer);
    }
    protected void onStart(){
        super.onStart();
        // info from query
        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Booking...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        try {
            booking_support_info.BackGround b= new booking_support_info.BackGround();
            String s = b.execute(ref).get();
            getInfo(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void getInfo(String s){

        String err=null;
        prgDialog.dismiss();
        if(s.equals("false")){
            Toast.makeText(booking_support_info.this, "No complaints", Toast.LENGTH_LONG).show();
        }
        else {

            try {
                JSONObject root = new JSONObject(s);

                JSONArray array = root.getJSONArray("user_data");
                flag=new String[array.length()];
                comtitle=new String[array.length()];
                complains=new String[array.length()];
                complains_s=new String[array.length()];
                answers=new String[array.length()];
                c_id=new int[array.length()];
                for (int i=0;i<array.length();i++){
                    flag[i]=array.getJSONObject(i).getString("flag");
                    comtitle[i]=array.getJSONObject(i).getString("title");
                    complains[i]=array.getJSONObject(i).getString("complain_descreption");
                    complains_s[i]=array.getJSONObject(i).getString("complain_status");
                    answers[i]=array.getJSONObject(i).getString("admin_answer");
                    c_id[i]=array.getJSONObject(i).getInt("complain_id");
                }
                bref.setText(ref);
                if(flag.length ==0){
                    pcom.setText("NO Complints From Passenger");
                    dcom.setText("NO Complints From Driver");
                    danswer.setVisibility(View.GONE);
                    panswer.setVisibility(View.GONE);
                }if(flag.length ==1){
                    if(flag[0].equals("p")){
                        dcom.setText("NO Complints From Driver");
                        danswer.setVisibility(View.GONE);
                    }else{
                        pcom.setText("NO Complints From Passenger");
                        panswer.setVisibility(View.GONE);
                    }
                }
                for (int i=0;i<flag.length;i++) {
                    String temp = "Compline Title:\n"+comtitle[i]+"\n"+
                            "Complain Descreption:\n"+complains[i]+"\n"+
                            "Complain Status:\n"+complains_s[i]+"\n";
                    if(flag[i].equals("p")){
                        p_com_pos= i ;
                        if(complains_s[i].equals("Answerd")){
                            temp +="Admin Answer:\n"+answers[i];
                            panswer.setVisibility(View.GONE);
                        }else{
                            temp +="Until now there is no answer from the admin";
                            panswer.setOnClickListener(booking_support_info.this);

                        }
                        pcom.setText(temp);
                    }else if(flag[i].equals("d")){
                        d_com_pos= i ;
                        if(complains_s[i].equals("Answerd")){
                            temp +="Admin Answer:\n"+answers[i];
                            danswer.setVisibility(View.GONE);
                        }else{
                            temp +="Until now there is no answer from the admin";
                            danswer.setOnClickListener(booking_support_info.this);
                        }
                        dcom.setText(temp);
                    }
                }
            } catch (JSONException e) {
                //Toast.makeText(booking_support_info.this, "Complints load "+e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

    }

    Intent intent;

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.panswer:
                intent = new Intent(booking_support_info.this,support_answer.class);
                intent.putExtra("id",c_id[p_com_pos]);
                intent.putExtra("ref",ref);
                intent.putExtra("title",comtitle[p_com_pos]);
                intent.putExtra("complain",complains[p_com_pos]);
                startActivity(intent);
                break;
            case R.id.danswer:
                intent = new Intent(booking_support_info.this,support_answer.class);
                intent.putExtra("id",c_id[d_com_pos]);
                intent.putExtra("ref",ref);
                intent.putExtra("title",comtitle[d_com_pos]);
                intent.putExtra("complain",complains[d_com_pos]);
                startActivity(intent);
                break;
    }
    }


    class BackGround extends AsyncTask<String, String, String> {

        String b_r;
        @Override
        protected String doInBackground(String... params) {
            b_r = params[0];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/getcomplain.php");
                String urlParams = "b_r="+b_r;

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


    }

}
