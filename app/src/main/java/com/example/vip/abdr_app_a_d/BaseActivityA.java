package com.example.vip.abdr_app_a_d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivityA extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenuea, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.home:
               startActivity(new Intent(this, home.class));
                return true;
            case R.id.Passengers:
                finish();
                startActivity(new Intent(this, Show_passengers.class));
                return true;
            case R.id.Drivers:
                finish();
                startActivity(new Intent(this, Show_drivers.class));
                return true;
            case R.id.admins:
                finish();
                startActivity(new Intent(this, Show_admins.class));
                return true;
            case R.id.bookings:
                finish();
                startActivity(new Intent(this, bookings.class));
                return true;
            case R.id.Emails:
                Intent n= new Intent(this,main_send_email.class);
                startActivity(n);
                return true;
            case R.id.support:
                finish();
                startActivity(new Intent(this, Show_support.class));
                return true;
            case R.id.logout:
                new localuser(this).clear();
                finish();
                startActivity(new Intent(this, mainpage.class));

                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
