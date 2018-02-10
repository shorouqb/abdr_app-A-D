package com.example.vip.abdr_app_a_d;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class Show_support extends BaseActivityA implements View.OnClickListener {


    Spinner order;
    Spinner flag;
    Spinner status;
    ImageButton search;

    String []titles;
    String []ref;
    String []desc;
    String []answer;
    String []state;
    String []from;
    int []ids;
    GridView gv;
    ProgressDialog prgDialog;
    GridAdaptersupportA ga=new GridAdaptersupportA(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_supporte);

        order=(Spinner) findViewById(R.id.spinner_o);
        flag=(Spinner) findViewById(R.id.spinner_f);
        status=(Spinner) findViewById(R.id.spinner_s);

        search = (ImageButton) findViewById(R.id.Search_btn);
        search.setOnClickListener(this);
        prgDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Complaints...", false, true);
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);
        Show_support.BackGround b = new Show_support.BackGround();
        b.execute("All","descending","All");
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Search_btn:
                if(order.getSelectedItem().toString().equals("Select") || status.getSelectedItem().toString().equals("Select") || flag.getSelectedItem().toString().equals("Select")) {
                    showAlert();
                }else {
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Loading Comlaints...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    String order_S = order.getSelectedItem().toString();
                    String status_S = status.getSelectedItem().toString();
                    String flag_S = "";
                    if (flag.getSelectedItem().toString().equals("From drivers"))
                        flag_S = "d";
                    else if (flag.getSelectedItem().toString().equals("From passengers"))
                        flag_S = "p";
                    else
                        flag_S=flag.getSelectedItem().toString();
                    Show_support.BackGround b = new Show_support.BackGround();
                    b.execute(flag_S, order_S, status_S);
                }
                break;
        }
    }

    public void showAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Please you have to choose all (From , Status , Order) ..");

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

        @Override
        protected String doInBackground(String... params) {
            String status = params[2];
            String order = params[1];
            String flag = params[0];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/admin_Allcomplains.php");
                String urlParams = "status="+status+"&order="+order+"&flag="+flag;

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
            String err=null;
            prgDialog.dismiss();

            if(s.equals("false")){
                Toast.makeText(Show_support.this, "No complaints", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");

                    if(array.length()>0){
                        titles=new String[array.length()];
                        state=new String[array.length()];
                        ref=new String[array.length()];
                        desc=new String[array.length()];
                        answer=new String[array.length()];
                        ids=new int[array.length()];
                        from=new String[array.length()];
                    for (int i=0;i<array.length();i++){
                        titles[i]=array.getJSONObject(i).getString("title");
                        state[i]=array.getJSONObject(i).getString("complain_status");
                        from[i]=array.getJSONObject(i).getString("flag");
                        if(from[i].equals("p"))
                            ref[i]=array.getJSONObject(i).getString("booking_reference_c");
                        else
                            ref[i]=array.getJSONObject(i).getString("booking_ref");
                        desc[i]=array.getJSONObject(i).getString("complain_descreption");
                        answer[i]=array.getJSONObject(i).getString("admin_answer");
                        ids[i]=array.getJSONObject(i).getInt("complain_id");
                    }
                        gv=(GridView)findViewById(R.id.support_grid);
                        ga=new GridAdaptersupportA(Show_support.this,titles,state,ref,desc,answer,from,ids);
                        gv.setAdapter(ga);
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                Intent i=new Intent(Show_support.this,support_info.class);
                                i.putExtra("titles",titles);
                                i.putExtra("state",state);
                                i.putExtra("ref",ref);
                                i.putExtra("desc",desc);
                                i.putExtra("answer",answer);
                                i.putExtra("id",ids);
                                i.putExtra("pos",position);
                                startActivity(i);
                            }
                        });
                 }
                    else {
                        Toast.makeText(Show_support.this, "No complaints according to your filters", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //Toast.makeText(Show_support.this, "complains load "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }


        }
    }
}
