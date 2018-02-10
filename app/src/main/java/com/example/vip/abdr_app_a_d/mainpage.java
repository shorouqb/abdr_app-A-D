package com.example.vip.abdr_app_a_d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class mainpage extends AppCompatActivity implements View.OnClickListener {
    Intent intent;localuser LU;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        LU=new localuser(this);
        b1=(Button)findViewById(R.id.btnlogin);

        b1.setOnClickListener(this);

    }
@Override
protected void onStart(){
 super.onStart();
    if (auth()==true){

            Intent startIntent = new Intent(mainpage.this, NotificationReciever.class);
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startService(startIntent);
        if(LU.getLoggedInUser().flag.equals("d")) {
            intent = new Intent(mainpage.this, mainbookings.class);
            startActivity(intent);
        }
        else if (LU.getLoggedInUser().flag.equals("a")){
            intent = new Intent(mainpage.this, home.class);
            startActivity(intent);
        }

    }

}

    private boolean auth(){
        return LU.getIfLoggedInUser();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin:
                intent = new Intent(mainpage.this,login.class);
                startActivity(intent);
                break;

        }

    }
}
