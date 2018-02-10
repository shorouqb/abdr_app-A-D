package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class support_answer extends BaseActivityA implements View.OnClickListener {

    TextView b_r;
    TextView title;
    TextView complain;

    int id;

    int A_ID;

    ImageView submit,cans;

    EditText answer;

    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_answer);
        submit=(ImageView)findViewById(R.id.submit);
        submit.setOnClickListener(this);


        prgDialog = new ProgressDialog(this);

        cans= (ImageView)findViewById(R.id.cans);
        cans.setOnClickListener(this);

        b_r = (TextView)findViewById(R.id.bookref);
        title = (TextView)findViewById(R.id.title);
        complain = (TextView)findViewById(R.id.complain);

        answer= (EditText)findViewById(R.id.answer);

        Intent i=getIntent();
        String ref=i.getStringExtra("ref");
        String tit=i.getStringExtra("title");
        String com =i.getStringExtra("complain");
        id = i.getIntExtra("id",0);
        A_ID=new localuser(this).getLoggedInUser().ID;

        b_r.setText(ref);
        title.setText(tit);
        complain.setText(com);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                prgDialog = ProgressDialog.show(this, "Wait Please...", "Adding Support Case...", false, true);
                prgDialog.setCancelable(false);
                prgDialog.setCanceledOnTouchOutside(false);
                support_answer.BackGround b = new support_answer.BackGround();
                b.execute(id+"",answer.getText().toString(),A_ID+"");
                super.onBackPressed();
                break;
            case R.id.cans:
                super.onBackPressed();
                break;

        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int id = Integer.parseInt(params[0]);
            String answer = params[1];
            int a_id =Integer.parseInt(params[2]);
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_complainanswer.php");
                String urlParams = "id="+id+"&answer="+answer+"&a_id="+a_id;
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
            if (s.equals("true")){
                Toast.makeText(support_answer.this, "Answered", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(support_answer.this, "Failed to answer ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
