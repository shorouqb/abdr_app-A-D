package com.example.vip.abdr_app_a_d;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class passenger_info extends BaseActivityA implements View.OnClickListener {

    String name;
    String phone;
    String nationality;
    String city;
    String email;
    String image;
    long id_number;
    int P_Id;
    int pos;
    Intent intent;

    TextView namet;
    TextView cityt;
    TextView nationalityt;
    TextView id_numbert;

    ImageView family;
    ImageView call;
    ImageView send_email;
    ImageView ccp;
    WebView id_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_info);
    }


    protected void onStart() {
        super.onStart();
        Intent i = getIntent();

        pos = i.getExtras().getInt("pos");
        name = i.getStringArrayExtra("nameF")[pos] +" "+ i.getStringArrayExtra("nameL")[pos];
        nationality = i.getStringArrayExtra("nationality")[pos];
        phone = i.getStringArrayExtra("phones")[pos];
        city = i.getStringArrayExtra("city")[pos];
        id_number = i.getLongArrayExtra("id_number")[pos];
        email = i.getStringArrayExtra("emails")[pos];
        image = i.getStringArrayExtra("images")[pos];
        P_Id=i.getIntArrayExtra("P_Id")[pos];

        namet=(TextView)findViewById(R.id.name);
        cityt=(TextView)findViewById(R.id.city);
        nationalityt=(TextView)findViewById(R.id.nationality);
        id_numbert=(TextView)findViewById(R.id.id_number);
        id_card=(WebView)findViewById(R.id.idimg);

        String data="<div align='center'> <img src='"+image+"' style='width:200px; height:150px;' /></div>";
        id_card.loadData(data, "text/html", "utf-8");
        id_card.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        namet.setText(name);
        cityt.setText(city);
        nationalityt.setText(nationality);
        id_numbert.setText(id_number+"");

        family=(ImageView)findViewById(R.id.family);
        family.setOnClickListener(this);
        call=(ImageView)findViewById(R.id.call);
        call.setOnClickListener(this);
        send_email=(ImageView)findViewById(R.id.ccp);
        send_email.setOnClickListener(this);
        ccp=(ImageView)findViewById(R.id.send_email);
        ccp.setOnClickListener(this);
    }
    Intent i;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
                break;
            case R.id.send_email:
                i= new Intent(passenger_info.this,send_email.class);
                i.putExtra("ID" , P_Id );
                i.putExtra("check" , 1 );
                startActivity(i);
                break;
            case R.id.family:
                i= new Intent(passenger_info.this,familymain.class);
                i.putExtra("P_Id" , P_Id );
                startActivity(i);
                break;
            case R.id.ccp:
                i= new Intent(passenger_info.this,p_payment.class);
                i.putExtra("P_Id" , P_Id );
                startActivity(i);
                break;

        }
    }
}
